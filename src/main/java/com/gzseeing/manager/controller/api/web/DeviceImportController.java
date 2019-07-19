package com.gzseeing.manager.controller.api.web;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;
import com.gzseeing.utils.View;
import java.util.regex.Pattern;
import com.gzseeing.sys.model.R;
import com.gzseeing.utils.PathUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import com.gzseeing.manager.service.DeviceImportService;
import com.gzseeing.manager.service.DeviceTestService;
import com.gzseeing.manager.service.DeviceImportLogService;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 设备临时导入表 前端控制器
 * </p>
 *
 * @author Haowen
 * @since 2018-08-29
 */
@Controller
@RequestMapping("/api/web/deviceImport")
public class DeviceImportController {
	
	@Autowired
	private DeviceImportService deviceImportService;
	@Autowired
	private DeviceImportLogService deviceImportLogService;

	@Autowired
	private DeviceTestService deviceTestService;
	
	@RequestMapping("/check")
	@ResponseBody
	public R check(HttpServletRequest request,
					@RequestParam(value="macs",required=false) String[] macs,
					@RequestParam(value="codes",required=false) String[] codes,
					@RequestParam( value="file",required=false) MultipartFile file
				) {
		String fileName = backupFile(file);
		macs = dealMac(macs);
		
		List<Integer> wrong = getWrong(macs);
		List<Integer> repeat = getRepeat(macs);
		Map<String, List<Integer>> check = deviceImportService.check(macs, codes);
		List<Integer> exit = check.get("exitList");
		List<Integer> wrongCode = check.get("wrongCode");
		//TODO 登陆后的用户id没加
		deviceImportLogService.record(fileName, 1, "相同数据:" + repeat.size() + ",数据库重复数据:" + exit.size() + ",错误MAC:" + wrong.size() + ",错误型号代码:" + wrongCode.size(), "检查");
		
		R r = R.ok();
		r.add("repeat", repeat).add("wrong", wrong).add("exit", exit).add("wrongCode", wrongCode);
		return r;
	}
	
	@RequestMapping("/import")
	@ResponseBody
	public R importMacs(HttpServletRequest request,
			@RequestParam(value="macs",required=false) String[] macs,
			@RequestParam(value="codes",required=false) String[] codes,
			@RequestParam( value="file",required=false) MultipartFile file
			) {
		String fileName = backupFile(file);
		macs = dealMac(macs);
		List<Integer> wrong = getWrong(macs);
		List<Integer> repeat = getRepeat(macs);
		Map<String, List<Integer>> check = null;
		if (wrong.size() == 0 && repeat.size() == 0) {
			check = deviceImportService.importMacs(macs, codes);
		}else {
			check = deviceImportService.check(macs, codes);
		}
		if (check == null) {
			//TODO 登陆后的用户id没加
			deviceImportLogService.record(fileName, null, null, "系统繁忙");
			R r = R.fail();
			return r;
		}else if (wrong.size() == 0 && repeat.size() == 0 && check.get("exitList").size() == 0 && check.get("wrongCode").size() == 0) {
			//导入测试表
			List<Integer> deviceIds= check.get("deviceIds");
			deviceTestService.addDeviceTestRecord(deviceIds);
			//TODO 登陆后的用户id没加
			deviceImportLogService.record(fileName, null, null, "导入成功");
			
		}else {
			//TODO 登陆后的用户id没加
			List<Integer> exit = check.get("exitList");
			List<Integer> wrongCode = check.get("wrongCode");
			deviceImportLogService.record(fileName, 1, "相同数据:" + repeat.size() + ",数据库重复数据:" + exit.size() + ",错误MAC:" + wrong.size() + ",错误型号代码:" + wrongCode.size(), "导入失败");
		}
		
		List<Integer> exit = check.get("exitList");
		List<Integer> wrongCode = check.get("wrongCode");
		R r = R.ok();
		r.add("repeat", repeat).add("wrong", wrong).add("exit", exit).add("wrongCode", wrongCode);
		return r;
	}
	
	@RequestMapping("/down")
	public void down(HttpServletRequest request, HttpServletResponse response) {
		String webAppsPath = PathUtils.getTomcatPath();
		String path = webAppsPath + "/excel/mac.xlsx";
		File file = new File(path);
		View.returnFile(file, "台灯mac导入模板.xlsx", response);
	}
	
	private String[] dealMac(String[] macs) {
		for (int i = 0; i < macs.length; i++) {
			macs[i] = macs[i].toLowerCase().replaceAll("-", ":");
		}
		return macs;
	}
	
	private String backupFile(MultipartFile file){
		String fileName = file.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		String fileName2 =  UUID.randomUUID().toString() + "." + suffix;
		try {
			File file2 = new File("D:/Project/lamp/code/lamp-manager-springboot/excel/" + fileName + "_" + fileName2);
			file.transferTo(file2);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName + "_" + fileName2;
	}
	
	private List<Integer> getWrong(String[] macs){
		String patternMac = "^([0-9a-fA-F]{2})(([/\\s:][0-9a-fA-F]{2}){5})$";
		List<Integer> wrong = new ArrayList<>();
		for (int i = 0; i < macs.length; i++) {
			if(!Pattern.compile(patternMac).matcher(macs[i]).find()){
				wrong.add(i);
			} 
		}
		return wrong;
	}
	
	private List<Integer> getRepeat(String[] macs){
		List<Integer> repeat = new ArrayList<>();
		List<String> list = new ArrayList<>();
		for (int i = 0; i < macs.length; i++) {
			if (list.indexOf(macs[i]) != -1) {
				repeat.add(i);
			}else {
				list.add(macs[i]);
			}
		}
		return repeat;
	}
	
}

