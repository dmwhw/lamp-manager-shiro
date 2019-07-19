package com.gzseeing.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gzseeing.manager.entity.DeviceNo;
import com.gzseeing.manager.mapper.DeviceNoMapper;
import com.gzseeing.manager.service.DeviceNoService;

/**
 * <p>
 * 设备型号表 服务实现类
 * </p>
 *
 * @author Haowen
 * @since 2018-10-08
 */
@Service
public class DeviceNoServiceImpl extends ServiceImpl<DeviceNoMapper, DeviceNo> implements DeviceNoService {

	
	
	@Autowired
	private DeviceNoMapper deviceNoMapper;
	
	
	
	@Override
	public List<DeviceNo> getAllList(){
		return  deviceNoMapper.selectList(null);
	}
	
}
