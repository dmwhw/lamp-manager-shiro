package com.gzseeing.manager.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 设备表
 * </p>
 *
 * @author Haowen
 * @since 2018-08-29
 */
public class Device extends Model<Device> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备uuid
     */
    @TableField("device_uuid")
    private String deviceUuid;
    private String password;
    private String salt;
    /**
     * 设别别名
     */
    @TableField("device_nick_name")
    private String deviceNickName;
    /**
     * mac地址
     */
    private String mac;
    
    @TableField("sn_no")
    private String snNo;
    /**
     * 设备型号id
     */
    @TableField("device_model_id")
    private Integer deviceModelId;
    /**
     * 设备固件版本
     */
    @TableField("soft_ware_version")
    private String softWareVersion;
    /**
     * 0、待激活 1、已激活
     */
    private Integer status;
    /**
     * 激活日期
     */
    @TableField("active_date")
    private Date activeDate;
    /**
     * 上次在线时间
     */
    @TableField("last_login_time")
    private Date lastLoginTime;


    public Integer getId() {
        return id;
    }

    public Device setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public Device setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Device setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getSalt() {
        return salt;
    }

    public Device setSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public String getDeviceNickName() {
        return deviceNickName;
    }

    public Device setDeviceNickName(String deviceNickName) {
        this.deviceNickName = deviceNickName;
        return this;
    }

    public String getMac() {
        return mac;
    }

    public Device setMac(String mac) {
        this.mac = mac;
        return this;
    }

    public Integer getDeviceModelId() {
        return deviceModelId;
    }

    public Device setDeviceModelId(Integer deviceModelId) {
        this.deviceModelId = deviceModelId;
        return this;
    }

    public String getSoftWareVersion() {
        return softWareVersion;
    }

    public Device setSoftWareVersion(String softWareVersion) {
        this.softWareVersion = softWareVersion;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public Device setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Date getActiveDate() {
        return activeDate;
    }

    public Device setActiveDate(Date activeDate) {
        this.activeDate = activeDate;
        return this;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public Device setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        return this;
    }

    
    public String getSnNo() {
		return snNo;
	}

	public void setSnNo(String snNo) {
		this.snNo = snNo;
	}

	public static final String ID = "id";

    public static final String DEVICE_UUID = "device_uuid";

    public static final String PASSWORD = "password";

    public static final String SALT = "salt";

    public static final String DEVICE_NICK_NAME = "device_nick_name";

    public static final String MAC = "mac";

    public static final String DEVICE_MODEL_ID = "device_model_id";

    public static final String SOFT_WARE_VERSION = "soft_ware_version";

    public static final String STATUS = "status";

    public static final String ACTIVE_DATE = "active_date";

    public static final String LAST_LOGIN_TIME = "last_login_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Device{" +
        ", id=" + id +
        ", deviceUuid=" + deviceUuid +
        ", password=" + password +
        ", salt=" + salt +
        ", deviceNickName=" + deviceNickName +
        ", mac=" + mac +
        ", deviceModelId=" + deviceModelId +
        ", softWareVersion=" + softWareVersion +
        ", status=" + status +
        ", activeDate=" + activeDate +
        ", lastLoginTime=" + lastLoginTime +
        "}";
    }
}
