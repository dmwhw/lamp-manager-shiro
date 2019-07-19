package com.gzseeing.manager.controller.api.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzseeing.manager.entity.DeviceNo;
import com.gzseeing.manager.service.DeviceNoService;
import com.gzseeing.sys.model.R;

@Controller("api_deviceNoController")
@RequestMapping("/api/web/device-no")
public class DeviceNoController {

	
	
	@Autowired
	private DeviceNoService deviceNoService;
	
	
	
	/**
	 * 获得所有型号列表
	 * @author haowen
	 * @time 2018年10月25日下午6:01:33
	 * @Description  
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public R ajaxGetList(){
		List<DeviceNo> list = deviceNoService.getAllList();
		return R.ok().add("list", list).add("size", list==null?0:list.size());
	}
	
	
	
}
