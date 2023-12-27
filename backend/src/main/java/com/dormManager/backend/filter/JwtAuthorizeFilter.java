package com.dormManager.backend.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.dormManager.backend.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT授权过滤器，用于验证和解析请求中的JWT令牌，并将用户信息添加到Spring Security上下文中。
 */
@Component
public class JwtAuthorizeFilter extends OncePerRequestFilter {

    @Resource
    JwtUtils utils;

    /**
     * 在内部执行过滤操作，处理HTTP请求，验证JWT（JSON Web Token），并设置安全上下文。
     *
     * @param request     HTTP请求对象
     * @param response    HTTP响应对象
     * @param filterChain 过滤器链
     * @throws ServletException 如果发生Servlet错误
     * @throws IOException      如果发生I/O错误
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头部获取Authorization信息
        String authorization = request.getHeader("Authorization");

        // 解析Authorization信息获取JWT（JSON Web Token）
        DecodedJWT jwt = utils.resolveJwt(authorization);

        // 如果JWT不为空，进行身份验证
        if (jwt != null) {
            // 将JWT转换为用户信息
            UserDetails user = utils.toUser(jwt);

            // 创建身份验证令牌
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            // 设置身份验证令牌的详细信息
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 将身份验证令牌设置到安全上下文中
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // 将JWT中的用户ID设置到请求属性中
            request.setAttribute("id", utils.toId(jwt));
        }

        // 继续执行过滤器链中的下一个过滤器，或者最终的请求处理器
        filterChain.doFilter(request, response);
    }
}
