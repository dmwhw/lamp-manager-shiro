package com.gzseeing.manager.service.impl;

import com.gzseeing.manager.entity.AppVersion;
import com.gzseeing.manager.mapper.AppVersionMapper;
import com.gzseeing.manager.service.AppVersionService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 * App版本升级表 服务实现类
 * </p>
 *
 * @author Haowen
 * @since 2018-10-26
 */
@Service
public class AppVersionServiceImpl extends ServiceImpl<AppVersionMapper, AppVersion> implements AppVersionService {

	
	@Override
	public List<AppVersion> getAndroidAllList (){
		Wrapper<AppVersion> wrapper=new EntityWrapper<>();
		wrapper.eq("system", 1);
		return selectList(wrapper);
	}
	
	@Override
	public List<AppVersion> getIOSAllList (){
		Wrapper<AppVersion> wrapper=new EntityWrapper<>();
		wrapper.eq("system", 2);
		return selectList(wrapper);
	}
}
