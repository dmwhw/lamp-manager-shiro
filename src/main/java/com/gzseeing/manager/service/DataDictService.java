package com.gzseeing.manager.service;

import com.gzseeing.manager.entity.DataDict;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author Haowen
 * @since 2018-11-02
 */
public interface DataDictService extends IService<DataDict> {



    public Map<String, List<DataDict>> getDataDictByCodes(String[] codes);



    List<DataDict> getScriptDataDict(String code, Map<String, Object> params);
}
