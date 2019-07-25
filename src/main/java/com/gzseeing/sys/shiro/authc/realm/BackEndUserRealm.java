package com.gzseeing.sys.shiro.authc.realm;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gzseeing.manager.entity.BackendUser;
import com.gzseeing.manager.service.BackendUserService;
import com.gzseeing.sys.shiro.ShiroConfig;
import com.gzseeing.utils.LogUtils;
import com.gzseeing.utils.RedisUtils;

/**
 * 处理认证和权限的 自带缓存功能
 * 
 * @author haowen
 *
 */
@Component
public class BackEndUserRealm extends AuthorizingRealm {

    private final static String WRONG_TIME = "wrong_time";
    private final static String WRONG_COUNT = "wrong_count";

    @Autowired
    private BackendUserService backendUserService;

    @Override
    @Resource(name = "hashedCredentialsMatcher")
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        super.setCredentialsMatcher(credentialsMatcher);
    }

    @Override
    public String getName() {
        return ShiroConfig.LOGIN_TYPE_BACKEND_USER;
    }

    /*  
     *  身份有何种权限的。
     *  这个是针对特定用户的 
     *  在这个位置给用户配置权限和角色。
     *  
     *  这个仅仅在做首次，权限验证时调用出现。之后会使用缓存，不再调用。
     *  
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        LogUtils.info("这个位置配置权限的，权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        LogUtils.info("doGetAuthorizationInfo()...{}" + principals);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole("role");
        authorizationInfo.addStringPermission("permission");
        Object next = principals.fromRealm(getName()).iterator().next();
        LogUtils.info("这个是在权限验证的时候出现的,这个是用户名{}", next);
        return authorizationInfo;
    }

    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        LogUtils.info("MyShiroRealm.doGetAuthenticationInfo()-->{}", this);

        // 获取用户的输入的账号.
        String username = (String)token.getPrincipal();
        // System.out.println(token.getCredentials());
        // 这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方
        BackendUser backendUser = null;
        if ((backendUser = backendUserService.getByUserName(username)) == null) {
            throw new UnknownAccountException();
        }
        if (!BackendUser.STATUS_ON.equals(backendUser.getStatus())) {
            throw new LockedAccountException();
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, // 用户名.getSubject里面的
            backendUser.getPassword(), // 数据库密码hash
            ByteSource.Util.bytes(backendUser.getSalt() + backendUser.getId()), // salt=salt+id
            getName() // realm name
        );
        return authenticationInfo;
    }

    /**
     * 查看是否错误超过6次。
     * 
     * @author haowen
     * @time 2018-1-22下午4:53:09
     * @Description
     * @param redisKey
     * @return
     */
    private boolean checkLoginTimes(String redisKey) {
        Map<String, Object> loginRecord =
            RedisUtils.getObjectRedisDao().getByMapKeys(redisKey, new String[] {WRONG_TIME, WRONG_COUNT}, 0);
        Long wrongTime = null;
        int wrongCount2 = 0;
        if (loginRecord == null || loginRecord.isEmpty()) {
            loginRecord = new HashMap<String, Object>();
            loginRecord.put(WRONG_TIME, System.currentTimeMillis());
            loginRecord.put(WRONG_COUNT, 0);
        }
        wrongTime = (Long)loginRecord.get(WRONG_TIME);
        wrongCount2 = (Integer)loginRecord.get(WRONG_COUNT);
        Long nowTime = System.currentTimeMillis();
        RedisUtils.getObjectRedisDao().putMap(redisKey, loginRecord, 30 * 60L);// 30分钟
        if ((nowTime - wrongTime < 15 * 60 * 1000L) && wrongCount2 >= 6) {// 15分钟以内不给错6次
            // 操作过于频繁
            return false;
        }
        return true;
    }

    /**
     * 修改了某个人的权限.假装是某个人，然后去清空他的授权缓存。
     * 
     * @author haowen
     * @time 2019年7月23日上午11:37:15
     * @Description
     */
    public void clearAuthz(String userName) {
        Subject subject = SecurityUtils.getSubject();
        SimplePrincipalCollection principals = new SimplePrincipalCollection(userName, getName());
        subject.runAs(principals);
        this.getAuthorizationCache().remove(subject.getPrincipals());
        subject.releaseRunAs();
    }

    public static void main(String[] args) {
        String result = new Md5Hash("1234", "abcd12341", ShiroConfig.MD5_TIMES).toString();
        System.out.println(result);
    }
}
