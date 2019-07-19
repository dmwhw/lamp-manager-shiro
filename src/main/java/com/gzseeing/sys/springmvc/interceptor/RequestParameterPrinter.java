package com.gzseeing.sys.springmvc.interceptor;
//@SpringHandlerInterceptor(order=1,includePatterns="/**")

import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.gzseeing.sys.springmvc.annotation.SpringHandlerInterceptor;
import com.gzseeing.utils.LogUtils;

@SpringHandlerInterceptor(order=1,includePatterns="/api/**")
public class RequestParameterPrinter  implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse arg1, Object arg2) throws Exception {
		Map<String, String[]> parameterMap = req.getParameterMap();
		LogUtils.info("****url*****{}",req.getRequestURI());
		if (parameterMap==null||parameterMap.isEmpty()){
			LogUtils.info("***null request param"+req.getRequestURI());
			return true;
		}
		Iterator<Entry<String, String[]>> iterator = parameterMap.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, String[]> next = iterator.next();
			LogUtils.info("**receive{}-->[{}]",next.getKey(),Arrays.toString(next.getValue()) );
		}
		return true;
	}

}
