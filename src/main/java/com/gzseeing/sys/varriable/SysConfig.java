package com.gzseeing.sys.varriable;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.io.support.PropertiesLoaderUtils;

public class SysConfig {
	public final static Map<String,String> SysParam=new ConcurrentHashMap<String, String>(16);

	private static boolean isDevMode=false; 
	static {
		 try {
			Properties properties = PropertiesLoaderUtils.loadAllProperties("application.properties");
			if ("product".equals(properties.getProperty("spring.profiles.active"))){
				isDevMode=false;
			}else{
				isDevMode=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean isDevMode(){
		return isDevMode;
	}
	

	/**
	 * 系统日志参数，
	 * 用于控制LogUtils的打印级别。
		FATAL>ERROR>WARN>INFO>DEBUG>TRACE>ALL
		DEBUG能查看所有SQL语句。
		INFO级别只能查看到 DML语句。
		默认为DEBUG
	 */
	public final static String CONFIG_LOGGER_LEVEL="logger_level";
 
	


	public final static Integer CONFIG_APP_SESSION_TIMEOUT_INT = 60;


	public static String getValue(String key) {
		return SysParam.get(key);
	}
	
	public static Integer getIntValue(String key) {
		return Integer.parseInt(SysParam.get(key));
	}
	
 
	
}
