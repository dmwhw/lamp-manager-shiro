package com.gzseeing.sys.shiro;

import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.gzseeing.sys.shiro.authc.CustomizedModularRealmAuthenticator;
import com.gzseeing.sys.shiro.authorize.tool.FilterChainDefinitionsService;
import com.gzseeing.sys.shiro.cache.ShiroRedisCacheManager;
import com.gzseeing.sys.shiro.filter.AjaxHandleFormAuthenticationFilter;
import com.gzseeing.sys.shiro.filter.CustomRolesAuthorizationFilter;
import com.gzseeing.sys.shiro.session.MySessionManager;
import com.gzseeing.utils.LogUtils;

@Configuration
@Lazy(true)
public class ShiroConfig {
    public final static String MAP_SHIRO_KEY = "MAP_SHIRO_KEY";

    public final static String LOGIN_TYPE_BACKEND_USER = "back_end_user";

    public final static String LOGIN_TYPE_LAMP = "lamp";

    public final static String LOGIN_TYPE_MOBILE = "mobile";

    public final static String SALT_SECRET = "ATYUJHGBNVFTF";

    /**
     * Md5散列次数
     */
    public final static Integer MD5_TIMES = 10;
    public final static String ALGORITHMNAME = "MD5";

    @Autowired
    private List<Realm> realmList;
    @Autowired
    private FilterChainDefinitionsService filterChainDefinitionsService;
    @Value("${spring.redis.shiro.host}")
    private String host;
    @Value("${spring.redis.shiro.port}")
    private int port;
    @Value("${spring.redis.shiro.timeout}")
    private int timeout;
    // @Value("${spring.redis.shiro.password}")
    private String password;

    // @Bean("cacheManager")
    // public RedisCacheManager cacheManager(RedisTemplate redisTemplate) {
    // RedisCacheManager rdm = new RedisCacheManager(redisTemplate);
    // Map<String, Long> expires = new HashMap<>();
    // expires.put("halfHour", 1800L);
    // expires.put("hour", 3600L);
    // expires.put("oneDay", 86400L);
    // // shiro cache keys 对缓存的配置
    // expires.put("authorizationCache", 1800L);
    // expires.put("authenticationCache", 1800L);
    // expires.put("activeSessionCache", 1800L);
    // rdm.setExpires(expires);
    // return rdm;
    // }
    // ---------------------------------------------------------------filter------------------------------------
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        LogUtils.info("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // -----------------设置安全器----------------------
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // -----------------设置拦截路径----------------------
        Map<String, String> filterChainDefinitionMap = filterChainDefinitionsService.loadFilterChainDefinitionMap();
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        // --------------------设置登录页面、成功页面、未授权页面--------------------------------
        // 配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl("/");// 登录页
        // 登录成功后要跳转的链接，直接通过ajax接口控制，不需要设置这个。
        // shiroFilterFactoryBean.setSuccessUrl("/");
        // 未授权界面;
        // shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        // --------------------------设置filter，定制化filter动作-----------------------------------
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put("authc", new AjaxHandleFormAuthenticationFilter());
        filters.put("roleOr", new CustomRolesAuthorizationFilter());
        return shiroFilterFactoryBean;
    }

