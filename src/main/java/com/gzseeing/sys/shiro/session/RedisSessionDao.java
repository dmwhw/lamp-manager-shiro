package com.gzseeing.sys.shiro.session;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;

import com.gzseeing.sys.redis.RedisDao;
import com.gzseeing.sys.shiro.ShiroConfig;
import com.gzseeing.sys.varriable.Dictionary;
import com.gzseeing.sys.varriable.SysConfig;
import com.gzseeing.utils.LogUtils;
import com.gzseeing.utils.RedisUtils;

/**
 * 对redis的操作
 * 
 * @author haowen
 *
 */
public class RedisSessionDao extends EnterpriseCacheSessionDAO {

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = super.doCreate(session);
        LogUtils.info("sessionId: " + sessionId);
        LogUtils.info("{}", session);
        RedisDao<String, Object> objectRedisDao = RedisUtils.getObjectRedisDao();
        objectRedisDao.saveValueInMap(Dictionary.APP_USER + ":" + sessionId, ShiroConfig.MAP_SHIRO_KEY, session,
            1L * SysConfig.CONFIG_APP_SESSION_TIMEOUT_INT);
        return sessionId;
    }

    @Override
    protected void doDelete(Session session) {
        RedisDao<String, Object> objectRedisDao = RedisUtils.getObjectRedisDao();
        objectRedisDao.removeKey(Dictionary.APP_USER + ":" + session.getId());
        super.doDelete(session);
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        RedisDao<String, Object> objectRedisDao = RedisUtils.getObjectRedisDao();
        Session session =
            objectRedisDao.getByMapKey(Dictionary.APP_USER + ":" + sessionId, ShiroConfig.MAP_SHIRO_KEY, Session.class);
        return session;
    }

    @Override
    protected void doUpdate(Session session) {
        // super.doUpdate(session);
        LogUtils.info("-----updating....-----");
        RedisDao<String, Object> objectRedisDao = RedisUtils.getObjectRedisDao();
        objectRedisDao.saveValueInMap(Dictionary.APP_USER + ":" + session.getId(), ShiroConfig.MAP_SHIRO_KEY, session,
            1L * SysConfig.CONFIG_APP_SESSION_TIMEOUT_INT);

    }

}
