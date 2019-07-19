package com.gzseeing.manager.service;

import com.gzseeing.manager.entity.DeviceTestSubject;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产线测试项目表 服务类
 * </p>
 *
 * @author Haowen
 * @since 2018-11-02
 */
public interface DeviceTestSubjectService extends IService<DeviceTestSubject> {

    /**
     * 保存或者更新测试时间
     * @param dts
     */
    void saveOrUpdateBySubjectCodeAndDeviceId(DeviceTestSubject dts);

    /**
     * 通过deviceId获取测试结果
     * @param deviceIds
     * @return
     */
    Map<Integer, List<DeviceTestSubject>> getDeviceTestResultByDeviceIds(Integer[] deviceIds);
}
