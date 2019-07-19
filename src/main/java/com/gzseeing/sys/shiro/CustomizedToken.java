package com.gzseeing.sys.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 用户名，密码，类型的载体
 * @author haowen
 *
 */
public class CustomizedToken extends UsernamePasswordToken {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2813768478553804788L;
	//登录类型，判断是普通用户登录，教师登录还是管理员登录
    private String loginType;

    public CustomizedToken(final String username, final String password,String loginType) {
        super(username,password);
        this.loginType = loginType;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
    
}