package com.gzseeing.manager.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceUserMapper {

    @Delete(
            " DELETE FROM device_user WHERE device_id= #{deviceId}"
    )
    int deleteDeviceUserByDeviceId(Integer deviceId);
}
