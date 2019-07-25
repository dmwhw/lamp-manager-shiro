package com.gzseeing.sys.shiro.authorize.tool;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.stereotype.Component;

import com.gzseeing.utils.SpringContextUtils;

/**
 * 用于动态更改shiro中的不同角色的url权限。
 * 
 * @author haowen
 *
 */
@Component
public class FilterChainDefinitionsService {

    private ShiroFilterFactoryBean shiroFilterFactoryBean;

    // https://www.cnblogs.com/007sx/p/7381475.html
    public void reloadFilterChains() {
        shiroFilterFactoryBean = SpringContextUtils.getContext().getBean(ShiroFilterFactoryBean.class);
        // 查询数据
        synchronized (shiroFilterFactoryBean) { // 强制同步，控制线程安全
            AbstractShiroFilter shiroFilter = null;

            try {
                shiroFilter = (AbstractShiroFilter)shiroFilterFactoryBean.getObject();

                PathMatchingFilterChainResolver resolver =
                    (PathMatchingFilterChainResolver)shiroFilter.getFilterChainResolver();
                // 过滤管理器
                DefaultFilterChainManager manager = (DefaultFilterChainManager)resolver.getFilterChainManager();
                // 清除权限配置
                manager.getFilterChains().clear();
                shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();

                // 这里重新进行配置Map
                Map<String, String> filterChainDefinitionMap = loadFilterChainDefinitionMap();

                // 重新设置权限
                // permissFactory.setFilterChainDefinitions(ShiroPermissionFactoryBean.definition);// 传入配置中的filterchains
                shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
                Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
                // 重新生成过滤链
                if (!CollectionUtils.isEmpty(chains)) {
                    chains.forEach((url, definitionChains) -> {
                        manager.createChain(url, definitionChains.trim().replace(" ", ""));
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, String> loadFilterChainDefinitionMap() {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 注意过滤器配置顺序 不能颠倒
        // 配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了，登出后跳转配置的loginUrl
        filterChainDefinitionMap.put("/api/web/entry/logout", "logout");
        // 配置不会被拦截的链接 顺序判断,不含项目名
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/api/web/entry/login", "anon");
        filterChainDefinitionMap.put("/ui/**", "anon");
        filterChainDefinitionMap.put("/222", "anon");

        filterChainDefinitionMap.put("/js/**", "anon");
        // filterChainDefinitionMap.put("/api/**", "authc");
        filterChainDefinitionMap.put("/**", "authc");
        return filterChainDefinitionMap;
    }

}
