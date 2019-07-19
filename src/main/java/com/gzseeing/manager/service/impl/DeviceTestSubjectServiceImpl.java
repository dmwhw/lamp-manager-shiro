package com.gzseeing.manager.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gzseeing.manager.entity.DeviceTestSubject;
import com.gzseeing.manager.mapper.DeviceTestSubjectMapper;
import com.gzseeing.manager.service.DeviceTestSubjectService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 产线测试项目表 服务实现类
 * </p>
 *
 * @author Haowen
 * @since 2018-11-02
 */
@Service
public class DeviceTestSubjectServiceImpl extends ServiceImpl<DeviceTestSubjectMapper, DeviceTestSubject> implements DeviceTestSubjectService {



    @Override
    public void saveOrUpdateBySubjectCodeAndDeviceId(DeviceTestSubject dts){

        DeviceTestSubject t=new DeviceTestSubject();
        t.setDeviceId(dts.getDeviceId());
        t.setSubjectCode(dts.getSubjectCode());
        DeviceTestSubject exists=baseMapper.selectOne(t);
        if (exists!=null){
            exists.setRemark(dts.getRemark());
            exists.setStatus(dts.getStatus());
            exists.setLastTestTime(dts.getLastTestTime());
            exists.setStatus(dts.getStatus());
            baseMapper.updateById(exists);
        }else{
            baseMapper.insert(dts);
        }

    }


    @Override
    public Map<Integer, List<DeviceTestSubject>> getDeviceTestResultByDeviceIds(Integer[] deviceIds){
        Map<Integer, List<DeviceTestSubject>> map=new HashMap<>();
        Wrapper<DeviceTestSubject> condition=new EntityWrapper<>();
        List<Integer> ids=new ArrayList<>();
        Collections.addAll(ids,deviceIds);
        condition.in(DeviceTestSubject.DEVICE_ID, ids).orderBy(DeviceTestSubject.DEVICE_ID);
        List<DeviceTestSubject> deviceTestSubjects = baseMapper.selectList(condition);
        Integer nowId=null;
        for(DeviceTestSubject dts:deviceTestSubjects){
            if (dts.getDeviceId()!=nowId){
                nowId=dts.getDeviceId();
                List<DeviceTestSubject> list=new ArrayList<>();
                map.put(nowId,list);
             }
            map.get(dts.getDeviceId()).add(dts);
        }
        return map;
    }

}
