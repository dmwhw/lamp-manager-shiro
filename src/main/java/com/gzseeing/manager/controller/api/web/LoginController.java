package com.gzseeing.manager.controller.api.web;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ConcurrentAccessException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzseeing.sys.model.R;
import com.gzseeing.sys.shiro.ShiroConfig;
import com.gzseeing.sys.shiro.authc.CustomizedToken;

@Controller
@RequestMapping("/api/web/entry")
public class LoginController {

    // @Autowired
    // private BackendUserService backendUserService;

    @PostMapping("/login")
    @ResponseBody
    public R ajaxLogin(String username, String password, HttpSession session) {
        // if ("smartgiant".equals(username)&&"smartgiant2018".equals(password)){
        // session.setAttribute("login", "aaa");
        // return R.ok();
        //
        // }
        // if ("zhoulin".equals(username)&&"zhoulin123".equals(password)){
        // session.setAttribute("login", "aaa");
        // return R.ok();
        //
        // }
        // if ("wuyueqian".equals(username)&&"wuyueqian123".equals(password)){
        // session.setAttribute("login", "aaa");
        // return R.ok();
        //
        // }
        // if ("whw".equals(username)&&"whw123".equals(password)){
        // session.setAttribute("login", "aaa");
        // return R.ok();
        //
        // }
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            return R.ok();
        }
        try {
            CustomizedToken token = new CustomizedToken(username, password, ShiroConfig.LOGIN_TYPE_BACKEND_USER);
            currentUser.login(token);
            if (currentUser.isAuthenticated()) {
                return R.ok();
            }
        } catch (ConcurrentAccessException e) {
            System.out.println(e);
            return R.fail("40001", "manytime");
        } catch (UnknownAccountException e) {
            System.out.println(e);
            return R.fail("40001", "unknow user");
        } catch (LockedAccountException e) {
            System.out.println(e);
            return R.fail("40001", "locked");
        } catch (Exception e) {
            System.out.println(e);
            return R.fail();
        }
        return R.fail();
    }

    @PostMapping(value = "login2", produces = "application/json;charset=UTF-8",
        consumes = "application/json;charset=UTF-8")
    @ResponseBody

    public R ajaxLogin2(String username, String password, HttpSession session) {
        if ("smartgiant".equals(username) && "smartgiant2018".equals(password)) {
            session.setAttribute("login", "aaa");
            return R.ok();

        }
        if ("zhoulin".equals(username) && "zhoulin123".equals(password)) {
            session.setAttribute("login", "aaa");
            return R.ok();

        }
        if ("wuyueqian".equals(username) && "wuyueqian123".equals(password)) {
            session.setAttribute("login", "aaa");
            return R.ok();

        }
        if ("whw".equals(username) && "whw123".equals(password)) {
            session.setAttribute("login", "aaa");
            return R.ok();

        }
        // Subject currentUser = SecurityUtils.getSubject();
        // if (currentUser.isAuthenticated()){
        // return R.ok();
        // }
        // try {
        // CustomizedToken token=new CustomizedToken(username, password, ShiroConfig.LOGIN_TYPE_BACKEND_USER);
        // currentUser.login(token);
        // if (currentUser.isAuthenticated()){
        // return R.ok();
        // }
        // } catch (ConcurrentAccessException e) {
        // System.out.println(e);
        // return R.fail("40001","manytime");
        // }catch (UnknownAccountException e) {
        // System.out.println(e);
        // return R.fail("40001","unknow user");
        // }catch (LockedAccountException e) {
        // System.out.println(e);
        // return R.fail("40001","locked");
        // }catch (Exception e) {
        // System.out.println(e);
        // return R.fail();
        // }
        return R.fail();
    }

    @RequestMapping("/logout")
    public String ajaxLogOut() {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            // return R.ok();
        }
        return "redirect:/login.html";
    }
}
