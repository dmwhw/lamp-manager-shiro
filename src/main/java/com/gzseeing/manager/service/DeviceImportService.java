package com.gzseeing.manager.service;

import java.util.Map;
import java.util.List;
import com.gzseeing.manager.entity.DeviceImport;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 设备临时导入表 服务类
 * </p>
 *
 * @author Haowen
 * @since 2018-08-29
 */
public interface DeviceImportService extends IService<DeviceImport> {

	public Map<String, List<Integer>> check(String[] macs, String[] codes);
	
	/**
	 *  插入mac地址
	 * @author haowen
	 * @time 2018年11月2日下午5:18:40
	 * @Description  
	 * @param macs
	 * @param codes
	 * @return 返回 exitList 存在的id，错误的行 wrongCode  成功插入的deviceIds deviceIds
	 */
	public Map<String, List<Integer>> importMacs(String[] macs, String[] codes);
	
	
}
