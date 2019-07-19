package com.gzseeing.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {

	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois =  new ObjectInputStream( bais);
			return ois.readObject();
		}catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public static byte[] serializeString(String string){
		try {
			return string.getBytes("utf-8");
		} catch (Exception e) {
 
		}
		return null;
	}
	
	public static String unSerializeString(byte[] buff){
		if (buff==null){
			return null;
		}
		try {
			return new String(buff,"utf-8");
		} catch (Exception e) {
 			e.printStackTrace();
		}
		return null;
	}

}