package com.gzseeing.manager.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gzseeing.manager.entity.FirmwareFile;
import com.gzseeing.manager.entity.SoftwareVersion;
import com.gzseeing.manager.mapper.FirmwareFileMapper;
import com.gzseeing.manager.mapper.SoftwareVersionMapper;
import com.gzseeing.manager.service.FirmwareFileService;
import com.gzseeing.sys.model.PageBean;
import com.gzseeing.sys.model.exception.MsgException;
import com.gzseeing.utils.DataUtils;
import com.gzseeing.utils.DateUtil;
import com.gzseeing.utils.FileUtils;

/**
 * <p>
 * 固件文件列表 服务实现类
 * </p>
 *
 * @author Haowen
 * @since 2018-08-25
 */
@Transactional
@Service
public class FirmwareFileServiceImpl implements FirmwareFileService {

    @Autowired
    private FirmwareFileMapper firmwareFileMapper;
    @Autowired
    private SoftwareVersionMapper softwareVersionMapper;
    @Value("${myconfig.firmfile.upload.path}")
    private String uploadFilePath;

    @Override
    public PageBean<FirmwareFile> getFirmWareFileListByVersionIdByPage(Integer versionId, Integer pageIndex,
        Integer pageSize) {
        Wrapper<FirmwareFile> wrap = new EntityWrapper<FirmwareFile>();
        wrap.eq("version_id", versionId);
        // count
        Integer count = firmwareFileMapper.selectCount(wrap);
        PageBean<FirmwareFile> pageBean = new PageBean<>(count, pageIndex, pageSize);
        if (count == 0) {// rowBounds -1
            return pageBean;
        }
        // 设置分页参数
        RowBounds rowBounds = new RowBounds(pageBean.getPageStart(), pageBean.getPageSize());
        List<FirmwareFile> result = firmwareFileMapper.selectPage(rowBounds, wrap.orderBy("id"));
        pageBean.setList(result);
        return pageBean;

    }

    @Override
    public int saveOrUpdateBatch(List<FirmwareFile> firmFileList, MultipartFile files[]) throws Exception {
        SoftwareVersion softwareVersion = softwareVersionMapper.selectById(firmFileList.get(0).getVersionId());
        Date nowDate = DateUtil.getNowDate();
        String yyyyMMddHHmmss = DateUtil.dateFormat("yyyy_MM_dd_HHmmss", nowDate);
        String targetPath =
            softwareVersion.getId() + "-" + softwareVersion.getMainVersion() + "." + softwareVersion.getSubVersion();

        for (int i = 0; i < firmFileList.size(); i++) {
            FirmwareFile firmwareFile = firmFileList.get(i);
            File uploadFileDir = new File(uploadFilePath, targetPath);
            File targetFile =
                new File(uploadFileDir.getCanonicalFile(), yyyyMMddHHmmss + "_" + files[i].getOriginalFilename());
            File parentFile = targetFile.getParentFile();
            parentFile.mkdirs();
            files[i].transferTo(targetFile);
            Integer checksum = DataUtils.getUnsignedShortCheckSum(FileUtils.getFileBytes(targetFile));
            String md5Checksum = FileUtils.getMD5Checksum(targetFile);
            // 查找有没有这个index和type和version的。如果有，更新。
            FirmwareFile condition = new FirmwareFile();
            condition.setVersionId(firmwareFile.getVersionId());
            condition.setFirmwareFileIndex(firmwareFile.getFirmwareFileIndex());
            condition.setFirmwareFileType(firmwareFile.getFirmwareFileType());
            FirmwareFile dbFilmware = firmwareFileMapper.selectOne(condition);

            firmwareFile.setCheckSum(checksum);
            firmwareFile.setFileMd5(md5Checksum);
            firmwareFile.setUploadDate(nowDate);
            firmwareFile.setFileLength(targetFile.length());
            firmwareFile.setFilePath(targetFile.getPath());
            // insert
            if (dbFilmware == null) {
                firmwareFileMapper.insert(firmwareFile);
                continue;
            }
            // update
            firmwareFile.setId(dbFilmware.getId());

            firmwareFileMapper.updateAllColumnById(firmwareFile);
        }

        return firmFileList.size();
    }

    @Override
    public FirmwareFile getByFirmFileId(Integer firmFileId) {
        return firmwareFileMapper.selectById(firmFileId);
    }

    @Override
    public boolean delFirmFileById(Integer firmFileId) {
        FirmwareFile ff = getByFirmFileId(firmFileId);
        if (ff == null) {
            return false;
        }
        Integer versionId = ff.getVersionId();
        SoftwareVersion softwareVersion = softwareVersionMapper.selectById(versionId);
        if (SoftwareVersion.STATUS_PUBLISHED.equals(softwareVersion.getStatus())) {
            return false;
        }
        firmwareFileMapper.deleteById(firmFileId);
        new File(ff.getFilePath()).delete();
        return true;
    }

