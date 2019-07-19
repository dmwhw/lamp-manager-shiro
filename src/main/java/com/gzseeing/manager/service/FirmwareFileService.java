package com.gzseeing.manager.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.gzseeing.manager.entity.FirmwareFile;
import com.gzseeing.sys.model.PageBean;

/**
 * <p>
 * 固件文件列表 服务类
 * </p>
 *
 * @author Haowen
 * @since 2018-08-25
 */
public interface FirmwareFileService {

    /**
     * 获取固件文件列表的分页
     * 
     * @author haowen
     * @time 2018年8月28日下午2:17:23
     * @Description
     * @param versionId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PageBean<FirmwareFile> getFirmWareFileListByVersionIdByPage(Integer versionId, Integer pageIndex, Integer pageSize);

    /**
     * 保存或者更新文件
     * 
     * @author haowen
     * @time 2018年8月29日下午1:52:16
     * @Description
     * @param firmFileList
     * @param files
     * @return
     * @throws Exception
     */
    int saveOrUpdateBatch(List<FirmwareFile> firmFileList, MultipartFile[] files) throws Exception;

    /**
     * 通过id获取firmFile
     * 
     * @author haowen
     * @time 2018年9月17日上午9:25:39
     * @Description
     * @param firmFileId
     * @return
     */
    FirmwareFile getByFirmFileId(Integer firmFileId);

    /**
     * 有检验的删除
     * 
     * @author haowen
     * @time 2018年9月17日上午9:30:01
     * @Description
     * @param firmFileId
     * @return
     */
    boolean delFirmFileById(Integer firmFileId);

    /**
     * 新上传并保存firmFile
     * 
     * @author haowen
     * @time 2018年9月19日下午3:21:15
     * @Description
     * @param firmwareFile
     * @param file
     * @throws Exception
     */
    void addOneFirmFile(FirmwareFile firmwareFile, MultipartFile file) throws Exception;

    /**
     * 更新固件文件
     * 
     * @author haowen
     * @time 2018年9月20日上午8:56:50
     * @Description
     * @param firmwareFile
     * @param file
     * @throws Exception
     */
    void updateOneFirmFile(FirmwareFile firmwareFile, MultipartFile file) throws Exception;

    void updateFileLengthAndMd5ById(Integer id, String md5, Integer checksum, Long length);

}
