package com.gzseeing.sys.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.gzseeing.sys.varriable.SysConfig;
import com.gzseeing.utils.StringUtils;

public class R extends HashMap<String, Object> implements Serializable{

	private static final long serialVersionUID = -3463990285870343404L;
	private String msg;
	private String msgCode;
	private boolean result;
	private Map<String, Object> data = new HashMap<String, Object>();

	public R add(String key, Object value) {
		this.data.put(key, value);
		return this;
	}

	public R addAll(Map<String, Object> map) {
		if (map != null) {
			this.data.putAll(map);
		}
		return this;
	}

	/**
	 * 操作成功
	 * @author haowen
	 * @time 2017-11-8下午4:35:11
	 * @Description
	 * @param msgCode
	 * @param msg
	 * @return
	 */
	public static R ok(String msgCode, String msg) {
		return new R(msgCode, StringUtils.nvl(msg, "操作成功！"), true);
	}

	/**
	 * 操作成功
	 * @author haowen
	 * @time 2017-11-8下午4:34:42
	 * @Description
	 * @return
	 */
	public static R ok() {
		return new R("10000", "操作成功！", true);
	}

	/**
	 * 操作失败
	 * @author haowen
	 * @time 2017-11-8下午4:35:11
	 * @Description
	 * @param msgCode
	 * @param msg
	 * @return
	 */
	public static R fail(String msgCode, String msg) {
		return new R(msgCode, StringUtils.nvl(msg, "操作失败!"), false);
	}

	/**
	 * 操作失败
	 * @author haowen
	 * @time 2017-11-8下午4:34:42
	 * @Description
	 * @return
	 */
	public static R fail() {
		return new R("40000", "操作失败！", false);
	}

	/**
	 * 自定义返回内容
	 * @author haowen
	 * @time 2017-11-8下午4:34:18
	 * @Description
	 * @param msgCode
	 * @param msg
	 * @param result
	 * @return
	 */
	public static R result(String msgCode, String msg, boolean result) {
		return new R(msgCode, msg, result);
	}
	
	public R(String msgCode, String msg, boolean result) {
		this.msgCode = msgCode;
		this.msg = msg;
		this.result = result;
		this.put("result", result);
		//if(SysConfig.isDevMode()){
			this.put("msg", ""+msg);
		//}
		this.put("msgCode",msgCode);
		this.put("data",data);
	}

	public R set(String msgCode, String msg, boolean result){
		this.msgCode=msgCode;
		this.msg=msg;
		this.result=result;
		return this;
	}
	
	public R set(String msgCode, String msg){
		this.msgCode=msgCode;
		this.msg=msg;
		return this;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public String getMsg() {
		return msg;
	}

	public boolean getResult() {
		return result;
	}

	public Map<String, Object> getData() {
		return data;
	}

}
