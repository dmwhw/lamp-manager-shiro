package com.gzseeing.manager.service.impl;

import com.gzseeing.manager.entity.Device;
import com.gzseeing.manager.entity.DeviceImportLog;
import com.gzseeing.manager.entity.DeviceNo;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.weaver.AjAttribute.PrivilegedAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzseeing.manager.mapper.DeviceImportLogMapper;
import com.gzseeing.manager.mapper.DeviceMapper;
import com.gzseeing.manager.mapper.DeviceNoMapper;
import com.gzseeing.manager.service.DeviceService;
import com.gzseeing.manager.service.DeviceTestService;
import com.gzseeing.sys.model.exception.MsgException;
import com.gzseeing.utils.DateUtil;
import com.gzseeing.utils.PathUtils;
import com.haowen.SNNoGernerator;
import com.haowen.mqtt.utils.FileUtils;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

/**
 * <p>
 * 设备表 服务实现类
 * </p>
 *
 * @author Haowen
 * @since 2018-08-29
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {

	@Autowired
	private DeviceMapper deviceMapper;
	
	@Autowired
	private DeviceNoMapper deviceNoMapper;
	@Autowired
	private DeviceTestService deviceTestService;
	@Autowired
	private DeviceImportLogMapper deviceImportLogMapper;
	
	@Override
	public void insertUnknownMacs(String modelCode,Integer backendUserid,String...macs ){
		if (macs==null||macs.length==0){
			return ;
		}
		
		StringBuffer sb=new StringBuffer();
		DeviceNo arg0=new DeviceNo();
		DeviceNo selectOne = deviceNoMapper.selectOne(arg0.setDeviceCode(modelCode));
		if (selectOne==null){
			throw new MsgException(40001, "型号不正确！");
		}
		List<Integer> deviceIds=new ArrayList<>();
		for (String mac : macs) {
			Device device=new Device();
			device.setMac(mac);
			device.setDeviceModelId(selectOne.getId());
			device.setSnNo(SNNoGernerator.get16In36());
			device.setStatus(0);
			deviceMapper.insert(device);
			deviceIds.add(device.getId());
			System.out.println(deviceIds);
			sb.append("新增了MAC:").append(mac).append(" 型号为:").append(selectOne.getDeviceCode()).append(" ").append(selectOne.getDeviceName()).append("\r\n") ;
		}
		deviceTestService.addDeviceTestRecord(deviceIds);
		File logFile=new File(PathUtils.getTomcatPath(),"excel/log_"+DateUtil.dateFormat("yyyy-MM-dd_HH_mm_ss", System.currentTimeMillis())+".log");
		try {
			FileUtils.writeFileBytes(logFile, sb.toString().getBytes("UTF-8"));
		} catch ( Exception e) {
 			//e.printStackTrace();
		}
		DeviceImportLog importLog=new DeviceImportLog();
		importLog.setBackendId(backendUserid);
		importLog.setFileName(logFile.getPath());
		importLog.setLog("通过陌生Mac页导入");
		importLog.setStatus("陌生Mac导入");
		importLog.setTime(DateUtil.getNowDate());
		deviceImportLogMapper.insert(importLog);
	}
	
	
}
