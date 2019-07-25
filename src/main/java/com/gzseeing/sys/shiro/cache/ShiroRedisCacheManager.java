package com.gzseeing.sys.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.cache.CacheManager;

import com.gzseeing.sys.varriable.Dictionary;

/**
 * shiro他自己的 cacheManager的实现方式
 * 
 * @author haowen
 *
 */
public class ShiroRedisCacheManager implements org.apache.shiro.cache.CacheManager {

    private CacheManager cacheManager;

    @Override
    public <K, V> Cache<K, V> getCache(String paramString) throws CacheException {
        // return new MyRedisCache<K, V>(redisDao,Dictionary.WEBAPP_NAME+":"+ paramString);
        return new ShiroRedisCache2<K, V>(Dictionary.WEBAPP_NAME + ":" + paramString, cacheManager);

    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

}
