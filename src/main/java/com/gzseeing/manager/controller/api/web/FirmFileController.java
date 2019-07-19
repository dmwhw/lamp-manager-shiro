package com.gzseeing.manager.controller.api.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gzseeing.manager.entity.FirmwareFile;
import com.gzseeing.manager.service.FirmwareFileService;
import com.gzseeing.manager.service.SoftwareVersionService;
import com.gzseeing.sys.model.LoginInfo;
import com.gzseeing.sys.model.PageBean;
import com.gzseeing.sys.model.R;
import com.gzseeing.sys.model.exception.MsgException;
import com.gzseeing.utils.DataUtils;
import com.gzseeing.utils.DateUtil;
import com.gzseeing.utils.FileUtils;
import com.gzseeing.utils.LogUtils;
import com.gzseeing.utils.StringUtils;

@Controller
@RequestMapping("/api/web/firm-file")
public class FirmFileController {

    @Autowired
    private FirmwareFileService firmwareFileService;
    @Autowired
    private SoftwareVersionService softwareVersionService;
    @Value("${myconfig.firmfile.upload.path}")
    private String uploadFilePath;

    /**
     * 拿pageBean
     * 
     * @author haowen
     * @time 2018年8月28日下午3:39:35
     * @Description
     * @param versionId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/pagebean")
    public R ajaxGetFirmFilePageBean(@RequestParam(value = "versionId", required = true) Integer versionId,
        Integer pageIndex, Integer pageSize) {
        PageBean<FirmwareFile> pagebean =
            firmwareFileService.getFirmWareFileListByVersionIdByPage(versionId, pageIndex, pageSize);
        R r = R.ok().add("pageBean", pagebean);
        r.put("rows", pagebean.getList());
        r.put("total", pagebean.getTotalCount());
        return r;
    }

    /**
     * 上传一个固件
     * 
     * @author haowen
     * @time 2018年9月19日下午2:24:51
     * @Description
     * @param versionId
     *            版本id
     * @param file
     * @param type
     *            16进制字符串
     * @param index
     *            16进制字符串
     * @param flashAddr
     *            16进制字符串
     * @param name
     *            名称
     * @param remark
     *            备注
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public R uploadFirmFile(@RequestParam(value = "versionId", required = false) Integer versionId,
        @RequestParam(value = "file", required = false) MultipartFile file,
        @RequestParam(value = "fileType", required = false) String type, // 只接收数字
        @RequestParam(value = "fileIndex", required = false) String index, // 只接受数字
        @RequestParam(value = "flashAddr", required = false) String flashAddr, // 只接受数字
        @RequestParam(value = "name_", required = false) String name, // 名称
        @RequestParam(value = "remark", required = false) String remark, // 备注
        HttpSession session) {
        LoginInfo logininfo = (LoginInfo)session.getAttribute(LoginInfo.LOGIN_INFO_KEY);

        if (versionId == null) {
            return R.fail("40000", "版本号为空");
        }
        if (file == null || file.getSize() == 0) {
            return R.fail("42010", "文件无效");
        }
        if (!StringUtils.isHexString(type) || type.length() > 2) {
            return R.fail("42011", "fileType无效");
        }
        if (!StringUtils.isHexString(index) || index.length() > 5) {
            return R.fail("42012", "fileIndex无效");
        }
        if (StringUtils.isNull(name) || name.length() > 11) {
            return R.fail("42013", "name无效");
        }
        if (!StringUtils.isHexString(flashAddr) || flashAddr.length() > 6) {
            return R.fail("42014", "flashAddr无效");
        }

        // 封装
        FirmwareFile firmwareFile = new FirmwareFile();
        firmwareFile.setFirmwareFileIndex(Integer.parseInt(index, 16));
        firmwareFile.setFirmwareFileType(Integer.parseInt(type, 16));
        firmwareFile.setFlashAddr(Integer.parseInt(flashAddr, 16));
        firmwareFile.setName(name);
        firmwareFile.setUploadDate(DateUtil.getNowDate());
        // firmwareFile.setUserId(logininfo.getIdInt());
        firmwareFile.setVersionId(versionId);
        try {
            firmwareFileService.addOneFirmFile(firmwareFile, file);
        } catch (MsgException e) {
            return R.fail(e.getMsgCode(), e.getMsg());
        } catch (Exception e) {
            LogUtils.error("操作失败{}", e.getMessage());
            return R.fail();
        }
        return R.ok();
    }

    @RequestMapping("/edit")
    @ResponseBody
    public R editFirmFile(@RequestParam(value = "firmFileId", required = false) Integer firmFileId,
        @RequestParam(value = "versionId", required = false) Integer versionId,
        @RequestParam(value = "file", required = false) MultipartFile file,
        @RequestParam(value = "fileType", required = false) String type, // 只接收数字
        @RequestParam(value = "fileIndex", required = false) String index, // 只接受数字
        @RequestParam(value = "flashAddr", required = false) String flashAddr, // 只接受数字
        @RequestParam(value = "name_", required = false) String name, // 名称
        @RequestParam(value = "remark", required = false) String remark, // 备注
        HttpSession session) {
        LoginInfo logininfo = (LoginInfo)session.getAttribute(LoginInfo.LOGIN_INFO_KEY);
        if (versionId == null) {
            return R.fail("40000", "版本号为空");
        }
        if (file != null && !StringUtils.isNull(file.getOriginalFilename()) && file.getSize() == 0) {
            return R.fail("42010", "文件无效");
        }
        if (!StringUtils.isHexString(type) || type.length() > 2) {
            return R.fail("42011", "fileType无效");
        }
        if (!StringUtils.isHexString(index) || index.length() > 5) {
            return R.fail("42012", "fileIndex无效");
        }
        if (StringUtils.isNull(name) || name.length() > 11) {
            return R.fail("42013", "name无效");
        }
        if (!StringUtils.isHexString(flashAddr) || flashAddr.length() > 6) {
            return R.fail("42014", "flashAddr无效");
        }
        if (firmFileId == null) {
            return R.fail("40001", "操作无效");
        }

        // 封装
        FirmwareFile firmwareFile = new FirmwareFile();
        firmwareFile.setId(firmFileId);
        firmwareFile.setFirmwareFileIndex(Integer.parseInt(index, 16));
        firmwareFile.setFirmwareFileType(Integer.parseInt(type, 16));
        firmwareFile.setFlashAddr(Integer.parseInt(flashAddr, 16));
        firmwareFile.setName(name);
        // firmwareFile.setUserId(logininfo.getIdInt());
        firmwareFile.setVersionId(versionId);
        try {
            firmwareFileService.updateOneFirmFile(firmwareFile, file);
        } catch (MsgException e) {
            return R.fail(e.getMsgCode(), e.getMsg());
        } catch (Exception e) {
            LogUtils.error("操作失败{}", e.getMessage());
            return R.fail();
        }
        return R.ok();
    }

    /**
     * 删除firmFIle
     * 
     * @author haowen
     * @time 2018年9月17日上午10:00:59
     * @Description
     * @param firmFileId
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public R delFirmFile(Integer firmFileId) {
        boolean delFlag = firmwareFileService.delFirmFileById(firmFileId);
        return delFlag ? R.ok() : R.fail();
    }

    @ResponseBody
    @RequestMapping("/pagebean_tmp")
    public R ajaxGetFirmFilePageBeanTMP(@RequestParam(value = "versionId", required = false) Integer versionId,
        Integer pageIndex, Integer pageSize) {
        versionId = 4;
        PageBean<FirmwareFile> pagebean =
            firmwareFileService.getFirmWareFileListByVersionIdByPage(versionId, pageIndex, pageSize);
        R r = R.ok().add("pageBean", pagebean);
        r.put("rows", pagebean.getList());
        r.put("total", pagebean.getTotalCount());
        return r;
    }

    @ResponseBody
    @RequestMapping("/upload_tmp")
    public R uploadAndReplaceTMP(@RequestParam(value = "versionId", required = false) Integer versionId,
        @RequestParam(value = "files", required = false) MultipartFile files[],
        @RequestParam(value = "type", required = false) String typeHexs[], // 16进制字符
        @RequestParam(value = "index", required = false) String indexHexs[], // 16进制字符
        @RequestParam(value = "name_", required = false) String names[], // 名称
        @RequestParam(value = "file_addr", required = false) String fileAddrHexs[], // 16进制字符
        @RequestParam(value = "remark", required = false) String remarks[] // 备注
    ) {
        if (files == null || typeHexs == null || indexHexs == null || names == null || remarks == null) {
            return R.fail("40000", "失败，上传为空");
        }
        if (files.length != typeHexs.length || files.length != typeHexs.length || files.length != indexHexs.length
            || files.length != names.length || (files.length != remarks.length && remarks.length > 0)) {
            return R.fail("40000", "请填写完整");
        }
        Integer types[] = new Integer[files.length];
        Integer fileIndex[] = new Integer[files.length];
        Integer fileAddr[] = new Integer[files.length];

        // verisonId
        if (versionId == null) {
            return R.fail("40000", "版本号为空");
        }

        // type
        for (int i = 0; i < typeHexs.length; i++) {
            String typehex = typeHexs[i];
            if (!StringUtils.isHexString(typehex)) {
                return R.fail("40000", "类型type不正确").add("errorRow", i + 1).add("value", typehex);
            }
            types[i] = Integer.parseInt(typehex, 16);
        }

        // fileIndex
        for (int i = 0; i < indexHexs.length; i++) {
            String fi = indexHexs[i];
            if (!StringUtils.isHexString(fi)) {
                return R.fail("40000", "类型fileIndex不正确").add("errorRow", i + 1).add("value", fi);
            }
            fileIndex[i] = Integer.parseInt(fi, 16);
        }

        // fileAddr
        for (int i = 0; i < fileAddr.length; i++) {
            String value = fileAddrHexs[i];
            if (!StringUtils.isHexString(value)) {
                return R.fail("40000", "类型fileAddr不正确").add("errorRow", i + 1).add("value", value);
            }
            fileAddr[i] = Integer.parseInt(value, 16);
        }

        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            if (StringUtils.isNull(name)) {
                return R.fail("40000", "name不能为空").add("errorRow", i + 1).add("value", name);
            }
        }
        List<FirmwareFile> firmwareFileList = new ArrayList<>();
        for (int i = 0; i < fileAddr.length; i++) {
            FirmwareFile firmwareFile = new FirmwareFile();
            firmwareFile.setFirmwareFileIndex(fileIndex[i]);
            firmwareFile.setFirmwareFileType(types[i]);
            firmwareFile.setFlashAddr(fileAddr[i]);
            firmwareFile.setName(names[i]);
            firmwareFile.setRemark(remarks.length > 0 ? remarks[i] : null);
            firmwareFile.setUserId(1);
            firmwareFile.setVersionId(versionId);
            firmwareFileList.add(firmwareFile);
        }
        try {
            firmwareFileService.saveOrUpdateBatch(firmwareFileList, files);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.fail();

    }

    @ResponseBody
    @RequestMapping("/upload-v2-tmp")
    public R updateFirmFileTmp(@RequestParam(value = "file", required = false) MultipartFile file) {
        File targetFile = new File("/usr/local/tomcat/static-web/lamp/static/zkzztech-xiaozhi-S3/ota-test.1.1.tar.gz");
        if (targetFile.exists()) {
            FileUtils.copyFile(targetFile, new File(targetFile.getParentFile(),
                targetFile.getName() + "." + DateUtil.dateFormat("yyyy-MM-dd HH_mm_ss", System.currentTimeMillis())));
        }
        try {

            targetFile.getParentFile().mkdirs();
            file.transferTo(targetFile);
            String md5 = FileUtils.getMD5Checksum(targetFile).toUpperCase();
            Integer checksum = DataUtils.getUnsignedShortCheckSum(FileUtils.getFileBytes(targetFile));
            // 修改id是15的md5和fileLength
            firmwareFileService.updateFileLengthAndMd5ById(15, md5, checksum, targetFile.length());
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail();
        }
        return R.ok();
    }

    @ResponseBody
    @RequestMapping("/v2-info-tmp")
    public R firmFileTmp() {
        File targetFile = new File("/usr/local/tomcat/static-web/lamp/static/zkzztech-xiaozhi-S3/ota-test.1.1.tar.gz");
        long len = 0;
        String md5 = "";
        Long lastModified = 0L;
        if (targetFile.exists()) {
            try {
                md5 = FileUtils.getMD5Checksum(targetFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            len = targetFile.length();
            lastModified = targetFile.lastModified();

        }
        return R.ok().add("fileLength", len).add("md5", md5).add("lastModified",
            DateUtil.dateFormat("yyyy-MM-dd HH_mm_ss", new Date(lastModified)));
    }

}
