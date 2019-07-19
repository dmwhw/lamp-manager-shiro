package com.gzseeing.manager.service.impl;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import com.gzseeing.manager.entity.Device;
import com.gzseeing.manager.entity.DeviceNo;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gzseeing.manager.entity.DeviceImport;
import com.gzseeing.manager.mapper.DeviceMapper;
import com.gzseeing.manager.mapper.DeviceNoMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gzseeing.manager.mapper.DeviceImportMapper;
import com.gzseeing.manager.service.DeviceImportService;
import com.haowen.SNNoGernerator;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 设备临时导入表 服务实现类
 * </p>
 *
 * @author Haowen
 * @since 2018-08-29
 */
@Service
@Transactional
public class DeviceImportServiceImpl extends ServiceImpl<DeviceImportMapper, DeviceImport> implements DeviceImportService {

	@Autowired
	private DeviceMapper deviceMapper;
	@Autowired
	private DeviceNoMapper deviceNoMapper;
	
	
	private static Boolean lock = true;
	
	@Override
	public Map<String, List<Integer>> check(String[] macs, String[] codes) {
		List<DeviceNo> DeviceNos = deviceNoMapper.selectList(null);
		List<Integer> exitList = new ArrayList<>();
		List<Integer> wrongCode = new ArrayList<>();
		for (int i = 0; i < macs.length; i++) {
			Wrapper<Device> wrapper = new EntityWrapper<Device>();
			wrapper.eq("mac", macs[i]);
			Integer count = deviceMapper.selectCount(wrapper);
			if (count > 0) {
				exitList.add(i);
			}
		}
		codes : for (int i = 0; i < codes.length; i++) {
			for (DeviceNo deviceNo : DeviceNos) {
				String deviceCode = deviceNo.getDeviceCode();
				if (deviceCode.equals(codes[i])) {
					continue codes;
				}
			}
			wrongCode.add(i);
		}
		Map<String, List<Integer>> map = new HashMap<>();
		map.put("exitList", exitList);
		map.put("wrongCode", wrongCode);
		return map;
	}

	@Override
	public Map<String, List<Integer>> importMacs(String[] macs, String[] codes) {
		if (lock) {
			synchronized (lock) {
				if (lock) {
					lock = false;
				}else {
					return null;
				}
			}
		}else {
			return null;
		}
		Map<String, List<Integer>> check = check(macs, codes);
		List<Integer> exitList = check.get("exitList");
		List<Integer> wrongCode = check.get("wrongCode");
		List<Integer> insertDeviceId=new ArrayList<Integer>();
		check.put("deviceIds", insertDeviceId);
		if (exitList.size() == 0 && wrongCode.size() == 0) {
			List<DeviceNo> DeviceNos = deviceNoMapper.selectList(null);
			for (int i = 0; i < macs.length; i++) {
				Device device = new Device();
				for (DeviceNo deviceNo : DeviceNos) {
					if (deviceNo.getDeviceCode().equals(codes[i])) {
						device.setDeviceModelId(deviceNo.getId());
					}
				}
				device.setMac(macs[i]);
				device.setStatus(0);
				
				device.setSnNo(SNNoGernerator.get16In36());
				deviceMapper.insert(device);
				insertDeviceId.add(device.getId());
			}
		}
		lock = true;
		return check;
	}
}
