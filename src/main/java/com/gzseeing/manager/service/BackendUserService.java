package com.gzseeing.manager.service;

import com.gzseeing.manager.entity.BackendUser;

/**
 * <p>
 * 后台管理人员 服务类
 * </p>
 *
 * @author Haowen
 * @since 2018-08-25
 */
public interface BackendUserService  {

 
	BackendUser getByUserName(String username);

	/**
	 * 只需要输入手机和密码
	 * @author haowen
	 * @time 2018年10月13日下午3:05:44
	 * @Description  
	 * @param backendUser
	 */
	void addUser(BackendUser backendUser);

}
