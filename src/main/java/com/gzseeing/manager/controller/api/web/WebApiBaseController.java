package com.gzseeing.manager.controller.api.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;

import com.gzseeing.sys.model.R;
import com.gzseeing.utils.View;

public abstract class WebApiBaseController {

	
	
	@ExceptionHandler
	public String processLoginNotAuthentication(HttpServletRequest request, HttpServletResponse response){
	    boolean isAjax =  "XMLHttpRequest".equalsIgnoreCase(request.getHeader("x-requested-with"));
        if (isAjax) {
        	R r = R.fail();
        	View.returnJson(r, response);
        	return null;
        } else {
        	return "redirect:/login.html";
        }

	}
	
	
}
