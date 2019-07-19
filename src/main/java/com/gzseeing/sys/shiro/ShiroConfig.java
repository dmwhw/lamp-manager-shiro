package com.gzseeing.sys.shiro;

import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import com.gzseeing.sys.shiro.cache.MyRedisCacheManager;
import com.gzseeing.sys.shiro.filter.AjaxHandleFormAuthenticationFilter;
import com.gzseeing.sys.shiro.session.MySessionManager;

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

    @Value("${spring.redis.shiro.host}")
    private String host;
    @Value("${spring.redis.shiro.port}")
    private int port;
    @Value("${spring.redis.shiro.timeout}")
    private int timeout;
    // @Value("${spring.redis.shiro.password}")
    private String password;

    // @Bean("cacheManager")
    public RedisCacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager rdm = new RedisCacheManager(redisTemplate);
        Map<String, Long> expires = new HashMap<>();
        expires.put("halfHour", 1800L);
        expires.put("hour", 3600L);
        expires.put("oneDay", 86400L);
        // shiro cache keys 对缓存的配置
        expires.put("authorizationCache", 1800L);
        expires.put("authenticationCache", 1800L);
        expires.put("activeSessionCache", 1800L);
        rdm.setExpires(expires);
        return rdm;
    }

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // -----------------设置安全器----------------------
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // -----------------设置拦截路径----------------------
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 注意过滤器配置顺序 不能颠倒
        // 配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
        filterChainDefinitionMap.put("/api/web/entry/logout", "logout");
        // 配置不会被拦截的链接 顺序判断,不含项目名
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/api/web/entry/login", "anon");
        filterChainDefinitionMap.put("/ui/**", "anon");
        filterChainDefinitionMap.put("/**html", "anon");
        filterChainDefinitionMap.put("/**js", "anon");
        filterChainDefinitionMap.put("/**css", "anon");

        filterChainDefinitionMap.put("/js/**", "anon");
        // filterChainDefinitionMap.put("/api/**", "authc");
        filterChainDefinitionMap.put("/**", "authc");
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
                    // Context couldn't resolve it directly, let's see if we can since we have direct access to
                    // the session manager:
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
        // 实现支持多个realm
        securityManager.setAuthenticator(customizedModularRealmAuthenticator());
        if (realmList != null) {
            securityManager.setRealms(realmList);
        }
        // 自定义session管理 使用redis
        // securityManager.setSessionManager(sessionManager);
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager);
        return securityManager;
    }

    @Bean
    public Authenticator customizedModularRealmAuthenticator() {
        return new CustomizedModularRealmAuthenticator();
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
    public SessionManager sessionManager(MyRedisCacheManager myRedisCacheManager, SessionDAO redisSessionDAO) {
        MySessionManager mySessionManager = new MySessionManager();
        mySessionManager.setCacheManager(myRedisCacheManager);
        mySessionManager.setSessionDAO(redisSessionDAO);
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
    public MyRedisCacheManager myRedisCacheManager(org.springframework.cache.CacheManager cacheManager) {
        MyRedisCacheManager myRedisCacheManager = new MyRedisCacheManager();
        // myRedisCacheManager.setRedisDao(objectRedisDao);
        myRedisCacheManager.setCacheManager(cacheManager);

        return myRedisCacheManager;
    }

    @Bean
    public EnterpriseCacheSessionDAO sessionDAO(CacheManager myRedisCacheManager) {
        EnterpriseCacheSessionDAO ecsd = new EnterpriseCacheSessionDAO();
        ecsd.setCacheManager(myRedisCacheManager);
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