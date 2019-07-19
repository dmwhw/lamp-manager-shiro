package com.gzseeing.manager.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 设备临时导入表
 * </p>
 *
 * @author Haowen
 * @since 2018-08-29
 */
@TableName("device_import")
public class DeviceImport  {
	private static final long serialVersionUID = 2978268838710315553L;
	
	/**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    /**
     * 导入批次号
     */
    @TableField("import_no")
    private String importNo;
    
    /**
     * 设备mac地址
     */
    private String mac;
    
    /**
     * 机型代码code
     */
    @TableField("model_code")
    private String modelCode;
    
    /**
     * model_number_id
     */
    @TableField("model_number_id")
    private Integer modelNumberId;
    
    /**
     * 导入时间
     */
    @TableField("import_time")
    private Date importTime;
    
    /**
     * 导入人
     */
    @TableField("import_user")
    private Integer importUser;
    
    /**
     * 导入状态0、创建 1、已经导入  -1 删除.真删除
     */
    private Integer status;


    public Integer getId() {
        return id;
    }

    public DeviceImport setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getImportNo() {
        return importNo;
    }

    public DeviceImport setImportNo(String importNo) {
        this.importNo = importNo;
        return this;
    }

    public String getMac() {
        return mac;
    }

    public DeviceImport setMac(String mac) {
        this.mac = mac;
        return this;
    }

    public String getModelCode() {
        return modelCode;
    }

    public DeviceImport setModelCode(String modelCode) {
        this.modelCode = modelCode;
        return this;
    }

    public Integer getModelNumberId() {
        return modelNumberId;
    }

    public DeviceImport setModelNumberId(Integer modelNumberId) {
        this.modelNumberId = modelNumberId;
        return this;
    }

    public Date getImportTime() {
        return importTime;
    }

    public DeviceImport setImportTime(Date importTime) {
        this.importTime = importTime;
        return this;
    }

    public Integer getImportUser() {
        return importUser;
    }

    public DeviceImport setImportUser(Integer importUser) {
        this.importUser = importUser;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public DeviceImport setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public static final String ID = "id";

    public static final String IMPORT_NO = "import_no";

    public static final String MAC = "mac";

    public static final String MODEL_CODE = "model_code";

    public static final String MODEL_NUMBER_ID = "model_number_id";

    public static final String IMPORT_TIME = "import_time";

    public static final String IMPORT_USER = "import_user";

    public static final String STATUS = "status";

 

    @Override
    public String toString() {
        return "DeviceImport{" +
        ", id=" + id +
        ", importNo=" + importNo +
        ", mac=" + mac +
        ", modelCode=" + modelCode +
        ", modelNumberId=" + modelNumberId +
        ", importTime=" + importTime +
        ", importUser=" + importUser +
        ", status=" + status +
        "}";
    }
}
