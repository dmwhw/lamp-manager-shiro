package com.gzseeing.manager.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.gzseeing.manager.entity.DeviceTest;

/**
 * <p>
 * 产线测试表 Mapper 接口
 * </p>
 *
 * @author Haowen
 * @since 2018-11-02
 */
/**
 * @author haowen
 *
 */
@Mapper
public interface DeviceTestMapper extends BaseMapper<DeviceTest> {

	
	
	@Select("<script>"
			+ "SELECT "
			+ "dt.id, "
			+ "device_id, "
			+ "last_test_date,"
			+ " test_done_date, "
			+ "dt.remark,"
			+ "d.device_uuid as deviceUuid,"
			+ "dt.`status`,"
			+ " dt.device_nick_name  as deviceNickName,"
			+ " d.sn_no   as snNo,"
			+ " mac   as mac "
			+ " FROM device_test dt,device d "
			+ "where dt.device_id=d.id "
			+ "<when  test='deviceName!= null'> and dt.device_nick_name like #{deviceName} </when>   "
			+ "<when test='mac!= null'> and d.device_nick_name = #{mac} </when>   "
			+ "<when test='snno!= null'> and d.sn_no = #{snno} </when>   "
			+ "<when test='remark!= null'> and d.remark like #{remark} </when>   "
			+ "<when test='testStatus!= null'> and dt.status = #{testStatus} </when>"
			+ "<when test ='testStartTime!=null and testEndTime!=null'> and test_done_date between #{testStartTime} and #{testEndTime} </when> "
			+ "   "
			+ "</script>")
	public List<DeviceTest> getByListPage(
			Pagination page ,
			@Param("deviceName") String deviceName,
			@Param("testStartTime") Date testStartTime,
			@Param("testEndTime") Date testEndTime,
			@Param("mac") String mac,
			@Param("snno") String snno,
			@Param("remark") String remark,
			@Param("testStatus") Integer testStatus
			);
	@Select("<script>"
			+ "SELECT "
			+ "dt.id, "
			+ "device_id, "
			+ "last_test_date,"
			+ " test_done_date, "
			+ "remark,"
			+ "d.device_uuid as deviceUuid,"
			+ "dt.`status`,"
			+ " dt.device_nick_name  as deviceNickName,"
			+ " d.sn_no   as snNo,"
			+ " mac   as mac "
			+ " FROM device_test dt,device d "
			+ "where dt.device_id=d.id "
			+ "<when  test='deviceName!= null'> and dt.device_nick_name like #{deviceName} </when>   "
			+ "<when test='mac!= null'> and d.device_nick_name = #{mac} </when>   "
			+ "<when test='snno!= null'> and d.sn_no = #{snno} </when>   "
			+ "<when test='remark!= null'> and d.remark like #{remark} </when>   "
			+ "<when test='testStatus!= null'> and dt.status = #{testStatus} </when>"
			+ "<when test ='testStartTime!=null and testEndTime!=null'> and test_done_date between #{testStartTime} and #{testEndTime} </when> "
			+ "   "
			+ "</script>")
	public List<DeviceTest> getByList(
			@Param("deviceName") String deviceName,
			@Param("testStartTime") Date testStartTime,
			@Param("testEndTime") Date testEndTime,
			@Param("mac") String mac,
			@Param("snno") String snno,
			@Param("remark") String remark,
			@Param("testStatus") Integer testStatus
			);
	@Update("update device_test dt set dt.remark=#{remark} where dt.id=#{id}")
	public int updateRemarkById(@Param("id")Integer id,@Param("remark") String remark);
}
