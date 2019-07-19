package com.gzseeing.sys.web.filter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gzseeing.utils.LogUtils;

//@WebFilter(urlPatterns = "/*",filterName = "blosTest")//filter名字 没有排序，默认用的名字排序，不好
public class MyFilter implements Filter{
	@Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
/*
 * 最简单明了的区别就是过滤器可以修改request，而拦截器不能
过滤器需要在servlet容器中实现，拦截器可以适用于javaEE，javaSE等各种环境
拦截器可以调用IOC容器中的各种依赖，而过滤器不能
过滤器只能在请求的前后使用，而拦截器可以详细到每个方法
 * 
 * */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if(request.getDispatcherType().equals(DispatcherType.REQUEST)){
        	LogUtils.info("MyFilter***************************"+(request.getRequestURI()));
        }
        filterChain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
