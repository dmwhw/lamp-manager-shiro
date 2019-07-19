package com.gzseeing.manager.entity;

import java.util.Date;

import org.codehaus.groovy.util.Finalizable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 后台管理人员
 * </p>
 *
 * @author Haowen
 * @since 2018-08-25
 */
@TableName("backend_user")
public class BackendUser extends Model<BackendUser> {

	private static final long serialVersionUID = 1L;

	
	public static final Integer STATUS_ON=1;
	public static final Integer STATUS_OFF=0;

	
	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 盐值
	 */
	private String salt;
	
	/**
	 * 手机号
	 */
	private String phone;
	
	/**
	 * 昵称
	 */
	@TableField("nick_name")
	private String nickName;
	
	/**
	 * is_root
	 */
	@TableField("is_root")
	private Integer isRoot;
	
	/**
	 * 状态
	 */
	private Integer status;
	
	/**
	 * 创建时间
	 */
	@TableField("create_date")
	private Date createDate;
 

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getIsRoot() {
		return isRoot;
	}

	public void setIsRoot(Integer isRoot) {
		this.isRoot = isRoot;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	protected Serializable pkVal() {
		return null;
	}

	@Override
	public String toString() {
		return "BackendUser{" + ", id=" + id + ", username=" + username + ", password=" + password + ", salt=" + salt
				+ ", phone=" + phone + ", nickName=" + nickName + ", isRoot=" + isRoot + ", status=" + status
				+ ", createDate=" + createDate + "}";
	}
}
