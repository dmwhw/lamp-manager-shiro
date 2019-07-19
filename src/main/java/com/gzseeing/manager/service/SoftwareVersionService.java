package com.gzseeing.manager.service;

import com.gzseeing.manager.entity.SoftwareVersion;
import com.gzseeing.sys.model.PageBean;

/**
 * <p>
 * 设备固件版本登记 服务类
 * </p>
 *
 * @author Haowen
 * @since 2018-08-25
 */
public interface SoftwareVersionService {

    /**
     * 拿去softwareVersion
     * 
     * @author haowen
     * @time 2018年8月28日下午5:04:58
     * @Description
     * @param versionName
     * @param mainVersion
     * @param subVersion
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PageBean<SoftwareVersion> getSoftWareVersionByPage(String remark, String versionName, Integer mainVersion,
        Integer subVersion, Integer pageIndex, Integer pageSize);

}
