package com.gzseeing.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.sun.org.apache.xml.internal.serialize.OutputFormat.DTD;

/**
 * @author haowen
 * 全是 13位的System.currentTimeMillis()
 */
public class DateUtil {
	
	
	
	private final static String [] weekDayDict={"NA","周日","周一","周二","周三","周四","周五","周六"};
	/**
	 * 获得当前时间
	 * @author haowen
	 * @time 2017年11月20日上午11:40:32
	 * @Description 
	 * @return
	 */
	public  static Date getNowDate(){
  		return new Date(System.currentTimeMillis());
	}
	
	public  static Date getDate(int year,int month,int date,int hourOfDay,int minute,int second){
		Calendar instance = Calendar.getInstance();
		//这里有个坑，1月是0
		instance.set(year, month-1, date, hourOfDay, minute, second);
		//这里也是坑。他用当前时间初始化。有毫秒
		instance.set(Calendar.MILLISECOND, 0); 
		return instance.getTime();
	}
	
	public static String dateFormat(String exp,Date date) {
		if (date==null) {
			return null;
		}
		SimpleDateFormat sp = new SimpleDateFormat(exp);
		return sp.format(date);
	}
	/**
	 * null不作修改
	 * @author haowen
	 * @time 2018年2月2日下午2:38:04
	 * @Description  
	 * @param dateToUpdate
	 * @param year
	 * @param month
	 * @param date
	 * @param hourOfDay
	 * @param minute
	 * @param second
	 * @return
	 */
	public  static Date setDate(Date dateToUpdate,Integer year,Integer month,Integer date,Integer hourOfDay,Integer minute,Integer second,Integer mills){
		if (dateToUpdate==null ){
			return null;
		}
		Calendar instance = Calendar.getInstance();
		//这里有个坑，1月是0
		instance.setTime(dateToUpdate);
		if (year!=null){
			instance.set(Calendar.YEAR, year);
		}
		if (month!=null){
			instance.set(Calendar.MONTH, month-1);
		}
		if (date!=null){
			instance.set(Calendar.DATE, date);
		}
		if (hourOfDay!=null){
			instance.set(Calendar.HOUR_OF_DAY, hourOfDay);
		}
		if (minute!=null){
			instance.set(Calendar.MINUTE, minute);
		}
		if (second!=null){
			instance.set(Calendar.SECOND, second);
		}
		if (mills!=null){
			instance.set(Calendar.MILLISECOND, mills);
		}
		//这里也是坑。他用当前时间初始化。有毫秒
		return instance.getTime();
	}
	

	/**
	 * 获得某个时间的的持续时间
	 * @author haowen
	 * @Description 
	 * @param TimeStamp
	 * @returns timestamp
	 */
	public final static long [] getCurrentQuartzerPeroid(Long timeStamp){
    	SimpleDateFormat toSecond=new SimpleDateFormat("yyyyMMddHHmmss");
		long[] rs=null;
		try {
			rs = new long []{0l,0l}; 
			SimpleDateFormat sdf=new SimpleDateFormat("MM");
			SimpleDateFormat sdf2=new SimpleDateFormat("yyyy");
			String monthstr = sdf.format(new Date(timeStamp));
			String monthendStr=null;//季度末
			String yearstr = sdf2.format(new Date(timeStamp));
			int month=Integer.valueOf(monthstr);
			if (month>=1&& month<=3){
				monthstr="01";
				monthendStr="0331";
			}else if  (month>=4&& month<=6){
				monthstr="04";
				monthendStr="0630";
			}else if  (month>=7&& month<=9){
				monthstr="07";
				monthendStr="0930";
			}else if  (month>=10&& month<=12){
				monthstr="10";
				monthendStr="1231";
			}	  		
			rs[0]=toSecond.parse( yearstr+monthstr+"01000000")
						.getTime();//季度初
			rs[1]=toSecond.parse( yearstr+monthendStr+"235959")
						.getTime();//季度末
 		} catch ( Exception e) {
 		}
		
		return rs;		
	}
	
