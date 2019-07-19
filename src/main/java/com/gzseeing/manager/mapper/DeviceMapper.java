package com.gzseeing.manager.mapper;

import com.gzseeing.manager.entity.Device;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 设备表 Mapper 接口
 * </p>
 *
 * @author Haowen
 * @since 2018-08-29
 */
public interface DeviceMapper extends BaseMapper<Device> {

	@Select("select * from device for update ")
	public void lockTable();
	
}
