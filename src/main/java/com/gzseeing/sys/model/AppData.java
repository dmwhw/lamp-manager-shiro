package com.gzseeing.sys.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppData  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2049813754001870406L;

	private Map<String, Object> appData=new HashMap<String,Object>();

	public Map<String, Object> getAppData() {
		return appData;
	}

	public AppData setAppData(Map<String, Object> appData) {
		if (appData!=null){
			this.appData = appData;
		}
		return this;
	}

	public Integer getDataInteger(String key) {
		Object object = appData.get(key);
		if (object==null){
			return null;
		}
		try {
			if (object instanceof Number){
				return ((Number) object).intValue();
			}
			return Integer.valueOf( appData.get(key).toString());
		} catch (Exception e) {
		}
		 return null;
	}

	public String getDataString(String key) {
		Object object = appData.get(key);
		if (object==null){
			return null;
		}
		try {
			return object.toString();
		} catch (Exception e) {
		}
		 return null;
	}

	public Double getDataDouble(String key) {
		Object object = appData.get(key);
		if (object==null){
			return null;
		}
		try {
			if (object instanceof Number){
				return ((Number) object).doubleValue();
			}
			return Double.valueOf( appData.get(key).toString());
		} catch (Exception e) {
		}
		 return null;
	}

	public Long getDataLong(String key) {
		Object object = appData.get(key);
		if (object==null){
			return null;
		}
		try {
			if (object instanceof Number){
				return ((Long) object).longValue();
			}
			return Long.valueOf( appData.get(key).toString());
		} catch (Exception e) {
		}
		return null;
	}

	public Object getData(String key) {
		Object object = appData.get(key);
		if (object==null){
			return null;
		}
		
		return object;
	}

	@SuppressWarnings("rawtypes")
	public Map getDataMap(String key) {
		Object object = appData.get(key);
		if (object==null){
			return null;
		}
		try {
			if (object instanceof Map){
				return (Map) object;
			}
 		} catch (Exception e) {
		}
		
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDataList(String key) {
		Object object = appData.get(key);
		if (object==null){
			return null;
		}
		try {
			if (object instanceof List){
				return (List) object;
			}
 		} catch (Exception e) {
		}
		
		return null;	
	}
	public String[] getStringArray(String key) {
		Object object = appData.get(key);
		if (object==null){
			return null;
		}
		try {
			if (object instanceof String[]){
				return (String[]) object;
			}
 		} catch (Exception e) {
		}
		
		return null;	
	}

	public static AppData newInstance(){
		return new AppData();
	}

	@Override
	public String toString() {
		return "I [appData=" + appData + "]";
	}
	
}
