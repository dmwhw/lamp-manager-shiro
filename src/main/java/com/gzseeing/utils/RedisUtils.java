package com.gzseeing.utils;

import org.springframework.context.ApplicationContext;

import com.gzseeing.sys.redis.RedisDao;
import com.gzseeing.sys.redis.impl.ObjectRedisDaoImpl;
import com.gzseeing.sys.redis.impl.StringRedisDaoImpl;

public class RedisUtils {
	
	private  static  ApplicationContext act;
	public  static  void init(ApplicationContext act2){
		act=act2;
	}
	public static ApplicationContext getContext() {
		return act;
	}

	
	/**
	 * 获得 对象缓存
	 * @author haowen
	 * @time 2018年1月26日上午9:16:32
	 * @Description 
	 * @return
	 */
	public static RedisDao<String,String> getStringRedisDao(){
		return  act.getBean( StringRedisDaoImpl.class);

		
	}
	/**
	 * 获得字符串缓存
	 * @author haowen
	 * @time 2018年1月26日上午9:16:34
	 * @Description 
	 * @return
	 */
	public static RedisDao<String, Object> getObjectRedisDao(){
		return act.getBean( ObjectRedisDaoImpl.class);
	}
}
