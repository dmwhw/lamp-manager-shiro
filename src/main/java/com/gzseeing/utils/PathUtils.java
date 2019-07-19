package com.gzseeing.utils;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import com.gzseeing.sys.varriable.SysConfig;

public class PathUtils {
	/**
	 * 获得tomcat目录，不带/
	 * @author haowen
	 * @Description  
	 * @return
	 */
	public static String getTomcatPath(){
		if(!SysConfig.isDevMode()){
			return "/usr/local/tomcat/lamp-manage";
		}
        String path=Thread.currentThread().getContextClassLoader().getResource("").toString(); 
        path=path.replace("file:", ""); //去掉file:    
        path=path.replace("/WEB-INF/classes/", ""); //去掉/WEB-INF/classes/ 
        if (path.startsWith("\\")){
        	path=path.substring(1); //去掉第一个\        	
        }
        File file=new File(path);
        //获得项目名上的webapps目录-->获得tomcat目录
        return file.getParentFile().getParentFile().getAbsolutePath().replaceAll("\\\\", "/");
 	}
	
	/**
	 * 获取项目路径
	 * @author joyol
	 * @Description 
	 * @param request
	 * @return
	 */
	public static String getWebAppsPath(HttpServletRequest request){
		return request.getServletContext().getRealPath("");
	}
	
	/**
	 * 获取项目路径
	 * @author joyol
	 * @Description 
	 * @param request
	 * @return
	 */
	public static String getWebAppsRealPath(HttpServletRequest request,String path){
		return request.getServletContext().getRealPath(path);
	}
	
	/**
	 * 不带/
	 * @author haowen
	 * @time 2018年4月9日下午2:00:27
	 * @Description  
	 * @return
	 */
	public static String getTmpDir(){
		String path = new File( System.getProperty("java.io.tmpdir")).getPath();
		if (path.endsWith(File.separator)){
			path=path.substring(0, path.length()-1);
		}
		return path;
	}

	
}
