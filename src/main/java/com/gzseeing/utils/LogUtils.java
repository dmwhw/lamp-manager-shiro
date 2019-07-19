package com.gzseeing.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gzseeing.sys.varriable.SysConfig;

 



/**
 * 通用日志类
 * 级别 FATAL>ERROR>WARN》INFO>DEBUG>TRACE>ALL
 * 只定义级别，不定义实际业务，利于扩展
 * @Description  封装的初衷是为了日后方便更改日志系统
 * @author haowen
 * @version 1.0.0  2017-3-15 上午10:01:16
 * @see       [相关类/方法]
 * @since     [产品/模块版本] 
 */

public class LogUtils {
	
	private static Map<String,Logger> cache=new ConcurrentHashMap<String, Logger>();
	
	private LogUtils(){
	}
	/**
	 * 初始化Logger实现类 Log4j输出级别
	 * @Description 
	 */
	public static void init(){
		//Level SystemLoggerLevel =Level.toLevel( SysConfig.SysParam.get(SysConfig.CONFIG_LOGGER_LEVEL),Level.DEBUG);
		//setRootLoggerLevel(SystemLoggerLevel);
	}
	
	/**
	 * 获取Logger实现类的 日志打印级别
	 * @Description
	 * @return
	 */
	public static Level getRootLoggerLevel() {
		return LogManager.getRootLogger().getLevel();
	}

	
	/**
	 * 获取Logger。Utils类的Logger的根本来源
	 * @Description 此处使用了缓存
	 * @return
	 */
	private static Logger getLogger(){
		Throwable t=new Throwable();
		StackTraceElement st = t.getStackTrace()[3];
		Logger logger=cache.get(st.getClassName());
		if (logger==null ){
			logger=LogManager.getLogger(st.getClassName());
			cache.put(st.getClassName(), logger);
		}
		t=null;
		return logger;
	}
	/**
	 * 记录致命错误。
	 * @author haowen
	 * @Description  
	 * @param msg
	 */
	public static void fatal(String msg,Object ...obj){
		commomLog(Level.FATAL,msg,obj);
	}
	public static void error(String msg,Object ...obj){
		commomLog(Level.ERROR,msg,obj);
	}	
	/**
	 * 警告级别：疑似攻击，可能发生危险的信息
	 * @Description 警告
	 * @param msg
	 */
	public static  void warn(String msg,Object ...obj){
		commomLog(Level.WARN,msg,obj);
	}

	/**
	 * 生产环境，普通信息提示
	 * @Description 用于普通提示、SQL 增加删除修改操作、Service记录\Action访问、拦截器网址访问记录
	 * @param msg
	 */
	public static  void info(String msg,Object ...obj){
		commomLog(Level.INFO,msg,obj);
	}	
	/**
	 * sql查询语句、拦截器放行信息
	 * @author haowen
	 * @Description
	 * @param msg
	 */
	public static  void debug(String msg,Object ...obj){
		commomLog(Level.DEBUG,msg,obj);
	}
	/**
	 * 只在个人开发时，用于调试使用
	 * @Description 
	 * @param msg
	 */
	public static void trace(String msg,Object ...obj){
		commomLog( Level.TRACE,msg,obj);
	}
	/**
	 * 级别最低
	 * @author haowen
	 * @Description 
	 * @param msg
	 */
	public static  void all(String msg,Object ...obj){
		commomLog(Level.ALL,msg,obj);
	}

	/**
	 * 获得错误异常的所有栈信息。
	 * @Description 
	 * @param t throwable
	 * @return
	 */
	public static String getStackTrace(Throwable t){
		if (t==null) return "";
		StringWriter sw=null;
		PrintWriter pw=null;
		String StackTrace="";
		try {			
			sw=new StringWriter();
			pw=new PrintWriter(sw, true); 
			t.printStackTrace(pw);
			StackTrace= sw.toString();
		} catch (Exception e) {
		}finally{
			if (sw!=null){
				try {
					sw.close();
				} catch (IOException e) {
				}
			}
			if (pw!=null){pw.close();}
		}return StackTrace;
	}
	

	/**
	 * @Description 通用打印功能。打印的根本来源
	 * @param p 打印级别
	 * @param msg 打印的信息
	 * 
	 */
	private static void commomLog(Level p,String msg,Object ...obj){ 
		Level SystemLoggerLevel =Level.toLevel( SysConfig.SysParam.get(SysConfig.CONFIG_LOGGER_LEVEL),Level.DEBUG);
		if (p.intLevel()==Level.OFF.intLevel()){
			return;
		}
		if (p.intLevel()<=SystemLoggerLevel.intLevel() ){
			getLogger().log(p, msg,obj);
		}
		
	}

}
