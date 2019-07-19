package com.gzseeing.manager.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Haowen
 * @since 2018-08-30
 */
@TableName("device_import_log")
public class DeviceImportLog extends Model<DeviceImportLog> {

    private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("file_name")
    private String fileName;
    @TableField("backend_id")
    private Integer backendId;
    private String log;
    private String status;
    private Date time;


    
    public Integer getId() {
        return id;
    }

    public DeviceImportLog setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public DeviceImportLog setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public Integer getBackendId() {
        return backendId;
    }

    public DeviceImportLog setBackendId(Integer backendId) {
        this.backendId = backendId;
        return this;
    }

    public String getLog() {
        return log;
    }

    public DeviceImportLog setLog(String log) {
        this.log = log;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public DeviceImportLog setStatus(String status) {
        this.status = status;
        return this;
    }
    
    public Date getTime() {
		return time;
	}

	public DeviceImportLog setTime(Date time) {
		this.time = time;
		return this;
	}



	public static final String ID = "id";

    public static final String FILE_NAME = "file_name";

    public static final String BACKEND_ID = "backend_id";

    public static final String LOG = "log";

    public static final String STATUS = "status";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DeviceImportLog{" +
        ", id=" + id +
        ", fileName=" + fileName +
        ", backendId=" + backendId +
        ", log=" + log +
        ", status=" + status +
        "}";
    }
}
