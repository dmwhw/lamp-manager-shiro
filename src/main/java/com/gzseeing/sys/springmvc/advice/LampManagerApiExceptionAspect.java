package com.gzseeing.sys.springmvc.advice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.WebUtils;

import com.gzseeing.sys.model.exception.MyException;
import com.gzseeing.utils.LogUtils;

@ControllerAdvice(basePackages="com.gzseeing.manager.controller.api.web")
public class LampManagerApiExceptionAspect {

	
	@ExceptionHandler({ MyException.class })
	@ResponseBody
	public String handleMsgException(Exception e,HttpServletRequest req,HttpServletResponse res) {
		res.setStatus(200);
		req.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, 200);
		e.printStackTrace();
		LogUtils.error("What The  ---sgException。。。");
		System.out.println(req.getRequestURI());
		return "testArrayIndexOutOfBoundsException";
	}
	
	@ExceptionHandler({ Throwable.class })
	@ResponseBody
	public String handleException(Exception e,HttpServletRequest req,HttpServletResponse res) throws Exception {

		System.out.println("tjrw");
		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null){
			throw e;
		}
		
		res.setStatus(200);
		req.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, 200);
		LogUtils.error("What The {}",LogUtils.getStackTrace(e));
		System.out.println(req.getRequestURI());
		return "testArrayIndexOutOfBoundsException";
	}
	
}
