package com.gzseeing.sys.model.exception;

import java.util.Map;

/**
 * 用于Servcie抛出违规操作,返回上层使用.此类异常不做日志
 * 这个类型用于提示用户的错误信息。
 * @Description
 * @author haowen
 * @version 1.0.0  2017-3-24 下午5:36:57
 * @see       [相关类/方法]
 * @since     [产品/模块版本] 
 */

public class MsgException  extends MyException{	

	private static final long serialVersionUID = 1L;

	public MsgException(int code, String msg, Map<String, Object> map, Exception e) {
		super(code,msg,map,e);
	}
	
	public MsgException(int code, String msg, Exception e) {
		super(code, msg, e);
	}
	
	public MsgException(int code, String msg) {
		super(code, msg, null);
	}
	
	public MsgException(String msg, Exception e) {
		super(40000, msg, e);
	}
	
	public MsgException(String msg) {
		super(40000, msg,null);
	}
	
}
