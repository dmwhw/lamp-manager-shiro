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
 * App版本升级表
 * </p>
 *
 * @author Haowen
 * @since 2018-10-26
 */
@TableName("app_version")
public class AppVersion extends Model<AppVersion> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 版本号
     */
    @TableField("version_name")
    private String versionName;
    /**
     * 可更新的版本号
     */
    @TableField("next_version_name")
    private String nextVersionName;
    /**
     * 系统1,安卓 2苹果
     */
    private Integer system;
    /**
     * 状态0,无需更新 1,,可更新
     */
    private Integer status;
    /**
     * 发布日期
     */
    private Date date;
    /**
     * 说明
     */
    private String remark;


    public Integer getId() {
        return id;
    }

    public AppVersion setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getVersionName() {
        return versionName;
    }

    public AppVersion setVersionName(String versionName) {
        this.versionName = versionName;
        return this;
    }

    public String getNextVersionName() {
        return nextVersionName;
    }

    public AppVersion setNextVersionName(String nextVersionName) {
        this.nextVersionName = nextVersionName;
        return this;
    }

    public Integer getSystem() {
        return system;
    }

    public AppVersion setSystem(Integer system) {
        this.system = system;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public AppVersion setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public AppVersion setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public AppVersion setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public static final String ID = "id";

    public static final String VERSION_NAME = "version_name";

    public static final String NEXT_VERSION_NAME = "next_version_name";

    public static final String SYSTEM = "system";

    public static final String STATUS = "status";

    public static final String DATE = "date";

    public static final String REMARK = "remark";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "AppVersion{" +
        ", id=" + id +
        ", versionName=" + versionName +
        ", nextVersionName=" + nextVersionName +
        ", system=" + system +
        ", status=" + status +
        ", date=" + date +
        ", remark=" + remark +
        "}";
    }
}
