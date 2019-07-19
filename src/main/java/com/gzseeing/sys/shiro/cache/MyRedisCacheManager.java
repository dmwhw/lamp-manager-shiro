package com.gzseeing.sys.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
 import com.gzseeing.sys.redis.RedisDao;
import com.gzseeing.sys.varriable.Dictionary;
import com.gzseeing.sys.varriable.SysConfig;
import org.springframework.cache.CacheManager;

/**
 * cacheManager的实现方式
 * @author haowen
 *
 */
public class MyRedisCacheManager implements org.apache.shiro.cache.CacheManager {
	
	 
	private RedisDao redisDao;

	private CacheManager cacheManager;
	@Override
	public <K, V> Cache<K, V> getCache(String paramString) throws CacheException {
		//return new MyRedisCache<K, V>(redisDao,Dictionary.WEBAPP_NAME+":"+ paramString);
		return new MyRedisCache2<K, V>(Dictionary.WEBAPP_NAME+":"+ paramString,cacheManager);

	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public RedisDao getRedisDao() {
		return redisDao;
	}

	public void setRedisDao(RedisDao redisDao) {
		this.redisDao = redisDao;
	}

}
