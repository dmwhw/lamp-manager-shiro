package com.gzseeing.manager.controller.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzseeing.manager.entity.SoftwareVersion;
import com.gzseeing.manager.service.SoftwareVersionService;
import com.gzseeing.sys.model.PageBean;
import com.gzseeing.sys.model.R;

@Controller
@RequestMapping("/api/web/software-version")
public class SoftWareVersionController {

	
	@Autowired
	private SoftwareVersionService softwareVersionService;
	
	
	/**
	 * 拿版本的分页结果
	 * @author haowen
	 * @time 2018年8月28日下午5:14:38
	 * @Description  
	 * @param mainVersion
	 * @param subVersion
	 * @param remark
	 * @param versionName
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/pagebean")
	@ResponseBody
	public R ajaxGetVersionPageBean(
			Integer mainVersion,
			Integer subVersion,
			String remark,
			String versionName,
			Integer page,
			Integer rows){
		PageBean<SoftwareVersion> softWareVersionPageBean = softwareVersionService.getSoftWareVersionByPage(remark,versionName, mainVersion, subVersion, page, rows);
		R r = R.ok().add("pageBean", softWareVersionPageBean);
		//r.put("list", value);
		r.put("rows",softWareVersionPageBean.getList() );
		r.put("total",softWareVersionPageBean.getTotalCount());
		return r;
	}
	
}
