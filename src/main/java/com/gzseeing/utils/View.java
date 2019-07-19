package com.gzseeing.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.gzseeing.sys.model.R;

/**
 * 当需要返回可能不一样的结果时，请使用这个类
 * @author haowen
 *
 */
public class View {
	/**
	 * 返回json
	 * 
	 * @author haowen
	 * @time 2017-11-10下午2:20:43
	 * @Description
	 * @param model
	 * @param response
	 * @return
	 */
	public static ModelAndView returnJSON(Object model, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
		try {
			if (model instanceof R) {
				R r = (R) model;
				mv.addObject("msgCode", r.getMsgCode());
				mv.addObject("msg", r.getMsg());
				mv.addObject("result", r.getResult());
				mv.addObject("data", r.getData());
			} else {
				mv.addObject(model);
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return mv;
	}

	public static void returnJson( Serializable obj, HttpServletResponse response){
		   PrintWriter out = null;
	        try {
	            response.setCharacterEncoding("UTF-8");
	            response.setContentType("application/json; charset=utf-8");
	            out = response.getWriter();
	            out.write(GsonUtils.parseJSON(obj));
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (out != null) {
	                out.close();
	            }
	        }
	} 
	
	/**
	 * 返回文件
	 * 
	 * @author haowen
	 * @time 2017-11-10下午2:20:55
	 * @Description
	 * @param file
	 * @param response
	 * @return
	 */
	public static void returnFile(File file, HttpServletResponse res) {
		res.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
		try {
			res.setHeader("Content-Disposition", "attachment; filename="
					+ new String(StringUtils.nvl(file.getName()).getBytes("UTF-8"), "ISO8859-1"));
		} catch (Exception e1) {
		}
		FileInputStream fis = null;
		int len = -1;
		OutputStream os = null;
		try {
			fis = new FileInputStream(file);
			os = res.getOutputStream();
			byte[] buff = new byte[1024];
			while ((len = fis.read(buff)) > 0) {
				os.write(buff, 0, len);
				os.flush();
			}
		} catch (Exception e) {
		} finally {
			IOUtils.close(fis);
			IOUtils.close(os);
		}
	}

	/**
	 * 返回文件
	 * 
	 * @author haowen
	 * @time 2017-11-10下午2:20:55
	 * @Description
	 * @param file
	 * @param response
	 * @return
	 */
	public static void returnFile(File file, String fileName, HttpServletResponse res) {
		res.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
		try {
			res.setHeader("Content-Disposition",
					"attachment; filename=" + new String(StringUtils.nvl(fileName).getBytes("UTF-8"), "ISO8859-1"));
		} catch (Exception e1) {
		}
		FileInputStream fis = null;
		int len = -1;
		OutputStream os = null;
		try {
			fis = new FileInputStream(file);
			os = res.getOutputStream();
			byte[] buff = new byte[1024];
			while ((len = fis.read(buff)) > 0) {
				os.write(buff, 0, len);
				os.flush();
			}
		} catch (Exception e) {
		} finally {
			IOUtils.close(fis);
			IOUtils.close(os);
		}
	}

	/**
	 * 返回流
	 * @author haowen
	 * @time 2018年6月7日下午2:56:15
	 * @Description  
	 * @param is
	 * @param res
	 * @return
	 */
	public static ModelAndView returnInputStream(InputStream is, HttpServletResponse res) {
		res.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
		OutputStream os = null;
		try {
			int len = -1;
			os = res.getOutputStream();
			byte[] buff = new byte[1024];
			while ((len = is.read(buff)) > 0) {
				os.write(buff, 0, len);
				os.flush();
			}
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 * 返回字节
	 * @author haowen
	 * @time 2018年6月7日下午2:56:04
	 * @Description 
	 * @param bytes
	 * @param res
	 * @return
	 */
	public static ModelAndView returnBytes(byte[] bytes, HttpServletResponse res) {
		res.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
		OutputStream os = null;
		try {
 			os = res.getOutputStream();
 			os.write(bytes);
		} catch (Exception e) {
		}
		return null;
	}
}
