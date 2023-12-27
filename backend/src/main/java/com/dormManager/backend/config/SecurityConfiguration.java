package com.dormManager.backend.config;

import com.dormManager.backend.entity.RestBean;
import com.dormManager.backend.entity.dto.Account;
import com.dormManager.backend.entity.vo.response.AuthorizeVO;
import com.dormManager.backend.filter.JwtAuthorizeFilter;
import com.dormManager.backend.service.AccountService;
import com.dormManager.backend.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Spring Security配置类，用于配置认证、授权、过滤器等安全相关的设置。
 */
@Configuration
public class SecurityConfiguration {

    @Resource
    JwtUtils utils;

    @Resource
    JwtAuthorizeFilter jwtAuthorizeFilter;

    @Resource
    AccountService accountService;

    /**
     * 配置Spring Security的过滤器链。
     *
     * <p>该方法设置了请求授权、登录、注销、异常处理等配置，并添加了JWT授权过滤器。</p>
     *
     * @param http HttpSecurity对象，用于配置过滤器链
     * @return 配置完成的SecurityFilterChain对象
     * @throws Exception 可能抛出的异常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf -> {
                    conf.requestMatchers("/api/auth/**").permitAll();
                    conf.anyRequest().authenticated();
                })
                .formLogin(conf -> {
                    conf.loginProcessingUrl("/api/auth/login");
                    conf.failureHandler(this::onAuthenticationFailure);
                    conf.successHandler(this::onAuthenticationSuccess);
                })
                .logout(conf -> {
                    conf.logoutUrl("/api/auth/logout");
                    conf.logoutSuccessHandler(this::onLogoutSuccess);
                })
                .exceptionHandling(conf -> {
                    conf.authenticationEntryPoint(this::onUnAuthentication);
                    conf.accessDeniedHandler(this::onAccessDeny);
                })
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(conf -> {
                    conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilterBefore(jwtAuthorizeFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * 处理访问被拒绝的情况的方法。
     *
     * @param request               HttpServletRequest对象
     * @param response              HttpServletResponse对象
     * @param accessDeniedException 访问被拒绝时抛出的AccessDeniedException异常
     * @throws IOException      如果发生I/O错误
     * @throws ServletException 如果发生与servlet相关的错误
     */
    public void onAccessDeny(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(RestBean.forbidden(accessDeniedException.getMessage()).asJsonString());
    }

    /**
     * 处理未经身份验证的访问的方法。
     *
     * @param request         HttpServletRequest对象
     * @param response        HttpServletResponse对象
     * @param authException   访问未经身份验证时抛出的AuthenticationException异常
     * @throws IOException      如果发生I/O错误
     * @throws ServletException 如果发生与servlet相关的错误
     */
    public void onUnAuthentication(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(RestBean.unauthorized(authException.getMessage()).asJsonString());
    }

    /**
     * 处理身份验证成功的方法。
     *
     * @param request         HttpServletRequest对象
     * @param response        HttpServletResponse对象
     * @param authentication  Authentication对象，包含身份验证成功的用户信息
     * @throws IOException      如果发生I/O错误
     * @throws ServletException 如果发生与servlet相关的错误
     */
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        User user = (User) authentication.getPrincipal();
        Account account = accountService.findAccountByNameOrEmail(user.getUsername());

        String token = utils.createJwt(user, account.getId(), account.getUsername());

        AuthorizeVO vo = new AuthorizeVO();
        vo.setExpireTime(utils.expireTime());
        vo.setRole(account.getRole());
        vo.setToken(token);
        vo.setUsername(account.getUsername());

        response.getWriter().write(RestBean.success(vo).asJsonString());
    }

    /**
     * 处理身份验证失败的方法。
     *
     * @param request         HttpServletRequest对象
     * @param response        HttpServletResponse对象
     * @param exception       身份验证失败时抛出的AuthenticationException异常
     * @throws IOException      如果发生I/O错误
     * @throws ServletException 如果发生与servlet相关的错误
     */
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(RestBean.failure(401, exception.getMessage()).asJsonString());
    }

    /**
     * 处理退出登录成功的方法。
     *
     * @param request         HttpServletRequest对象
     * @param response        HttpServletResponse对象
     * @param authentication  Authentication对象，包含退出登录的用户信息
     * @throws IOException      如果发生I/O错误
     * @throws ServletException 如果发生与servlet相关的错误
     */
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        String authorization = request.getHeader("Authorization");
        if (utils.invalidateJwt(authorization)) {
            writer.write(RestBean.success().asJsonString());
        } else {
            writer.write(RestBean.failure(400, "退出登录失败").asJsonString());
        }
    }
}
