package com.gzseeing.manager.controller;

import com.gzseeing.utils.LogUtils;
import com.gzseeing.sys.model.AppData;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.stereotype.Controller;
import com.gzseeing.sys.springmvc.annotation.Appdata;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("test_todel")
public class TestController {

	@RequestMapping("/222")
	@ResponseBody
	public String result(){
		LogUtils.info("doing in cotroller!!!***************** ////");

		return "ddd";
	}
	@RequestMapping("/test")
	//@ResponseBody
	
	public String result2(){
		LogUtils.info("doing in cotroller!!!***************** test");
		return "test";
	}
	
	@RequestMapping("/test2")
	@ResponseBody
	public String result2d2(AppData appdata,@Appdata  Integer a){
		System.out.println("appData:---->"+appdata);
		System.out.println(a);
		return "null";
	}
}
