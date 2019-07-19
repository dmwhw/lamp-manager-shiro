package com.gzseeing.sys.model.exception;

/**
 * 记录由非法操作，或者其他原因导致的应用程序异常,<h1>此异常往Service上抛会记录日志<h1><br>
 * @Description <a>?</a>
 * @author haowen
 * @version 1.0.0  2017-3-24 下午10:59:14
 * @see       [相关类/方法]
 * @since     [产品/模块版本] 
 */
public class AppErrException extends MyException{	

	private static final long serialVersionUID = 1L;

	/**
	 * 应用程序异常，记录非法操作、程序异常等。此异常往Service上抛会记录日志
	 * @Description 
	 * @param code 错误码，从MyException 中定义
	 * @param msg 自定义提示消息
	 * @param e  必须的，程序异常的根本原因。，把捕获的异常放到这里。可为空（不建议）。
	 */
	public AppErrException(int code, String msg, Exception e) {
		super(code, msg, e);
	}
	/**
	 * 应用程序异常，记录非法操作、程序异常等。此异常往Service上抛会记录日志
	 * 这里错误码默认-1
	 * @Description 
	 * @param msg 自定义提示消息
	 * @param e 必须的，程序异常的根本原因 ，把捕获的异常放到这里，可为空（不建议）。
	 */
	public AppErrException( String msg, Exception e) {
		super(40000, msg, e);
	}

}