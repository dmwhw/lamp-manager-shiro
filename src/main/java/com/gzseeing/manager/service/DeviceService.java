package com.gzseeing.manager.service;

import com.gzseeing.manager.entity.Device;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 设备表 服务类
 * </p>
 *
 * @author Haowen
 * @since 2018-08-29
 */
public interface DeviceService extends IService<Device> {

	/**
	 * 导入陌生mac
	 * @author haowen
	 * @time 2018年11月14日下午4:06:41
	 * @Description  
	 * @param modelCode
	 * @param backendUserid
	 * @param macs
	 */
	void insertUnknownMacs(String modelCode, Integer backendUserid, String ...macs);

	 
}
 