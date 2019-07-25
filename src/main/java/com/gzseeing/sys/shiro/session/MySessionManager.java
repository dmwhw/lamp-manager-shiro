package com.gzseeing.sys.shiro.session;

import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import com.gzseeing.utils.Servlets;

/**
 * 自定义从请求中获取sessionId的方式。
 *
 * 考虑从获得到的头信息，头信息获取 session，再去获得session。
 * 
 * @author haowen
 *
 */
public class MySessionManager extends DefaultWebSessionManager {

    private static final String AUTHORIZATION = "Authorization";
    private static final String TOKEN = "token";

    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

    public MySessionManager() {
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        // String id = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
        String id = WebUtils.toHttp(request).getParameter(TOKEN);
        HttpServletRequest req = (HttpServletRequest)request;
        // 如果请求头中有 Authorization 则其值为sessionId
        if (!StringUtils.isEmpty(id) && !Servlets.isStaticFile(req.getRequestURI())) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        } else {
            // 否则按默认规则从cookie取sessionId
            return super.getSessionId(request, response);
        }
    }

    /**
     * @description: 获取session, 优化单次请求需要多次访问redis的问题
     * @author cheng
     * @dateTime 2018/4/25 14:43
     */
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        // 获取sessionId
        Serializable sessionId = super.getSessionId(sessionKey);

        ServletRequest request = null;
        // 在 Web下使用 shiro 时这个 sessionKey 是 WebSessionKey 类型的
        // 若是在web下使用，则获取request
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey)sessionKey).getServletRequest();
        }

        // 尝试从request中获取session
        if (request != null && sessionId != null) {
            Object sessionObj = request.getAttribute(sessionId.toString());
            if (sessionObj != null) {
                // LogUtils.info("从request获取到session:{}", sessionId);
                return (Session)sessionObj;
            }
        }

        // 若从request中获取session失败,则从redis中获取session,并把获取到的session存储到request中方便下次获取
        Session session = super.retrieveSession(sessionKey);
        if (request != null && sessionId != null) {
            // LogUtils.info("存储session到request中:{}", sessionId);
            request.setAttribute(sessionId.toString(), session);
        }

        return session;
    }

}