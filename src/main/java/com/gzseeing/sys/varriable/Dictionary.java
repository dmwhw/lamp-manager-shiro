package com.gzseeing.sys.varriable;



public class Dictionary {

	public final static String WEBAPP_NAME="lampManager";
	
	public final static String WEB_VERIFICATION_CODE = "web_verification_code";
	public final static String WEB_USER = "lamp:web_user";
	public final static String WEB_PERMISSION_URL="web_permission_url";
	public final static String MQTT_USER="lamp:mqtt";

	/**
	 * 网页重定向的
	 */
	public static final String WEB_REDIRECT_URL="web_redirect_url";

	/**
	 * 账号正则表达式
	 */
	public static final String WEB_REGEX_ACCOUNT = "^[a-zA-Z0-9]{5,13}$";
	
	/**
	 * 密码正则表达式   密码的正则表达式 6到16位数 数字， 大写英文，小写英文， 3选2
	 */
	//public final static String WEB_REGEX_PASSWORD = "^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)([0-9a-zA-Z]{6,16})$";
	
	/**
	 * 密码正则表达式2   允许字母，数字
	 */
	public final static String WEB_REGEX_PASSWORD = "^([0-9a-zA-Z]{6,16})$";
	
	
	/**
	 * 手机号正则表达式
	 */
	public final static String WEB_REGEX_MOBILE = "^1(3|4|5|6|7|8|9)[0-9]\\d{8}$";
	
	
	
	public final static String APP_USER = "lamp:app_user";

	public final static String REMOTE_LOGIN_FLAG="remote_login_flag";
			
			
	
	public final static String APP_PERMISSION_URL="app_permission_url";
	
	
	public static final String APP_VERIFICATION_CODE = "app_verification_code";
	

	
}
