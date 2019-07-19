package com.gzseeing.utils;

public class StringUtils {
	/**
	 * @author yjh
	 * @Description 判断是否为空
	 * @date 2016.7.21 9:41
	 * @param str
	 * @return boolean
	 */
	public static boolean isNull(String str) {
		return str == null || "".equals(str.trim()) ? true : false;
	}
	
	/**
	 * 有一个null就返回true
	 * @author joyol
	 * @Description 
	 * @param str
	 * @return
	 */
	public static boolean isNull(String... str) {
		for (String string : str) {
			if (isNull(string)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isNotNull(String... str) {
		return !isNull(str);
	}
	
	public static Integer strToInteger(String str, Integer defaultValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static Integer objToInteger(Object obj, Integer defaultValue) {
		try {
			if (obj instanceof Number) {
				return ((Number) obj).intValue();
			}
			return Integer.parseInt(String.valueOf(obj));
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static Double objToDouble(Object obj, Double defaultValue) {
		try {
			if (obj instanceof Double) {
				return (Double) obj;
			}
			return Double.parseDouble(String.valueOf(obj));
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static Long objToLong(Object obj, Long defaultValue) {
		try {
			if (obj instanceof Long) {
				return (Long) obj;
			}
			return Long.parseLong(String.valueOf(obj));
		} catch (Exception e) {
			return defaultValue;
		}
	}
	private final static String REGEX_IS_NUMBER="^([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])$";
	private final static String REGEX_IS_SIGNED_INTEGER = "^[-\\+]?\\d+$";
	/**
	 * 用于判断是否 数字，可包含小数
	 * 
	 * @Description 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		if (str != null && str.matches(REGEX_IS_NUMBER)) {
			return true;
		}
		return false;
	}
	/**
	 * 用于判断是否 数字，可包含符号
	 * 
	 * @Description 
	 * @param str
	 * @return
	 */
	public static boolean isSignedNumber(String str) {
		if (str != null && str.matches(REGEX_IS_SIGNED_INTEGER)) {
			return true;
		}
		return false;
	}
	public final static String REGEX_IS_HEX="^[A-Fa-f0-9]+$";
	public static boolean isHexString(String str){
		if (str != null && str.matches(REGEX_IS_HEX)) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * 类似Oracle的nvl,把内容null变为非空�?
	 * @author haowen
	 * @Description 支持 String-->""
	 * @param obj
	 * @return
	 */
	public static String nvl(Object obj) {
		return nvl(obj,"");
	}

	/**
	 * 如果爲空，替換為 替換字符
	 * @author haowen
	 * @Description 
	 * @param obj
	 * @param replacement
	 * @return
	 */
	public static String nvl(Object obj, String replacement) {
		if (obj == null) {
			return replacement;
		}
		return obj.toString();
	}




}
