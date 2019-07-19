package com.gzseeing.sys.springmvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.gzseeing.sys.model.exception.MsgException;
import com.gzseeing.sys.springmvc.annotation.Auth;
import com.gzseeing.sys.springmvc.annotation.SpringHandlerInterceptor;
import com.gzseeing.sys.varriable.Dictionary;
import com.gzseeing.utils.LogUtils;
import com.gzseeing.utils.RedisUtils;
import com.gzseeing.utils.StringUtils;

@SpringHandlerInterceptor(order=1,includePatterns={"/api/app/**"})
public class AuthInterceptor implements HandlerInterceptor {

	  
    @Override  
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object obj) throws Exception {
    	if (obj !=null && obj instanceof HandlerMethod){
    		LogUtils.debug("goto-->"+request.getRequestURL());
	    	String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");  
	        //get auth info
	        HandlerMethod method=(HandlerMethod) obj;
	        
	        boolean isEmptyToken=false;
	        String token = getToken(request);
	        if (token==null||"".equals(token.trim())){
	        	isEmptyToken=true;
	        }else{
	        	//续命token
	        }
	        
	        //need auth
	        Auth auth = method.getMethodAnnotation(Auth.class);
	        if (auth==null){//不用auth
	        	return true;
	        }
	        //empty token?
	        if (isEmptyToken){
	        	throw new MsgException(90002, "token 无效");
	        }

	        String userKey = auth.requireUser();
	        if(StringUtils.isNull(userKey)){
	        	LogUtils.debug("{}未定义登录用户标志",request.getRequestURL());
	        	return true;
	        }
	        //检查登录   
	        LoginUser loginUser = RedisUtils.getObjectRedisDao().getByMapKey(token, Dictionary.APP_USER, LoginUser.class);
	        if (loginUser==null){
	        	throw new  MsgException(90001,"用户未登录");
	        }
	        //异地登录检查
	        String userId=loginUser.getLoginId();
	        String nowToken=RedisUtils.getStringRedisDao().get(Dictionary.APP_USER+":"+userId);
	        if (nowToken==null||!nowToken.equals(token)){
	        	throw new  MsgException(90001,"异地登录");
	        }
	        if(auth.isAuth()){
	        	//TODO 查询url列表
	        }
	        //TODO role
	        
        	//授权检查（url、角色）
	        //RedisUtils.get
	        
    	}
    	//其他资源
    	return true;
    }
    
    

    /**
     * 提供获取token的方法
     * @author haowen
     * @time 2018年7月21日下午3:15:41
     * @Description  
     * @param request
     * @return
     */
    public String getToken(HttpServletRequest request){
    	return request.getParameter("token");
    }
    
    
    
    
    @Override  
    public void afterCompletion(HttpServletRequest arg0,  
            HttpServletResponse arg1, Object arg2, Exception arg3)  
            throws Exception {  
  
    }  
  
    @Override  
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,  
            Object arg2, ModelAndView arg3) throws Exception {  
  
    } 
 
    
    public static interface LoginUser{
    	String getLoginId();
    }
    

}
