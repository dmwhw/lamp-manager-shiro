package com.gzseeing.manager.controller.api.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gzseeing.utils.GsonUtils;

@Controller
@RequestMapping("/api/web/firmfiles")
public class UploadFirmFileController {

    // @RequestMapping("/upload")
    @ResponseBody
    public String uploadFirmFile(HttpServletRequest request,
        @RequestParam(value = "versionId", required = false) Integer versionId,
        @RequestParam(value = "files", required = false) MultipartFile files[],
        @RequestParam(value = "type", required = false) Integer type[], // 只接收数字
        @RequestParam(value = "index", required = false) Integer index[], // 只接受数字
        @RequestParam(value = "name_", required = false) String name[], // 名称
        @RequestParam(value = "remark", required = false) String remark[] // 备注
    ) {
        System.out.println("type:" + GsonUtils.parseJSON(type));
        System.out.println("index:" + GsonUtils.parseJSON(index));

        System.out.println("files:" + files.length);
        for (MultipartFile f : files) {
            System.out.println("" + f.getOriginalFilename());
            System.out.println(f.getSize());
            try {
                f.transferTo(new File("e:/test.tmp"));
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    // @RequestMapping("/test2")
    @ResponseBody
    public String test2(HttpServletRequest request, String big) {
        System.out.println(big == null ? "null" : "" + "big--length" + big.length());
        return "";
    }

    // @RequestMapping("/test")
    // @ResponseBody
    public String uploadFirmFileTest(HttpServletRequest request,
        @RequestParam(value = "files", required = false) MultipartFile files[],
        @RequestParam(value = "type", required = false) ArrayList<String> type,
        @RequestParam(value = "index", required = false) ArrayList<String> index)
        throws IllegalStateException, IOException {
        System.out.println(files == null ? "null" : "getsize:" + files.length);
        // ,@RequestParam("type")List<String> type,@RequestParam("index")List<String> index,List<FirmFileInfo> info
        // System.out.println(info==null?"null":"getsize:"+GsonUtils.parseJSON(info));
        System.out.println(type == null ? "null" : "typegetsize:" + GsonUtils.parseJSON(type));
        System.out.println(index == null ? "null" : "indexgetsize:" + GsonUtils.parseJSON(index));

        // String tmpDir = System.getenv("tmp.dir");
        File file = new File("d:/", "test.tmp");
        System.out.println(file.getPath());
        // files.transferTo(file);
        // System.out.println(""+file.length()+file.exists());
        return "";
    }

}
