package com.dormManager.backend.filter;

import com.dormManager.backend.utils.Const;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 跨域请求过滤器，用于添加CORS（跨域资源共享）头信息，解决前端跨域请求问题。
 */
@Component("customCorsFilter")
@Order(Const.ORDER_CORS)
public class CorsFilter extends HttpFilter {

    /**
     * 对请求进行过滤，在响应头中添加CORS信息，然后将请求传递给下一个过滤器或目标资源。
     *
     * @param request  HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @param chain    FilterChain对象，用于继续请求的处理流程
     * @throws IOException      如果发生I/O错误
     * @throws ServletException 如果发生与servlet相关的错误
     */
    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {
        this.addCorsHeader(request, response);
        chain.doFilter(request, response);
    }

    /**
     * 向响应头中添加CORS信息。
     *
     * @param request  HttpServletRequest对象
     * @param response HttpServletResponse对象
     */
    private void addCorsHeader(HttpServletRequest request,
                               HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.addHeader("Access-Control-Allow-Methods", request.getHeader("GET, POST, PUT, DELETE, OPTIONS"));
        response.addHeader("Access-Control-Allow-Headers", request.getHeader("Authorization, Content-Type"));
    }
}
