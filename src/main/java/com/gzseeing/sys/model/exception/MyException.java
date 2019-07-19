package com.gzseeing.sys.model.exception;

import java.util.Map;

public abstract class MyException  extends RuntimeException implements ExceptionInterface{
	private static final long serialVersionUID = 1L;

	/**
	 * 返回码，错误都是4开头
	 * 操作失败
	 */
	private int code=40000;
	/**
	 *返回信息
	 */
	private String msg;
	/**
	 *  参数map
	 */
	private Map<String, Object> map;
	
	public MyException(int code, String msg,Exception e) {
		super(msg,e);
		this.code = code;
		this.msg = msg;
	}

	public MyException(int code, String msg, Map<String, Object> map, Exception e) {
		super(msg,e);
		this.code = code;
		this.msg = msg;
		this.map = map;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public String getMsgCode() {
		return String.valueOf(code);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

}
