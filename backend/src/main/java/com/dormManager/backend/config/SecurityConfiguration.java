package com.dormManager.backend.config;

import com.dormManager.backend.entity.RestBean;
import com.dormManager.backend.entity.vo.response.AuthorizeVO;
import com.dormManager.backend.filter.JwtAuthorizeFilter;
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
 * SecurityConfiguration 类配置了应用程序的安全设置。
 * 它定义了安全规则、登录/注销端点以及身份验证成功/失败处理器。
 *
 * <p>此配置使用无状态会话管理策略并禁用了 CSRF 保护。
 * 还提供了用于身份验证成功和失败以及注销成功的自定义处理器。</p>
 *
 * <p>例如：</p>
 * <pre>
 *     &#64;Configuration
 *     public class SecurityConfig extends WebSecurityConfigurerAdapter {
 *
 *         &#64;Autowired
 *         private SecurityConfiguration securityConfiguration;
 *
 *         &#64;Override
 *         protected void configure(HttpSecurity http) throws Exception {
 *             http.apply(securityConfiguration.filterChain());
 *         }
 *     }
 * </pre>
 */
@Configuration
public class SecurityConfiguration {

    @Resource
    JwtUtils utils;

    @Resource
    JwtAuthorizeFilter jwtAuthorizeFilter;

    /**
     * 配置 HTTP 请求的安全过滤器链。
     *
     * @param http 要配置的 HttpSecurity 对象
     * @return 配置好的 SecurityFilterChain
     * @throws Exception 如果在配置过程中发生错误
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
     * <p>当用户尝试访问没有足够权限的资源时，将调用此方法。它设置响应的内容类型为 JSON，然后将包含拒绝访问消息的
     * RestBean 对象写入响应。</p>
     *
     * <p>例如：</p>
     * <pre>
     *     // 在配置中指定访问被拒绝时的处理器
     *     .exceptionHandling()
     *         .accessDeniedHandler(this::onAccessDeny)
     * </pre>
     *
     * @param request               HttpServletRequest
     * @param response              HttpServletResponse
     * @param accessDeniedException 访问被拒绝时抛出的 AccessDeniedException 异常
     * @throws IOException      如果发生 I/O 错误
     * @throws ServletException 如果发生与 servlet 相关的错误
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
     * <p>当用户尝试访问需要身份验证的资源但未提供有效凭证时，将调用此方法。它设置响应的内容类型为 JSON，然后将包含
     * 未经授权消息的 RestBean 对象写入响应。</p>
     *
     * <p>例如：</p>
     * <pre>
     *     // 在配置中指定未经身份验证时的处理器
     *     .exceptionHandling()
     *         .authenticationEntryPoint(this::onUnAuthentication)
     * </pre>
     *
     * @param request         HttpServletRequest
     * @param response        HttpServletResponse
     * @param authException   访问未经身份验证时抛出的 AuthenticationException 异常
     * @throws IOException      如果发生 I/O 错误
     * @throws ServletException 如果发生与 servlet 相关的错误
     */
    public void onUnAuthentication(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(RestBean.unauthorized(authException.getMessage()).asJsonString());
    }

    /**
     * 处理身份验证成功，向响应中写入 "Success"。
     *
     * @param request        HttpServletRequest
     * @param response       HttpServletResponse
     * @param authentication Authentication 对象
     * @throws IOException      如果发生 I/O 错误
     * @throws ServletException 如果发生与 servlet 相关的错误
     */
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        /**
         * 设置字符编码，使显示中文
         */
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        User user = (User) authentication.getPrincipal();
        String token = utils.createJwt(user, 1, "huazaiki");
        AuthorizeVO vo = new AuthorizeVO();
        vo.setExpireTime(utils.expireTime());
        vo.setRole("");
        vo.setToken(token);
        vo.setUsername(user.getUsername());

        response.getWriter().write(RestBean.success(vo).asJsonString());
    }

    /**
     * 处理身份验证失败，向响应中写入失败信息。
     *
     * @param request    HttpServletRequest
     * @param response   HttpServletResponse
     * @param exception  AuthenticationException 对象
     * @throws IOException      如果发生 I/O 错误
     * @throws ServletException 如果发生与 servlet 相关的错误
     */
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(RestBean.failure(401, exception.getMessage()).asJsonString());
    }

    /**
     * 处理注销成功，可在此添加特定操作。
     *
     * @param request        HttpServletRequest
     * @param response       HttpServletResponse
     * @param authentication Authentication 对象
     * @throws IOException      如果发生 I/O 错误
     * @throws ServletException 如果发生与 servlet 相关的错误
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
