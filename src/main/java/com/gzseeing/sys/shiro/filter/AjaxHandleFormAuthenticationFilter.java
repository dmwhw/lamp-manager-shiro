package com.gzseeing.sys.shiro.filter;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import com.gzseeing.utils.LogUtils;

/**
 * 用于处理ajax和网页的
 * 
 * @author haowen
 *
 */
public class AjaxHandleFormAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        LogUtils.info("this is ajax handle filter");
        if (this.isLoginRequest(request, response)) {
            if (this.isLoginSubmission(request, response)) {
                return this.executeLogin(request, response);
            } else {
                return true;
            }
        } else {
            HttpServletRequest req = ((HttpServletRequest)request);
            String preffix = req.getContextPath() + "/api";
            if (req.getRequestURI().startsWith(preffix)) {
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print("{\"msgCode\" : \"90004\",\"result\":false,\"data\":{} }");
                out.flush();
                out.close();
            } else {
                this.saveRequestAndRedirectToLogin(request, response);
            }
            return false;
        }

    }

}
