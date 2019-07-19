package com.gzseeing.manager.controller.api.web;

import java.util.Map;
import java.util.Date;
import com.gzseeing.sys.model.R;
import com.gzseeing.sys.model.PageBean;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import com.gzseeing.manager.service.DeviceImportLogService;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Haowen
 * @since 2018-08-30
 */
@Controller
@RequestMapping("/api/web/deviceImportLog")
public class DeviceImportLogController {

	@Autowired
	private DeviceImportLogService deviceImportLogService;
	
	@RequestMapping("/page")
	@ResponseBody
	public R logPage(HttpServletRequest request,String username, String status, @DateTimeFormat(pattern="yyyy-MM-dd") Date start, @DateTimeFormat(pattern="yyyy-MM-dd") Date end, Integer page, Integer rows) {
		PageBean<Map<String, Object>> pageBean = deviceImportLogService.getPage(page, rows, username, status, start, end);
		R r = R.ok();
		r.put("rows", pageBean.getList());
		r.put("total", pageBean.getTotalCount());
		return r;
	}
	
}

