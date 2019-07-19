package com.gzseeing.manager.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 设备型号表
 * </p>
 *
 * @author Haowen
 * @since 2018-10-08
 */
@TableName("device_no")
public class DeviceNo extends Model<DeviceNo> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备型号code
     */
    @TableField("device_code")
    private String deviceCode;
    /**
     * 设备型号名称
     */
    @TableField("device_name")
    private String deviceName;


    public Integer getId() {
        return id;
    }

    public DeviceNo setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public DeviceNo setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
        return this;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public DeviceNo setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public static final String ID = "id";

    public static final String DEVICE_CODE = "device_code";

    public static final String DEVICE_NAME = "device_name";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DeviceNo{" +
        ", id=" + id +
        ", deviceCode=" + deviceCode +
        ", deviceName=" + deviceName +
        "}";
    }
}
