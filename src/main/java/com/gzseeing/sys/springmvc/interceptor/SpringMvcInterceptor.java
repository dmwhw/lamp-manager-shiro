package com.gzseeing.sys.springmvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.gzseeing.sys.springmvc.annotation.SpringHandlerInterceptor;
import com.gzseeing.utils.LogUtils;

 
/**
 * 
 * 
 * （权限、登录）顶号
 * @author haowen
 * 
 * 
 *
 */
//@SpringHandlerInterceptor(order=1,includePatterns="/**")
public class SpringMvcInterceptor implements HandlerInterceptor {
 	     
	    
	    /**   
	     * 进入controller层之前拦截请求   
	     * @param httpServletRequest   
	     * @param httpServletResponse   
	     * @param o   
	     * @return   
	     * @throws Exception   
	     */    
	    @Override    
	    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {    
	    
	        LogUtils.info("---------------------开始进入请求地址拦截----------------------------");    
	        HttpSession session = httpServletRequest.getSession();    

	        if(!org.springframework.util.StringUtils.isEmpty(session.getAttribute("userName"))){    
	            return true;    
	        }    
	        else{    
	            //PrintWriter printWriter = httpServletResponse.getWriter();    
	            //printWriter.write("{code:0,message:\"session is invalid,please login again!\"}");    
	            return true;    
	        }    
	    
	    }    
	    
	    @Override    
	    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {    
	    	LogUtils.info("--------------处理请求完成后视图渲染之前的处理操作---------------");    
	    }    
	    
	    @Override    
	    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {    
	    	LogUtils.info("---------------视图渲染之后的操作-------------------------0");    
	    }    
	
}
