package com.gzseeing.manager.service;

import com.gzseeing.manager.entity.DeviceImportLog;
import com.gzseeing.sys.model.PageBean;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Haowen
 * @since 2018-08-30
 */
public interface DeviceImportLogService extends IService<DeviceImportLog> {

	/**
	 * 分页
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public PageBean<Map<String, Object>> getPage(Integer pageIndex, Integer pageSize, String username, String status, Date start, Date end);
	
	/**
	 * 添加导入记录
	 * @param fileName 保存的文件名
	 * @param backendId	后台登陆用户的id
	 * @param log		日志
	 * @param status	导入结果
	 */
	public void record(String fileName, Integer backendId, String log, String status);
	
}
