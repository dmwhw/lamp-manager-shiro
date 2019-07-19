package com.gzseeing.sys.springsession;

import org.springframework.context.annotation.Configuration;

/**
 * session托管到redis
 *
 */
// 使用springboot-session处理，单位：秒；
// RedisFlushMode有两个参数：ON_SAVE（表示在response commit前刷新缓存），IMMEDIATE（表示只要有更新，就刷新缓存）
// @EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800, redisFlushMode = RedisFlushMode.ON_SAVE,
// redisNamespace = "myproject")
@Configuration
public class SpringSessionConfig {

}
