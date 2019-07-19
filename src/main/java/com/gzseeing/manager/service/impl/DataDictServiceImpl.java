package com.gzseeing.manager.service.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gzseeing.manager.entity.DataDict;
import com.gzseeing.manager.mapper.DataDictMapper;
import com.gzseeing.manager.service.DataDictService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author Haowen
 * @since 2018-11-02
 */
@Service
public class DataDictServiceImpl extends ServiceImpl<DataDictMapper, DataDict> implements DataDictService {


    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public Map<String, List<DataDict>> getDataDictByCodes(String[] codes) {
        Map<String,List< DataDict>> map=new HashMap<>();
        for (String code:codes){
           Wrapper<DataDict> tWrapper =new EntityWrapper<>();
           tWrapper.eq(DataDict.CODE,code).orderBy(DataDict.SORT);
           List<DataDict> dataDicts = baseMapper.selectList(tWrapper);
           for(DataDict d: dataDicts){
               List<DataDict> list=null;
               if ((list=map.get(code))==null){
                   list=new ArrayList<>();
                   map.put(code,list);
               }
               if (new Integer(0).equals( d.getType())){
                 list.add(d);
               }else if (new Integer(1).equals(d.getType())){

               }
           }
       }
       return map;
    }


    @Override
    public List<DataDict> getScriptDataDict(String code, Map<String, Object> params){
        List<DataDict> list= new ArrayList<>();
        Wrapper<DataDict> tWrapper =new EntityWrapper<>();
        tWrapper.eq(DataDict.CODE,code).orderBy(DataDict.SORT);
        List<DataDict> dataDicts = baseMapper.selectList(tWrapper);
        if (dataDicts==null||dataDicts.size()==0){
            return list;
        }
        for(DataDict d: dataDicts) {
            if (!new Integer(0).equals(d.getType())){
                continue;
            }
            String nameColumn= d.getName()==null?"name":d.getName();
            String valueColumn=d.getValue()==null?"value":d.getValue();
            SqlSession sqlSession = sqlSessionFactory.openSession();
            List<Map> results = sqlSession.selectList(d.getScript(), params);
            
        }
        return list;
    }

}
