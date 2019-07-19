package com.gzseeing.sys.redis.impl;

import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Component;

import com.gzseeing.sys.redis.AbstractRedisDao;

@Component("stringRedisDao")
public class StringRedisDaoImpl extends AbstractRedisDao<String, String> {

	@Override
	public String bytesToV(byte[] bs) {
		try {
			return new String(bs, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String bytesToK(byte[] bs) {
		try {
			return new String(bs, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String bytesToMapKey(byte[] bs) {
		try {
			return   new String(bs, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String bytesToMapVaule(byte[] bs) {
		try {
			return   new String(bs, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public byte[] getValueBytes(String v) {
		try {
			return v.getBytes("utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public byte[] getKeyBytes(String k) {
		return super.getKeyBytes(k);
	}

	@Override
	public   byte[] getMapKeyBytes(String mapkey) {
		return super.getMapKeyBytes(mapkey);
	}

	@Override
	public  byte[] getMapValueBytes(String mapvalue) {
		try {
			return   mapvalue .getBytes("utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
