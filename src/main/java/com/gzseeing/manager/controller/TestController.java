package com.gzseeing.manager.controller;

import javax.annotation.Resource;

import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzseeing.sys.model.AppData;
import com.gzseeing.sys.shiro.authorize.tool.FilterChainDefinitionsService;
import com.gzseeing.sys.springmvc.annotation.Appdata;
import com.gzseeing.utils.LogUtils;

@Controller("test_todel")
public class TestController {

    @Resource
    @Qualifier("shirFilter")
    private AbstractShiroFilter shirFilter;
    @Autowired
    private FilterChainDefinitionsService filterChainDefinitionsService;

    @RequestMapping("/222")
    @ResponseBody
    public String result() {
        LogUtils.info("doing in cotroller!!!***************** ////");
        filterChainDefinitionsService.reloadFilterChains();
        return "ddd";
    }

    @RequestMapping("/test")
    // @ResponseBody

    public String result2() {
        LogUtils.info("doing in cotroller!!!***************** test");
        return "test";
    }

    @RequestMapping("/test2")
    @ResponseBody
    public String result2d2(AppData appdata, @Appdata Integer a) {
        System.out.println("appData:---->" + appdata);
        System.out.println(a);
        return "null";
    }
}