    @Bean("mySecurityManager")
    public DefaultWebSecurityManager securityManager(CacheManager cacheManager, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager() {
            private final Logger log2 = LoggerFactory.getLogger(DefaultWebSecurityManager.class);

            @Override
            protected SubjectContext resolveSession(SubjectContext context) {
                if (context.resolveSession() != null) {
                    log2.debug("Context already contains a session.  Returning.");
                    return context;
                }
                try {

                    Session session = resolveContextSession(context);
                    if (session != null) {
                        context.setSession(session);
                    }
                } catch (InvalidSessionException e) {
                    // no print e exception
                    log2.debug(
                        "Resolved SubjectContext context session is invalid.  Ignoring and creating an anonymous "
                            + "(session-less) Subject instance.");
                }
                return context;
            }
        };
        /** 安全管理器，定义了认证授权的方式、session、realm的缓存 */

        // ---------------------------------实现支持多个realm---------------------------------------------------------------
        Authenticator customizedModularRealmAuthenticator = customizedModularRealmAuthenticator();
        LogUtils.info("这个是方法产生" + customizedModularRealmAuthenticator);
        securityManager.setAuthenticator(customizedModularRealmAuthenticator());
        if (realmList != null) {
            securityManager.setRealms(realmList);
        }
        // ------------------------------------自定义session管理 使用redis----------------------------------------------------
        // FIXME 这里启用 sessionManager，用的是redis的session。
        // securityManager.setSessionManager(sessionManager);
        // -----------------------------------------自定义缓存----------------------------------------------------------------
        // 自定义缓存实现 使用redis,认证和授权都会用的cache
        securityManager.setCacheManager(cacheManager);
        return securityManager;
    }

    @Bean
    public Authenticator customizedModularRealmAuthenticator() {
        CustomizedModularRealmAuthenticator customizedModularRealmAuthenticator =
            new CustomizedModularRealmAuthenticator();
        LogUtils.info("这个是bean" + customizedModularRealmAuthenticator);
        return customizedModularRealmAuthenticator;
    }

    /**
     * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了 ）
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(ALGORITHMNAME);// 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(MD5_TIMES);// 散列的次数，比如散列两次，相当于// md5(md5(""));
        return hashedCredentialsMatcher;
    }

    // 自定义sessionManager
    @Bean
    public SessionManager sessionManager(ShiroRedisCacheManager shiroRedisCacheManager,
        SessionDAO enterpriseCacheSessionDAO) {
        MySessionManager mySessionManager = new MySessionManager();
        mySessionManager.setCacheManager(shiroRedisCacheManager);
        mySessionManager.setSessionDAO(enterpriseCacheSessionDAO);
        return mySessionManager;
    }

    // @Bean
    // 避免404的url带去系统的sessionID
    public SimpleCookie simpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("shiro-jsessionID");
        simpleCookie.setMaxAge(SimpleCookie.DEFAULT_MAX_AGE);
        return simpleCookie;
    }

    /**
     * 
     * cacheManager 缓存 redis实现
     * <p>
     * 使用的是shiro-redis开源插件 cache使用redis
     * 
     * @return
     */
    @Bean
    public ShiroRedisCacheManager shiroRedisCacheManager(org.springframework.cache.CacheManager cacheManager) {
        ShiroRedisCacheManager myRedisCacheManager = new ShiroRedisCacheManager();
        // myRedisCacheManager.setRedisDao(objectRedisDao);
        myRedisCacheManager.setCacheManager(cacheManager);
        LogUtils.info("shiro用到的cacher{} ", cacheManager);
        return myRedisCacheManager;
    }

    @Bean
    public EnterpriseCacheSessionDAO enterpriseCacheSessionDAO(CacheManager shiroRedisCacheManager) {
        EnterpriseCacheSessionDAO ecsd = new EnterpriseCacheSessionDAO();
        ecsd.setCacheManager(shiroRedisCacheManager);
        ecsd.setActiveSessionsCacheName("activeSessionCache");
        return ecsd;
    }

    /**
     * session使用redis RedisSessionDAO shiro sessionDao层的实现 通过redis
     * <p>
     */
    // @Bean
    // public RedisSessionDao redisSessionDAO() {
    // RedisSessionDao redisSessionDAO = new RedisSessionDao();
    // return redisSessionDAO;
    // }

    /**
     * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;
     * 
     * @param authenticator
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor
        authorizationAttributeSourceAdvisor(@Qualifier("mySecurityManager") DefaultWebSecurityManager authenticator) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor =
            new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(authenticator);
        return authorizationAttributeSourceAdvisor;
    }

}