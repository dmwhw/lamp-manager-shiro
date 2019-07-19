package com.gzseeing.sys.redis;

import java.lang.reflect.Method;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableCaching // spring CacheManager
public class RedisConfig extends CachingConfigurerSupport {

    // 默认存在redisTemplate<?,?>
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    /**
     * spring CacheManager
     * 
     * @author haowen
     * @time 2018年7月21日下午2:34:35
     * @Description
     * @param redisTemplate
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Bean
    public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
        // 设置缓存过期时间
        rcm.setDefaultExpiration(7 * 24 * 60 * 60);// 秒
        return rcm;
    }
    /// --------------------------------缓存的end--------------

    @Bean
    public RedisTemplate<String, String> stringTemplate(RedisConnectionFactory factory) {

        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
            new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    // @Bean
    // public RedisTemplate<String, Object> redisTemplateObject() throws Exception {
    // RedisTemplate<String, Object> redisTemplateObject = new RedisTemplate<String, Object>();
    // redisTemplateObject.setConnectionFactory(jedisConnectionFactory());
    // setSerializer(redisTemplateObject);
    // redisTemplateObject.afterPropertiesSet();
    // return redisTemplateObject;
    // }

    /**
     * 设置序列化方法
     */
    private void setSerializer(RedisTemplate<String, Object> template) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
            new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setKeySerializer(template.getStringSerializer());
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
    }

    // @Bean
    // public RedisConnectionFactory jedisConnectionFactory(){
    // JedisPoolConfig poolConfig=new JedisPoolConfig();
    // poolConfig.setMaxIdle(maxIdl);
    // poolConfig.setMinIdle(minIdl);
    // poolConfig.setTestOnBorrow(true);
    // poolConfig.setTestOnReturn(true);
    // poolConfig.setTestWhileIdle(true);
    // poolConfig.setNumTestsPerEvictionRun(10);
    // poolConfig.setTimeBetweenEvictionRunsMillis(60000);
    // JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
    // jedisConnectionFactory.setHostName(hostName);
    // if(!passWord.isEmpty()){
    // jedisConnectionFactory.setPassword(passWord);
    // }
    // jedisConnectionFactory.setPort(port);
    // jedisConnectionFactory.setDatabase(database);
    // return jedisConnectionFactory;
    // }
}