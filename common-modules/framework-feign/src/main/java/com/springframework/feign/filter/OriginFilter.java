package com.springframework.feign.filter;

import com.springframework.feign.annotation.OriginConfigProperties;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

/**
 * @author summer
 * 2018/7/31
 */
public class OriginFilter implements Filter {
    private static final Set<String> originProperties = OriginConfigProperties.getOriginProperties();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) request;
        HttpServletRequest req = (HttpServletRequest) response;
        //feign客户端，在调用的时候都自动增加了一个头信息：X-FeignOrigin，内容为客户端服务名
        final String feignOriginHeader = req.getHeader("X-FeignOrigin");
        //feign服务端，服务提供方通过Fitler的方式实现了读取HTTP头信息，
        // 根据自己接口定义和X-FeignOrigin中信息的关系判断是否过滤，
        // 该过滤器需要通过security.origin-filter.enabled参数开启
        if (originProperties.isEmpty() || originProperties.contains(feignOriginHeader)) {
            chain.doFilter(req, resp);
        } else {
            resp.sendError(SC_FORBIDDEN, "服务消费方不在提供方提供范围内");
        }
    }

    @Override
    public void destroy() {

    }
}