	/**
	 * 获得某个时间的季度的上一个季度的持续时间
	 * @author haowen
	 * @Description 
	 * @param TimeStamp
	 * @returns timestamp
	 */
	public final static long [] getLastQuartzerPeroid(Long timeStamp){
		long[] currentQuartzerPeroid = getCurrentQuartzerPeroid(timeStamp);
		if(currentQuartzerPeroid==null){
			return null;
		}
 		return getCurrentQuartzerPeroid(currentQuartzerPeroid[0]-1000);
	}
	
	/**
	 * 获得某个时间的上个月持续的时间
	 * @author haowen
	 * @Description 
	 * @param TimeStamp
	 * @returns timestamp
	 */
	public final static long [] getLastMonthPeroid(Long timeStamp){
		//获得上月份
		Calendar  calendar=Calendar.getInstance();
		calendar.setTime(new Date(timeStamp)); 
        calendar.add(Calendar.MONTH, -1);//获取上个月月份        
		return getCurrMonthPeroid(calendar.getTime().getTime());
		
	}
	
	
	/**
	 * 获得当前是周几
	 * @author haowen
	 * @time 2018年5月25日下午6:05:29
	 * @Description  
	 * @param timestamp
	 * @return
	 */
	public final static String getDayOfWeekName(long timestamp){
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date(timestamp));
		int i = cal.get(Calendar.DAY_OF_WEEK);
		return weekDayDict[i];
	}
	
	/**
	 * 获得本日持续的时间
	 * @author haowen
	 * @Description 
	 * @param TimeStamp
	 * @returns timestamp
	 */
	public final static long [] getDayPeroid(Long timeStamp){
		try {
			long[]  rs = new long []{0l,0l};
			SimpleDateFormat toSecond=new SimpleDateFormat("yyyyMMddHHmmss");
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
			String string = sdf.format(new Date(timeStamp));
			Date sdate=toSecond.parse(string+"000000");//
			//最后的时间
			Date edate=toSecond.parse(string+"235959");//
			rs[0]=sdate.getTime();//
			rs[1]=edate.getTime();			
			return rs;
		} catch (Exception e) {
 		}
		return null;
	}
	
	/**
	 * 获得前几天的日期，日期是当天的0点，早期的在前面
	 * @author haowen
	 * @time 2018年5月25日下午5:12:19
	 * @Description  
	 */
	public final static long[] getPreDays(long timestamp,int days){
		if (days<=0){
			return null;
		}
		Date date = new Date(timestamp);
		Date setDate = setDate(date, null, null, null, 0, 0, 0,0);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(setDate);
		long [] rs=new long[days];
		for(int i=days;i>0;i--){
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			rs[days-i]=calendar.getTime().getTime();
		}
		return rs;
		
	}
	
	
	/**
	 * 获得某个时间的本月的持续的时间
	 * @author haowen
	 * @Description 
	 * @param TimeStamp
	 * @returns timestamp
	 */
	public final static long [] getCurrMonthPeroid(Long timeStamp){
		try {
			long[]  rs = new long []{0l,0l};
			/*SimpleDateFormat toSecond = new SimpleDateFormat("yyyyMMddHHmmss");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
			String string = sdf.format(new Date(timeStamp));
			Date sdate = toSecond.parse(string+"01000000");//月第一天
			//计算月最后一天
			Calendar ca = Calendar.getInstance(Locale.CHINA);  
			ca.setTime(sdate);
			ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));    
			rs[0]=sdate.getTime();//月初
			rs[1]=toSecond.parse(sdf2.format(ca.getTime())+"235959").getTime();//月末
			*/			
			timeStamp = timeStamp / 1000 * 1000;
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(timeStamp);
			calendar.set(calendar.get(1), calendar.get(2), 1, 0, 0, 0);
			rs[0] = calendar.getTimeInMillis();
			calendar.add(2, 1);
			rs[1] = calendar.getTimeInMillis()-1000;
			return rs;
		} catch (Exception e) {
 		}
		return null;
	}
	
	public final static long [] getCurrYearPeriod(Long currTime){
		try {
			long [] rs=new long[]{0l,0l};
			SimpleDateFormat toSecond=new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar cal=Calendar.getInstance();
			cal.setTime(new Date(currTime));
			int i = cal.get(Calendar.YEAR);
			rs[0]=toSecond.parse(String.valueOf(i)+"0101000000").getTime();
			rs[1]=toSecond.parse(String.valueOf(i)+"1231235959").getTime();
			return rs;
		} catch ( Exception e) {
 		}
		return null;
	}
	
	/**
	 * 获得某个时间每个月包含的天数
	 * @author haowen
	 * @Description  
	 * @param startTimeStamp
	 * @param endTimeStamp
	 * @return
	 */
	public final static LinkedHashMap<String, Integer> getDaysPerMonth(Long startTimeStamp,Long endTimeStamp){
		LinkedHashMap<String, Integer> map=new LinkedHashMap<String, Integer>();
		if (startTimeStamp >endTimeStamp){
			return map;
		}
		Date startDate=new Date(startTimeStamp);
		Date endDate=new Date(endTimeStamp);
		//在结束日期之前
		while(startDate.before(endDate)){
 		}
		return map;
	}
	
	/**
	 *字符串变日期
	 * @author haowen
	 * @Description  
	 * @param format
	 * @param time
	 * @return
	 */
	public final static Date StringtoDate(String format,String time){
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		try {
			return sdf.parse(time);
		} catch (ParseException e) {
 		}
 		return null;
	}
	
	public final static Date timeStampToDate(Long time){		
		return new Date(time);
	}
	
	public final static Date getNextMonthDate(Long timestamp){
		//获得上月份
		Calendar  calendar=Calendar.getInstance();
		calendar.setTime(new Date( timestamp)); 
        calendar.add(Calendar.MONTH, 1);//获取上个月月份     
        return calendar.getTime();
	}
	
	/**
	 * 获取上个月的字符串
	 * @author haowen
	 * @Description  
	 * @param Timestamp
	 * @return
	 */
	public final static String getLastMonth(Long timestamp){
		//获得上月份
		Calendar  calendar=Calendar.getInstance();
		calendar.setTime(new Date( timestamp)); 
        calendar.add(Calendar.MONTH, -1);//获取上个月月份     
        return String .valueOf(calendar.get(Calendar.MONTH));
	}
	
	public final static String getNextMonth(Long timestamp){
		//获得上月份
		Calendar  calendar=Calendar.getInstance();
		calendar.setTime(new Date( timestamp)); 
        calendar.add(Calendar.MONTH, 1);//获取上个月月份     
        return String .valueOf(calendar.get(Calendar.MONTH));
	}
	
	/**
	 * 获取月的最后一天
	 * @author haowen
	 * @Description 
	 * @param Timestamp
	 * @return
	 */
	public final static  String getLastDayOfMonth(Long timestamp ){
        Calendar ca = Calendar.getInstance();    
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
 		return dateFormat("dd", ca.getTime());
	}
	
	public static String dateFormat(String exp,Long timestamp) {
		if (timestamp==null) {
			return null;
		}
		Date temp = new Date(timestamp);
		return dateFormat(exp, temp);
	}
	
	public static Date dateString2Date(String pattern,String yyyyMMddhhmmss){
		try {
			SimpleDateFormat sdf=new SimpleDateFormat(pattern);		
			return sdf.parse(yyyyMMddhhmmss);			
		} catch (Exception e) {
		}
		return null;
	}
	
	public static Long dateString2timeStamp(String pattern,String yyyyMMddhhmmss){
		try {
			SimpleDateFormat sdf=new SimpleDateFormat(pattern);		
			return sdf.parse(yyyyMMddhhmmss).getTime();			
		} catch (Exception e) {
		}
		return null;
	}
	
	
	/**
	 * 计算中间时间
	 * @author haowen
	 * @time 2018年2月9日上午9:05:06
	 * @Description 
	 * @param pattern
	 * @param workStart
	 * @param workEnd
	 * @return
	 */
	public static String calulateMiddleTimeStr(String pattern,String workStart,String workEnd) {
		Long dateString2timeStamp = DateUtil.dateString2timeStamp(pattern, workStart);
		Long dateString2timeStamp2 = DateUtil.dateString2timeStamp(pattern, workEnd);
		Long interval= (dateString2timeStamp2-dateString2timeStamp)/2;
		Long middleTimeStamp=(dateString2timeStamp+interval);
		return DateUtil.dateFormat(pattern, middleTimeStamp);
	}
}
