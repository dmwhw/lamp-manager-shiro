package com.gzseeing.manager.service;

import com.gzseeing.manager.entity.DeviceNo;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 设备型号表 服务类
 * </p>
 *
 * @author Haowen
 * @since 2018-10-08
 */
public interface DeviceNoService extends IService<DeviceNo> {

	List<DeviceNo> getAllList();

}
