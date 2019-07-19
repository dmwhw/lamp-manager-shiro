package com.gzseeing.manager.controller.web;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gzseeing.utils.PathUtils;

@Controller
public class IndexController {

    @GetMapping("/")
    public String toHomePage(Model model, HttpSession session) throws IOException {
        System.out.println(PathUtils.getTomcatPath());

        if (session.getAttribute("login") != null) {
            return "home";
        }
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "home";
        }

        return "login";

        // 如果没有登录就去login
        // return "login";
    }
}