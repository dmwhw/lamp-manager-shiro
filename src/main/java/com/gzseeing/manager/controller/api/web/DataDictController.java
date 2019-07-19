package com.gzseeing.manager.controller.api.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzseeing.sys.model.R;

@Controller
@RequestMapping("api/web/data-dict")

public class DataDictController {

    @RequestMapping("/get-by-code")
    @ResponseBody
    public R getDataDictByCode(@RequestParam(required = false, value = "code[]") String codes,
        @RequestParam(required = false, value = "params") String params,

        @RequestParam(required = false, value = "code") String code) {

        if (codes != null) {

        }

        return R.ok();
    }

}
