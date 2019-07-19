package com.gzseeing.sys.redis.impl;

import org.springframework.stereotype.Component;

import com.gzseeing.sys.redis.AbstractRedisDao;
import com.gzseeing.utils.SerializeUtil;

@SuppressWarnings("unchecked")
@Component("objectRedisDao")
public class ObjectRedisDaoImpl extends AbstractRedisDao<String, Object> {

	@Override
	public Object bytesToV(byte[] bs) {
		try {
			return SerializeUtil.unSerializeString(bs);
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
	public  String bytesToMapKey(byte[] bs) {
		try {
			return   new String(bs, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public  Object bytesToMapVaule(byte[] bs) {
		if (bs==null){
			return null;
		}
		try {
			return   SerializeUtil.unserialize(bs);
		} catch (Exception e) {
			//e.printStackTrace();
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
	public  byte[] getMapValueBytes(Object mapvalue) {
		try {
			return SerializeUtil.serialize(mapvalue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
