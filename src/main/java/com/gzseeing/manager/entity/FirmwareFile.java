package com.gzseeing.manager.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 固件文件列表
 * </p>
 *
 * @author Haowen
 * @since 2018-08-25
 */
@TableName("firmware_file")
public class FirmwareFile   {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 版本id
     */
    @TableField("version_id")
    private Integer versionId;
    /**
     * 文件序号
     */
    @TableField("firmware_file_index")
    private Integer firmwareFileIndex;
    /**
     * 固件类型EH
     */
    @TableField("firmware_file_type")
    private Integer firmwareFileType;
    /**
     * 名称
     */
    private String name;
    /**
     * 文件地址:请使用绝对路径
     */
    @TableField("file_path")
    private String filePath;
    /**
     * 文件checksum
     */
    @TableField("check_sum")
    private Integer checkSum;
    
    @TableField("file_md5")
    private String fileMd5;
    
   
    /**
     * 文件长度(字节)
     */
    @TableField("file_length")
    private Long fileLength;
    /**
     * 烧录位置
     */
    @TableField("flash_addr")
    private Integer flashAddr;
    /**
     * 上传日期
     */
    @TableField("upload_Date")
    private Date uploadDate;
    /**
     * 上传人
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 备注,自己看的
     */
    private String remark;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    public Integer getFirmwareFileIndex() {
        return firmwareFileIndex;
    }

    public void setFirmwareFileIndex(Integer firmwareFileIndex) {
        this.firmwareFileIndex = firmwareFileIndex;
    }

    public Integer getFirmwareFileType() {
        return firmwareFileType;
    }

    public void setFirmwareFileType(Integer firmwareFileType) {
        this.firmwareFileType = firmwareFileType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(Integer checkSum) {
        this.checkSum = checkSum;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }

    public Integer getFlashAddr() {
        return flashAddr;
    }

    public void setFlashAddr(Integer flashAddr) {
        this.flashAddr = flashAddr;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

 

    public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}

	@Override
    public String toString() {
        return "FirmwareFile{" +
        ", id=" + id +
        ", versionId=" + versionId +
        ", firmwareFileIndex=" + firmwareFileIndex +
        ", firmwareFileType=" + firmwareFileType +
        ", name=" + name +
        ", filePath=" + filePath +
        ", checkSum=" + checkSum +
        ", fileLength=" + fileLength +
        ", flashAddr=" + flashAddr +
        ", uploadDate=" + uploadDate +
        ", userId=" + userId +
        ", remark=" + remark +
        "}";
    }
}
