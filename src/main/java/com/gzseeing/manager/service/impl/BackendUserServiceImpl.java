package com.gzseeing.manager.service.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.stereotype.Service;

import com.gzseeing.manager.entity.BackendUser;
import com.gzseeing.manager.mapper.BackendUserMapper;
import com.gzseeing.manager.service.BackendUserService;
import com.gzseeing.sys.shiro.ShiroConfig;
import com.gzseeing.sys.shiro.authc.CustomizedToken;
import com.gzseeing.utils.DateUtil;
import com.gzseeing.utils.EncryptUtils;

/**
 * <p>
 * 后台管理人员 服务实现类
 * </p>
 *
 * @author Haowen
 * @since 2018-08-25
 */
@Service
public class BackendUserServiceImpl   implements BackendUserService {

	
	@Autowired
	private BackendUserMapper backendUserMapper;
	
	@Override
	public BackendUser getByUserName(String username) {
		BackendUser backendUser = new BackendUser();
		backendUser.setUsername(username);
		return backendUserMapper.selectOne(backendUser);
 	
	}

	@Override
	public void addUser(BackendUser backendUser){
		String salt = EncryptUtils.getSalt();
		backendUser.setCreateDate(DateUtil.getNowDate());
		backendUser.setIsRoot(0);
		backendUser.setStatus(1);
		backendUser.setSalt(salt);
		//backendUser.setPassword( backendUser.getPassword());
		backendUserMapper.insert(backendUser);
		Md5Hash md5Hash = new Md5Hash( backendUser.getPassword(),ShiroConfig.SALT_SECRET+ salt+backendUser.getId(), ShiroConfig.MD5_TIMES);
		backendUser.setPassword( md5Hash.toString());
		backendUserMapper.updateById(backendUser);
	
	}
	
}
