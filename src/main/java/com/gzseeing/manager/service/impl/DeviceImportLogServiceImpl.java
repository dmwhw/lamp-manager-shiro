package com.gzseeing.manager.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.gzseeing.sys.model.PageBean;
import com.gzseeing.utils.StringUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.gzseeing.manager.entity.DeviceImportLog;
import com.gzseeing.manager.entity.DeviceTest;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gzseeing.manager.mapper.DeviceImportLogMapper;
import com.gzseeing.manager.service.DeviceImportLogService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Haowen
 * @since 2018-08-30
 */
@Transactional
@Service
public class DeviceImportLogServiceImpl extends ServiceImpl<DeviceImportLogMapper, DeviceImportLog> implements DeviceImportLogService {

	@Autowired
	private DeviceImportLogMapper deviceImportLogMapper;
	
	@Override
	public PageBean<Map<String, Object>> getPage(Integer pageIndex, Integer pageSize, String username, String status, Date start, Date end) {

		Page<Map<String, Object>> page=new Page<>(pageIndex,pageSize);
		page.setOrderByField(" dg.id ").setAsc(false);
		if (StringUtils.isNull( username) ){
			username=null;
		}else{
			username="%"+username+"%";
		}
		List<Map<String, Object>> result = deviceImportLogMapper.getImportByPage( page, username, status, start, end);
		PageBean<Map<String, Object>> pageBean =new PageBean<>(new Long(page.getTotal()).intValue(), pageIndex, pageSize);
		pageBean.setList(result);
		return pageBean;
	}

	@Override
	public void record(String fileName, Integer backendId, String log, String status) {
		DeviceImportLog deviceImportLog = new DeviceImportLog();
		deviceImportLog.setFileName(fileName);
		deviceImportLog.setBackendId(backendId);
		deviceImportLog.setLog(log);
		deviceImportLog.setStatus(status);
		deviceImportLog.setTime(new Date());
		deviceImportLogMapper.insert(deviceImportLog);
	}
	
}
