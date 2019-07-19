package com.gzseeing.manager.service;

import com.gzseeing.manager.entity.AppVersion;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * App版本升级表 服务类
 * </p>
 *
 * @author Haowen
 * @since 2018-10-26
 */
public interface AppVersionService extends IService<AppVersion> {

	List<AppVersion> getAndroidAllList();

	List<AppVersion> getIOSAllList();

}
