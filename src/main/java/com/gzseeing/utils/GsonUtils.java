package com.gzseeing.utils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

public class GsonUtils {

	public static  String parseJSON(Object obj){
		try {
			Gson gson=new Gson();
			return gson.toJson(obj);
		} catch (Exception e) {
		}
		return null;
	}
	
	@SuppressWarnings("restriction")
	public static <T> List<T>  toList(String  json,Class<T> clazz){
		try {
			Gson gson=new Gson();
			//Type type=new TypeToken<List<T>>(){}.getType();
		    // 生成List<T> 中的 List<T>
		    Type listType = ParameterizedTypeImpl.make(List.class, new Class[]{clazz}, null);
			List<T> list = gson.fromJson(json, listType);
			return list;
 		} catch (Exception e) {
 			e.printStackTrace();
		}
		return null;
	}
 
	/**
	 * 如果是整数，默认是Long
	 * @author haowen
	 * @time 2018年7月18日上午10:37:39
	 * @Description  
	 * @param json
	 * @return
	 */
	public static Map<String,Object>  toMap(String  json ){
		try {
			Gson gson=new GsonBuilder().registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
				 
                @Override
                public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                    if (src == src.longValue())
                        return new JsonPrimitive(src.longValue());
                    return new JsonPrimitive(src);
                }
            }).create();
			Type type=new TypeToken<Map<String,Object>>(){}.getType();
 			return gson.fromJson(json, type);
 		} catch (Exception e) {
		}
		return null;
	}
	
	public static<T> T  toBean(String  json ,Class<T> clazz){
		try {
			Gson gson=new Gson();
  			return gson.fromJson(json, clazz);
 		} catch (Exception e) {
		}
		return null;
	}

	
}
