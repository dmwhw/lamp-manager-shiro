package com.gzseeing.manager.mapper;

import java.util.Map;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.gzseeing.manager.entity.DeviceImportLog;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Haowen
 * @since 2018-08-30
 */
public interface DeviceImportLogMapper extends BaseMapper<DeviceImportLog> {
	
	@Select("<script>"
			+ "select u.username, dg.file_name, dg.log, dg.status, dg.time "
			+ "from "
			+ "device_import_log dg, backend_user u "
			+ "where dg.backend_id = u.id "
			+ "<if test='username != null'>and u.username = #{username} </if>"
			+ "<if test='status != null'>and dg.status = #{status} </if>"
			+ "<if test='start != null'>and dg.time &gt;= #{start} </if>"
			+ "<if test='end != null'>and dg.time &lt;= #{end} </if>"
			+ "order by dg.id desc "
			+ "limit #{pageStart}, #{pageSize}"
			+ "</script>")
	public List<Map<String, Object>> getPageMap(@Param("pageStart")Integer pageStart, 
												@Param("pageSize")Integer pageSize, 
												@Param("username")String username, 
												@Param("status")String status, 
												@Param("start")Date start, 
												@Param("end")Date end);
	
	
	
	@Select("<script>"
			+ "select u.username ,"
			+ " dg.file_name, "
			+ "dg.log, "
			+ "dg.status,"
			+ " dg.time "
			+ "from "
			+ "device_import_log dg,"
			+ " backend_user u "
			+ "where 1=1  "
			+ "<if test='username != null'>and u.username = #{username} </if>"
			+ "<if test='status != null'>and dg.status = #{status} </if>"
			+ "<if test='start != null'>and dg.time &gt;= #{start} </if>"
			+ "<if test='end != null'>and dg.time &lt;= #{end} </if>"
			+ "</script>")
	public List<Map<String, Object>> getImportByPage(
												Page<Map<String, Object>> page,
												@Param("username")String username, 
												@Param("status")String status, 
												@Param("start")Date start, 
												@Param("end")Date end
												);
	

}
