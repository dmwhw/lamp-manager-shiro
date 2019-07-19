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
 * 产线测试表
 * </p>
 *
 * @author Haowen
 * @since 2018-11-02
 */
@TableName("device_test")
public class DeviceTest extends Model<DeviceTest> {

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
    @TableField("device_nick_name")
    private String deviceNickName;
    
    /**
     * 上次测试时间
     */
    @TableField("last_test_date")
    private Date lastTestDate;
    /**
     * 测试完成时间
     */
    @TableField("test_done_date")
    private Date testDoneDate;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态 0 未测试 1测试完毕
     */
    private Integer status;

    
    
    
    //-------------------extended
    @TableField(exist=false)
    private String snNo;
    
    @TableField(exist=false)
    private String mac;
    @TableField(exist=false)
    private String deviceUuid;
    

    @TableField(exist=false)
    private Integer isOnLine;
    //----------------------------------
    
    
    
    
    

    public Integer getId() {
        return id;
    }

    public DeviceTest setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public DeviceTest setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public Date getLastTestDate() {
        return lastTestDate;
    }

    public DeviceTest setLastTestDate(Date lastTestDate) {
        this.lastTestDate = lastTestDate;
        return this;
    }

    public Date getTestDoneDate() {
        return testDoneDate;
    }

    public DeviceTest setTestDoneDate(Date testDoneDate) {
        this.testDoneDate = testDoneDate;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public DeviceTest setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public DeviceTest setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public static final String ID = "id";

    public static final String DEVICE_ID = "device_id";

    public static final String LAST_TEST_DATE = "last_test_date";

    public static final String TEST_DONE_DATE = "test_done_date";

    public static final String REMARK = "remark";

    public static final String STATUS = "status";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DeviceTest{" +
        ", id=" + id +
        ", deviceId=" + deviceId +
        ", lastTestDate=" + lastTestDate +
        ", testDoneDate=" + testDoneDate +
        ", remark=" + remark +
        ", status=" + status +
        "}";
    }

	public String getSnNo() {
		return snNo;
	}

	public void setSnNo(String snNo) {
		this.snNo = snNo;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public Integer getIsOnLine() {
		return isOnLine;
	}

	public void setIsOnLine(Integer isOnLine) {
		this.isOnLine = isOnLine;
	}

	public String getDeviceNickName() {
		return deviceNickName;
	}

	public void setDeviceNickName(String deviceNickName) {
		this.deviceNickName = deviceNickName;
	}

	public String getDeviceUuid() {
		return deviceUuid;
	}

	public void setDeviceUuid(String deviceUuid) {
		this.deviceUuid = deviceUuid;
	}
    
    
    
    
}
