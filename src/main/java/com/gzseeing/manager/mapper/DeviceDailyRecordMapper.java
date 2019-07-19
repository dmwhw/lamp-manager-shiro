package com.gzseeing.manager.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceDailyRecordMapper {

    @Delete("DELETE FROM device_daily_record WHERE device_id=#{deviceId}")
    int deleteByDeviceId(Integer deviceId);
}
