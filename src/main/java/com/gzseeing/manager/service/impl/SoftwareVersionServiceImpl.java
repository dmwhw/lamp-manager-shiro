package com.gzseeing.manager.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gzseeing.manager.entity.SoftwareVersion;
import com.gzseeing.manager.mapper.SoftwareVersionMapper;
import com.gzseeing.manager.service.SoftwareVersionService;
import com.gzseeing.sys.model.PageBean;

/**
 * <p>
 * 设备固件版本登记 服务实现类
 * </p>
 *
 * @author Haowen
 * @since 2018-08-25
 */
@Service
@Transactional
public class SoftwareVersionServiceImpl implements SoftwareVersionService {

    @Autowired
    private SoftwareVersionMapper softwareVersionMapper;

    @Override
    public PageBean<SoftwareVersion> getSoftWareVersionByPage(String remark, String versionName, Integer mainVersion,
        Integer subVersion, Integer pageIndex, Integer pageSize) {
        Wrapper<SoftwareVersion> wrap = new EntityWrapper<SoftwareVersion>();
        // count
        Integer count = softwareVersionMapper.selectCount(wrap);
        PageBean<SoftwareVersion> pageBean = new PageBean<>(count, pageIndex, pageSize);
        // 设置分页参数
        RowBounds rowBounds = new RowBounds(pageBean.getPageStart(), pageBean.getPageSize());
        List<SoftwareVersion> result = softwareVersionMapper.selectPage(rowBounds, wrap.orderBy("id"));
        pageBean.setList(result);
        return pageBean;
    }

}
