package com.dormManager.backend.filter;

import com.dormManager.backend.utils.Const;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 跨域配置过滤器，仅处理跨域，添加跨域响应头
 */
@Component("customCorsFilter")
@Order(Const.ORDER_CORS)
public class CorsFilter extends HttpFilter {

    @Value("${spring.web.cors.origin}")
    String origin;

    @Value("${spring.web.cors.credentials}")
    boolean credentials;

    @Value("${spring.web.cors.methods}")
    String methods;

    /**
     * 这是一个过滤器方法，用于处理HTTP请求。在执行实际的过滤操作之前，会调用addCorsHeader方法，
     * 该方法的作用是添加跨域资源共享（CORS）相关的头部信息。然后，通过调用chain.doFilter(request, response)
     * 继续执行过滤器链中的下一个过滤器，或者最终的请求处理器。
     *
     * @param request  HTTP请求对象
     * @param response HTTP响应对象
     * @param chain    过滤器链
     * @throws IOException      如果发生I/O错误
     * @throws ServletException 如果发生Servlet错误
     */
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 在执行实际的过滤操作之前，添加跨域资源共享（CORS）头部信息
        this.addCorsHeader(request, response);

        // 继续执行过滤器链中的下一个过滤器，或者最终的请求处理器
        chain.doFilter(request, response);
    }

    /**
     * 添加所有跨域相关响应头
     * @param request  HTTP请求对象，用于获取原始请求的信息
     * @param response HTTP响应对象，用于添加CORS头部信息
     */
    private void addCorsHeader(HttpServletRequest request, HttpServletResponse response) {
        // 添加允许的来源（Origin）到Access-Control-Allow-Origin头部
        response.addHeader("Access-Control-Allow-Origin", this.resolveOrigin(request));

        // 添加允许的HTTP方法到Access-Control-Allow-Methods头部
        response.addHeader("Access-Control-Allow-Methods", this.resolveMethod());

        // 添加允许的HTTP请求头部到Access-Control-Allow-Headers头部
        response.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");

        // 如果支持凭证（credentials），则设置Access-Control-Allow-Credentials头部为true
        if(credentials) {
            response.addHeader("Access-Control-Allow-Credentials", "true");
        }
    }

    /**
     * 解析并返回允许的HTTP方法字符串。
     * 如果配置为允许所有方法（"*"），则返回预定义的常用HTTP方法字符串；
     * 否则，返回配置中指定的方法字符串。
     *
     * @return 允许的HTTP方法字符串
     */
    private String resolveMethod(){
        return methods.equals("*") ? "GET, HEAD, POST, PUT, DELETE, OPTIONS, TRACE, PATCH" : methods;
    }

    /**
     * 解析并返回允许的跨域请求的来源（Origin）字符串。
     * 如果配置为允许所有来源（"*"），则返回当前请求的实际Origin；
     * 否则，返回配置中指定的来源字符串。
     *
     * @param request HTTP请求对象，用于获取实际请求的Origin头部信息
     * @return 允许的跨域请求的来源（Origin）字符串
     */
    private String resolveOrigin(HttpServletRequest request){
        return origin.equals("*") ? request.getHeader("Origin") : origin;
    }
}