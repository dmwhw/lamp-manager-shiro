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
 * 产线测试项目表
 * </p>
 *
 * @author Haowen
 * @since 2018-11-02
 */
@TableName("device_test_subject")
public class DeviceTestSubject extends Model<DeviceTestSubject> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备id
     */
    @TableField("device_id")
    private Integer deviceId;
    /**
     * 项目代码
     */
    @TableField("subject_code")
    private String subjectCode;
    /**
     * 上次测试时间
     */
    @TableField("last_test_time")
    private Date lastTestTime;
    /**
     * 状态 0 未测试 1测试完毕
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;


    public Integer getId() {
        return id;
    }

    public DeviceTestSubject setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public DeviceTestSubject setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public DeviceTestSubject setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
        return this;
    }

    public Date getLastTestTime() {
        return lastTestTime;
    }

    public DeviceTestSubject setLastTestTime(Date lastTestTime) {
        this.lastTestTime = lastTestTime;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public DeviceTestSubject setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public DeviceTestSubject setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public static final String ID = "id";

    public static final String DEVICE_ID = "device_id";

    public static final String SUBJECT_CODE = "subject_code";

    public static final String LAST_TEST_TIME = "last_test_time";

    public static final String STATUS = "status";

    public static final String REMARK = "remark";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DeviceTestSubject{" +
        ", id=" + id +
        ", deviceId=" + deviceId +
        ", subjectCode=" + subjectCode +
        ", lastTestTime=" + lastTestTime +
        ", status=" + status +
        ", remark=" + remark +
        "}";
    }
}
