package com.gzseeing.manager.controller.api.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzseeing.manager.service.DeviceService;
import com.gzseeing.sys.model.R;
import com.gzseeing.utils.GsonUtils;
import com.gzseeing.utils.RedisUtils;

@Controller
@RequestMapping("/api/web/other-mac")
public class DeviceOtherMacController {

	
	public final static String UNKNOWN_MACS="lamp:UNKNOWN_MACS_MAP";

	@Autowired
	private DeviceService deviceService;
	
	 
	@RequestMapping("/list")
	@ResponseBody
	public R getOtherDeviceMacList(){
		Map<String, String> allFromMap = RedisUtils.getStringRedisDao().getAllFromMap(UNKNOWN_MACS);
		List<Map<String, Object>> list=new ArrayList<Map<String,Object> >();
		if (allFromMap!=null){
			Iterator<Entry<String, String>> iterator = allFromMap.entrySet().iterator();
			while(iterator.hasNext()){
				Entry<String, String> next = iterator.next();
				Map<String,Object> device=new HashMap<>();
				device.put("mac", next.getKey());
				Map<String, Object> info=GsonUtils.toMap(next.getValue());
				device.putAll(info);
				list.add(device);
			}
			
		}
		return R.ok().add("list", list);
	}
	
	@ResponseBody
	@RequestMapping("/import")
	public R importIntoTable(@RequestParam(value="macs[]",required=false)String [] macs,@RequestParam(value="deviceCode",required=false) String deviceCode){
		if (macs==null){
			return R.fail("40001","未选择！");
		}
		deviceService.insertUnknownMacs(deviceCode, null, macs);
		for (String mac : macs) {
			RedisUtils.getStringRedisDao().deleteByMapKey(UNKNOWN_MACS, mac);
		}
		return R.ok();
		
	}
	@ResponseBody
	@RequestMapping("/del")
	public R delMac( @RequestParam(value="macs[]",required=false) String [] macs){
		if (macs==null){
			return R.fail("40001","未选择！");
		} 
		for (String mac : macs) {
			RedisUtils.getStringRedisDao().deleteByMapKey(UNKNOWN_MACS, mac);
		}
		return R.ok();
		
	}
}
