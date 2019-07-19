package com.gzseeing.manager.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.gzseeing.manager.entity.DeviceTest;
import com.gzseeing.sys.model.PageBean;

/**
 * <p>
 * 产线测试表 服务类
 * </p>
 *
 * @author Haowen
 * @since 2018-11-02
 */
public interface DeviceTestService extends IService<DeviceTest> {

	/**
	 *  插入deviceTest记录
	 * @author haowen
	 * @time 2018年11月2日下午5:25:44
	 * @Description  
	 * @param deviceIds
	 */
	void addDeviceTestRecord(List<Integer> deviceIds);

	/**
	 * 分页
	 * @author haowen
	 * @time 2018年11月4日下午11:41:17
	 * @Description  
	 * @param deviceName
	 * @param isOnLine
	 * @param testStartTime
	 * @param testEndTime
	 * @param mac
	 * @param snno
	 * @param remark
	 * @param testStatus 测试状态
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	PageBean<DeviceTest> getDeviceTestByPage(String deviceName, Integer isOnLine, Date testStartTime, Date testEndTime,
			String mac, String snno, String remark, Integer testStatus, Integer pageIndex, Integer pageSize);

	/**
	 * 通过id更新
	 * @author haowen
	 * @time 2018年11月9日上午11:07:12
	 * @Description  
	 * @param id
	 * @param deviceNickName
	 */
	void updateDeviceTestName(Integer id, String deviceNickName);

	/**
	 *导出excel、
	 * @author haowen
	 * @time 2018年11月9日上午11:31:22
	 * @Description  
	 * @param deviceName
	 * @param isOnLine
	 * @param testStartTime
	 * @param testEndTime
	 * @param mac
	 * @param snno
	 * @param remark
	 * @param testStatus
	 * @return
	 */
	File getDeviceTestExportFile(String deviceName, Integer isOnLine, Date testStartTime, Date testEndTime, String mac,
			String snno, String remark, Integer testStatus);

	/**
	 * 修改备注
	 * @author haowen
	 * @time 2018年11月9日下午2:27:42
	 * @Description  
	 * @param id
	 * @param remark
	 */
	void changRemark(Integer id, String remark);

	void confirmDone(Integer[] ids);

	/**
	 * 查询是否已经测试。null表示不存在该设备，一般不给测试。
	 * @author haowen
	 * @time 2018年11月19日上午11:42:33
	 * @Description  
	 * @param deviceUuid
	 * @return
	 */
	Boolean findIsTest(String deviceUuid);

}
