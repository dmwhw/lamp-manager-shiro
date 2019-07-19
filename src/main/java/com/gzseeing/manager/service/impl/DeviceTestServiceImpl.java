package com.gzseeing.manager.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.sound.midi.VoiceStatus;

import com.gzseeing.manager.mapper.DeviceDailyRecordMapper;
import com.gzseeing.manager.mapper.DeviceUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gzseeing.manager.entity.Device;
import com.gzseeing.manager.entity.DeviceTest;
import com.gzseeing.manager.mapper.DeviceMapper;
import com.gzseeing.manager.mapper.DeviceTestMapper;
import com.gzseeing.manager.service.DeviceTestService;
import com.gzseeing.sys.model.PageBean;
import com.gzseeing.utils.CSVUtils;
import com.gzseeing.utils.DateUtil;
import com.gzseeing.utils.PathUtils;
import com.gzseeing.utils.RedisUtils;
import com.gzseeing.utils.StringUtils;

/**
 * <p>
 * 产线测试表 服务实现类
 * </p>
 *
 * @author Haowen
 * @since 2018-11-02
 */
@Service
@Transactional
public class DeviceTestServiceImpl extends ServiceImpl<DeviceTestMapper, DeviceTest> implements DeviceTestService {

	
	@Autowired
	private DeviceTestMapper deviceTestMapper;
	
	@Autowired
	private DeviceMapper deviceMapper;
	@Autowired
	private DeviceUserMapper deviceUserMapper;
	@Autowired
	private DeviceDailyRecordMapper deviceDailyRecordMapper;

	@Override
	public Boolean findIsTest(String deviceUuid){
		Wrapper<Device> param=new EntityWrapper<>();
		param.eq(Device.DEVICE_UUID, deviceUuid);
		List<Device> selectList = deviceMapper.selectList(param);
		if (selectList==null||selectList.isEmpty()){
			return null;
		}
		Integer id = selectList.get(0).getId();
		Wrapper<DeviceTest> paramWrapper=new EntityWrapper<>();
		paramWrapper.eq(DeviceTest.DEVICE_ID, id);
		List<DeviceTest> deviceTestList = deviceTestMapper.selectList(paramWrapper);
		if (deviceTestList==null||deviceTestList.isEmpty()){
			return null;
		}
		return !new Integer(0).equals( deviceTestList.get(0).getStatus());
		
	}
	
	@Override
	public void addDeviceTestRecord(List<Integer> deviceIds){
		if(deviceIds==null){
			return;
		}
		for (Integer integer : deviceIds) {
			if (integer==null){
				continue;
			}
			DeviceTest deviceTest=new DeviceTest();
			deviceTest.setDeviceId(integer);
			deviceTest.setStatus(0);
			deviceTest.setDeviceNickName("小智台灯"+integer);
			baseMapper.insert(deviceTest);
			
		}
	}
	
	@Override
	public void updateDeviceTestName(Integer id ,String deviceNickName){
		Wrapper< DeviceTest > wrapper =new EntityWrapper<>();
		wrapper.eq("id", id);
		DeviceTest deviceTest = new DeviceTest();
		deviceTest.setDeviceNickName(deviceNickName);
		baseMapper.update( deviceTest,wrapper );
	}
	
	
	@Override
	public void changRemark(Integer id ,String remark){
		deviceTestMapper.updateRemarkById(id, remark);
	}
	@Override
	public PageBean<DeviceTest> getDeviceTestByPage(String deviceName,Integer isOnLine,Date testStartTime,Date testEndTime,String mac,String snno,String remark,Integer testStatus,Integer pageIndex,Integer pageSize){
		Page<DeviceTest> page=new Page<>(pageIndex,pageSize);
		if (StringUtils.isNull( deviceName) ){
			deviceName=null;
		}else{
			deviceName="%"+deviceName+"%";
		}
		List<DeviceTest> pageList = deviceTestMapper.getByListPage(page, deviceName, testStartTime, testEndTime, mac, snno, remark, testStatus);
		page.setRecords(pageList);
		PageBean<DeviceTest> pageBean =new PageBean<>(new Long(page.getTotal()).intValue(), pageIndex, pageSize);
		pageBean.setList(pageList);
		for (DeviceTest deviceTest : pageList) {
			deviceTest.setIsOnLine(0);
			if (StringUtils.isNull( deviceTest.getDeviceUuid())){
				continue;
			}
			String onLineRedisKey="lamp:online:"+deviceTest.getDeviceUuid();
			boolean exist = RedisUtils.getStringRedisDao().exist(onLineRedisKey);
			if (exist){
				deviceTest.setIsOnLine(1);
			}
		}
		return pageBean;
	}
	
	@Override
	public File getDeviceTestExportFile(String deviceName,Integer isOnLine,Date testStartTime,Date testEndTime,String mac,String snno,String remark,Integer testStatus ){
 		if (StringUtils.isNull( deviceName) ){
			deviceName=null;
		}else{
			deviceName="%"+deviceName+"%";
		}
		List<DeviceTest> pageList = deviceTestMapper.getByList( deviceName, testStartTime, testEndTime, mac, snno, remark, testStatus);
		File file=null;
		CSVUtils instance =null;
		try {
			file = new File(PathUtils.getTmpDir(),"deviceList"+DateUtil.dateFormat("_yyyyMMdd_HHmmss", System.currentTimeMillis())+".csv");
			instance = CSVUtils.getInstance(file.getPath(),"GBK");
			instance.writeHeader("设备名","序列号","mac地址","测试状态","上次测试时间");
			for (DeviceTest deviceTest : pageList) {
				String status = new Integer(1).equals( deviceTest.getStatus())?"已测试" :"未测试";
				String testtime=deviceTest.getTestDoneDate()==null?"":DateUtil.dateFormat("yyyyMMdd_HHmmss", deviceTest.getTestDoneDate());
				instance.writeRecord(
						deviceTest.getDeviceNickName(),
						deviceTest.getSnNo(),
						deviceTest.getMac(),
						status,
						testtime);

			}
			instance.flush();
		} catch (Exception e) {
 		}finally {
 			if (instance!=null){
 				instance.close();
 			}
			
		}
		return file;
	}

	@Override
	public void confirmDone(Integer[] ids) {
		for (Integer integer : ids) {
			DeviceTest paramT=new DeviceTest();
			paramT.setId(integer);
			paramT.setStatus(1);
			paramT.setTestDoneDate(DateUtil.getNowDate());
			deviceTestMapper.updateById(paramT);
			DeviceTest deviceTest = deviceTestMapper.selectById(integer);
			//清空daily数据
			deviceDailyRecordMapper.deleteByDeviceId(deviceTest.getDeviceId());
			//清空绑定关系
			deviceUserMapper.deleteDeviceUserByDeviceId(deviceTest.getDeviceId());


		}
	}
	
}
