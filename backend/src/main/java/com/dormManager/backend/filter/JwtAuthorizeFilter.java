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
     * 在每个请求中执行的方法，用于验证和解析JWT令牌，并将用户信息添加到Spring Security上下文中。
     *
     * @param request     HttpServletRequest对象
     * @param response    HttpServletResponse对象
     * @param filterChain FilterChain对象，用于继续请求的处理流程
     * @throws ServletException 如果发生与servlet相关的错误
     * @throws IOException      如果发生I/O错误
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        DecodedJWT jwt = utils.resolveJwt(authorization);
        if (jwt != null) {
            UserDetails user = utils.toUser(jwt);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            request.setAttribute("id", utils.toId(jwt));
        }

        filterChain.doFilter(request, response);
    }
}
