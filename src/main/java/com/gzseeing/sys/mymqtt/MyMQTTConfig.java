package com.gzseeing.sys.mymqtt;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.gzseeing.sys.redis.RedisDao;
import com.gzseeing.sys.varriable.Dictionary;
import com.gzseeing.sys.varriable.SysConfig;
import com.gzseeing.utils.EncryptUtils;
import com.gzseeing.utils.ThreadUtils;
import com.haowen.mqtt.comp.MyMQTTClient;

@Configuration
public class MyMQTTConfig {

	
	
	@Autowired
	private MyMQTTClient myMQTTClient;
	@Value("${haowen.mqtt.starter.mqtt-user}")
	private String userName;
	@Autowired
	private RedisDao<String, String> stringRedisDao; 
	
	
	@PostConstruct
	public void setpassword(){
		String pasword=EncryptUtils.getSalt();
		Long timeOut=null;
		Map<String,String> map=new HashMap<String,String>();
		map.put("pw",EncryptUtils.createPBKDF2WithHmacSHA256Password(pasword)  );
		map.put("super", ""+1 );
		stringRedisDao.putMap(Dictionary.MQTT_USER+":"+userName, map, timeOut);
		ThreadUtils.sleepOneSec();
		if(!SysConfig.isDevMode()){
			myMQTTClient.setMqttpw(pasword);
		}else{
			myMQTTClient.setMqttpw("123456a");

		}

		if (!SysConfig.isDevMode()){
			myMQTTClient.connect();
			
		}

	}
	
	
}
