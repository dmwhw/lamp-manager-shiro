package com.gzseeing.sys.springmvc.convert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**
 * @author haowen
 * spring Mvc的日期转换，参数可以直接放个Date，
 * 不过要符合 yyyy-MM-dd  yyyy-MM-dd HH:mm:ss
 */
@Component
public class DateConverter implements Converter<String, Date>{
		public final static String regex_yyyy_MM_dd="([0-9]{4})[-]([0-9]{1,2})[-]([0-9]{1,2})";
		public final static String regex_yyyy_MM_dd_HH_mm_ss="([0-9]{4})[-]([0-9]{1,2})[-]([0-9]{1,2})\\s{1}([0-9]{1,2})[:]([0-9]{1,2})[:]([0-9]{1,2})";

	    @Override
	    public Date convert(String stringDate) {
	        if (stringDate==null){return null;}
	        String pattern=null;
	        if (stringDate.matches(regex_yyyy_MM_dd)){
	        	pattern="yyyy-MM-dd";
	        }else if (stringDate.matches(regex_yyyy_MM_dd_HH_mm_ss)){
	        	pattern="yyyy-MM-dd HH:mm:ss";
	        }else{
	        	return null;
	        }	        
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);	        
	        try {
	            return simpleDateFormat.parse(stringDate);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	  

}
