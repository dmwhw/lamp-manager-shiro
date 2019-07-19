package com.gzseeing.manager.controller.api.web;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzseeing.manager.entity.AppVersion;
import com.gzseeing.manager.service.AppVersionService;
import com.gzseeing.sys.model.R;

/**
 * <p>
 * App版本升级表 前端控制器
 * </p>
 *
 * @author Haowen
 * @since 2018-10-26
 */
@Controller
@RequestMapping("/api/web/app-version")
public class AppVersionController {

	
	@Autowired
	private AppVersionService appVersionService;
	
	@RequestMapping("/lists-by-system")
	@ResponseBody
	public R ajaxGetAllAppVersion(){	
		List<AppVersion> android = appVersionService.getAndroidAllList();
		List<AppVersion> ios = appVersionService.getIOSAllList();
		return R.ok().add("ios", ios).add("android", android);
	}
}

