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
 * 设备固件版本登记
 * </p>
 *
 * @author Haowen
 * @since 2018-08-25
 */
@TableName("software_version")
public class SoftwareVersion extends Model<SoftwareVersion> {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 设备型号id
	 */
	@TableField("model_no_id")
	private Integer modelNoId;

	/**
	 * 主版本
	 */
	@TableField("main_version")
	private Integer mainVersion;

	/**
	 * 子版本
	 */
	@TableField("sub_version")
	private Integer subVersion;

	/**
	 * 版本名称
	 */
	@TableField("version_name")
	private String versionName;

	@TableField("android_min_version")
	private String androidMinVersion;

	@TableField("ios_min_version")
	private String iosMinVersion;

	/**
	 * 可更新的版本
	 */
	@TableField("next_version_id")
	private Integer nextVersionId;

	/**
	 * 升级选项0无需更新、1可选更新、2强制更新、4公测版本
	 */
	private Integer type;

	/**
	 * 提交日期
	 */
	@TableField("post_time")
	private Date postTime;

	/**
	 * 发布日期
	 */
	@TableField("publish_date")
	private Date publishDate;

	/**
	 * 状态0、提交 1、已发布
	 */
	private Integer status;

	/**
	 * 版本说明
	 */
	private String remark;

	/**
	 * 提交
	 */
	public final static Integer STATUS_POSTED = 0;

	/**
	 * 发布
	 */
	public final static Integer STATUS_PUBLISHED = 1;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getModelNoId() {
		return modelNoId;
	}

	public void setModelNoId(Integer modelNoId) {
		this.modelNoId = modelNoId;
	}

	public Integer getMainVersion() {
		return mainVersion;
	}

	public void setMainVersion(Integer mainVersion) {
		this.mainVersion = mainVersion;
	}

	public Integer getSubVersion() {
		return subVersion;
	}

	public void setSubVersion(Integer subVersion) {
		this.subVersion = subVersion;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getAndroidMinVersion() {
		return androidMinVersion;
	}

	public void setAndroidMinVersion(String androidMinVersion) {
		this.androidMinVersion = androidMinVersion;
	}

	public String getIosMinVersion() {
		return iosMinVersion;
	}

	public void setIosMinVersion(String iosMinVersion) {
		this.iosMinVersion = iosMinVersion;
	}

	public Integer getNextVersionId() {
		return nextVersionId;
	}

	public void setNextVersionId(Integer nextVersionId) {
		this.nextVersionId = nextVersionId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SoftwareVersion{" + ", id=" + id + ", modelNoId=" + modelNoId + ", mainVersion=" + mainVersion
				+ ", subVersion=" + subVersion + ", versionName=" + versionName + ", androidMinVersion="
				+ androidMinVersion + ", iosMinVersion=" + iosMinVersion + ", nextVersionId=" + nextVersionId
				+ ", type=" + type + ", postTime=" + postTime + ", publishDate=" + publishDate + ", status=" + status
				+ ", remark=" + remark + "}";
	}
}
