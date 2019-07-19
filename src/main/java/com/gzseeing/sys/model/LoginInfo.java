package com.gzseeing.sys.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.groovy.util.Finalizable;

import com.gzseeing.utils.DateUtil;

public class LoginInfo implements Serializable{

	
	public final static String LOGIN_INFO_KEY="LOGIN_INFO_KEY";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1862783847352641432L;

	
	private Long id; 
	
	/**
	 * 登录名
	 */
	private  String loginName;
	
	/**
	 * 登录时间
	 */
	private  Date loginTime;
	
	/**
	 * 其他数据
	 */
	public Map<String, Object> extraData=new ConcurrentHashMap<>();
	
	
	public final static LoginInfo init(Long id,String loginName){
		 LoginInfo loginInfo = new LoginInfo();
		 loginInfo.loginName=loginName;
		 loginInfo.id=id;
		 loginInfo.loginTime=DateUtil.getNowDate();
		 return loginInfo;
	}


	public String getLoginName() {
		return loginName;
	}


	public Date getLoginTime() {
		return loginTime;
	}


	public Long getId() {
		return id;
	}
	public Integer getIdInt() {
		return id==null?null:id.intValue();
	}
}
