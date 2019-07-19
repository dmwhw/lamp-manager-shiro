package com.gzseeing.sys.spring.listener;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.gzseeing.utils.LogUtils;
import com.gzseeing.utils.RedisUtils;
import com.gzseeing.utils.SpringContextUtils;

@Component
@Lazy(value=false)
public class SpringLoaderListener implements  ApplicationContextAware  {

	@Value("${spring.datasource.url}")
	private String jdbcUrl;
	/**
	 * 启动主入口
	 * @Description 
	 */
	@PostConstruct
	public void init(){
		LogUtils.info("************启动中...***************");
		intitLogUtils();//加载LogUtils
		LogUtils.info("数据库连接-->"+jdbcUrl);
		LogUtils.info("***************************");
		//SingleMqttListner.init();
		
	}
	
	/**
	 *log4j init
	 * @author haowen
	 * @time 2017-11-10下午4:39:53
	 * @Description  
	 */
	public void intitLogUtils(){
		//Log4j初始化
		LogUtils.init();
	}

	
	


	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		SpringContextUtils.init(arg0);;
		RedisUtils.init(arg0);

	}
	

}