    @Override
    public void addOneFirmFile(FirmwareFile firmwareFile, MultipartFile file) throws Exception {
        SoftwareVersion softwareVersion = softwareVersionMapper.selectById(firmwareFile.getVersionId());
        Date nowDate = DateUtil.getNowDate();
        String yyyyMMddHHmmss = DateUtil.dateFormat("yyyy_MM_dd_HHmmss", nowDate);
        String targetPath =
            softwareVersion.getId() + "-" + softwareVersion.getMainVersion() + "." + softwareVersion.getSubVersion();
        // 查找有没有这个index和type和version的。如果有报错
        FirmwareFile condition = new FirmwareFile();
        condition.setVersionId(firmwareFile.getVersionId());
        condition.setFirmwareFileIndex(firmwareFile.getFirmwareFileIndex());
        condition.setFirmwareFileType(firmwareFile.getFirmwareFileType());
        FirmwareFile dbFilmware = firmwareFileMapper.selectOne(condition);
        if (dbFilmware != null) {
            throw new MsgException(42015, "已存在相同的固件文件");
        }
        // 移动文件
        File uploadFileDir = new File(uploadFilePath, targetPath);
        File targetFile = new File(uploadFileDir.getCanonicalFile(), yyyyMMddHHmmss + "_" + file.getOriginalFilename());
        File parentFile = targetFile.getParentFile();
        parentFile.mkdirs();
        file.transferTo(targetFile);
        // 计算checksum
        Integer checksum = DataUtils.getUnsignedShortCheckSum(FileUtils.getFileBytes(targetFile));
        // 计算md5
        String md5Checksum = FileUtils.getMD5Checksum(targetFile);
        firmwareFile.setCheckSum(checksum);
        firmwareFile.setFileMd5(md5Checksum);
        firmwareFile.setUploadDate(nowDate);
        firmwareFile.setFileLength(targetFile.length());
        firmwareFile.setFilePath(targetFile.getPath());
        // insert
        firmwareFileMapper.insert(firmwareFile);

    }

    @Override
    public void updateOneFirmFile(FirmwareFile firmwareFile, MultipartFile file) throws Exception {
        SoftwareVersion softwareVersion = softwareVersionMapper.selectById(firmwareFile.getVersionId());
        // 版本验证，是否存在，是否已经发布了
        if (softwareVersion == null) {
            throw new MsgException(40000, "操作失败");
        }
        if (!SoftwareVersion.STATUS_POSTED.equals(softwareVersion.getStatus())) {
            throw new MsgException(40000, "版本不可编辑");
        }
        Date nowDate = DateUtil.getNowDate();
        String yyyyMMddHHmmss = DateUtil.dateFormat("yyyy_MM_dd_HHmmss", nowDate);
        String targetPath =
            softwareVersion.getId() + "-" + softwareVersion.getMainVersion() + "." + softwareVersion.getSubVersion();
        // 查找有没有这个index和type和version的。如果有报错
        FirmwareFile condition = new FirmwareFile();
        condition.setVersionId(firmwareFile.getVersionId());
        condition.setFirmwareFileIndex(firmwareFile.getFirmwareFileIndex());
        condition.setFirmwareFileType(firmwareFile.getFirmwareFileType());
        FirmwareFile dbFilmware = firmwareFileMapper.selectOne(condition);
        if (dbFilmware != null && dbFilmware.getId() != firmwareFile.getId()) {
            throw new MsgException(42015, "已存在相同的固件文件");
        }
        if (file != null && file.getSize() != 0) {
            // 移动文件
            File uploadFileDir = new File(uploadFilePath, targetPath);
            File targetFile =
                new File(uploadFileDir.getCanonicalFile(), yyyyMMddHHmmss + "_" + file.getOriginalFilename());
            File parentFile = targetFile.getParentFile();
            parentFile.mkdirs();
            file.transferTo(targetFile);
            // 计算checksum
            Integer checksum = DataUtils.getUnsignedShortCheckSum(FileUtils.getFileBytes(targetFile));
            // 计算md5
            String md5Checksum = FileUtils.getMD5Checksum(targetFile);
            firmwareFile.setFileLength(targetFile.length());
            firmwareFile.setFilePath(targetFile.getPath());
            firmwareFile.setCheckSum(checksum);
            firmwareFile.setFileMd5(md5Checksum);
            firmwareFile.setUploadDate(nowDate);

        }
        // update
        firmwareFileMapper.updateById(firmwareFile);

    }

    @Override
    public void updateFileLengthAndMd5ById(Integer id, String md5, Integer checksum, Long length) {
        FirmwareFile firmFile = new FirmwareFile();
        firmFile.setFileLength(length);
        firmFile.setFileMd5(md5);
        firmFile.setId(id);
        firmFile.setCheckSum(checksum);
        firmwareFileMapper.updateById(firmFile);
    }
}
