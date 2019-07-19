package com.gzseeing.sys.redis;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.codehaus.groovy.util.Finalizable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import com.gzseeing.utils.SerializeUtil;

public abstract class AbstractRedisDao<K, V> implements RedisDao<K, V> {
	@Resource(name = "redisTemplate")
	private RedisTemplate<Serializable, Serializable> redisTemplate;

	@Override
	public V get(K key) {
		return get(key, DEFAULT_DB_INDEX);
	}

	@Override
	public V get(final K key, final Integer dbIndex) {
		V value = null;
		try {
			value = redisTemplate.execute(new RedisCallback<V>() {
				@Override
				public V doInRedis(RedisConnection connection) throws DataAccessException {
					connection.select(dbIndex);
					byte[] bs = connection.get(getKeyBytes(key));
					return bytesToV(bs);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	@Override
	public void put(K k, V v, Long seconds) {
		put(k, v, seconds, DEFAULT_DB_INDEX);
	}

	@Override
	public void put(K k, V v, final Long seconds, final Integer dbIndex) {
		try {
			final byte[] rawKey = getKeyBytes(k);
			final byte[] rawValue = getValueBytes(v);

			redisTemplate.execute(new RedisCallback<String>() {
				@Override
				public String doInRedis(RedisConnection connection) throws DataAccessException {
					connection.select(dbIndex);
					if (seconds == null) {
						connection.set(rawKey, rawValue);
					} else {
						connection.setEx(rawKey, seconds, rawValue);
					}
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void leftPushAll(K key, Collection<V> vs, Long seconds) {
		leftPushAll(key, vs, seconds, DEFAULT_DB_INDEX);
	}

	@Override
	public void leftPushAll(K k, Collection<V> vs, final Long seconds, final Integer dbIndex) {
		try {
			final byte[] rawKey = getKeyBytes(k);
			final byte[][] rawValues = serializeCollection(vs);
			redisTemplate.execute(new RedisCallback<String>() {

				@Override
				public String doInRedis(RedisConnection connection) throws DataAccessException {
					connection.select(dbIndex);
					connection.lPush(rawKey, rawValues);
					if (seconds != null) {
						connection.expire(rawKey, seconds);
					}
					return null;
				}

			}, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void rightPushAll(K key, Collection<V> vs, final Long seconds, final Integer dbIndex) {
		try {
			final byte[] rawKey = getKeyBytes(key);
			final byte[][] rawValues = serializeCollection(vs);
			redisTemplate.execute(new RedisCallback<String>() {

				@Override
				public String doInRedis(RedisConnection connection) throws DataAccessException {
					connection.select(dbIndex);
					connection.rPush(rawKey, rawValues);
					if (seconds != null) {
						connection.expire(rawKey, seconds);
					}
					return null;
				}

			}, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void rightPushAll(K key, Collection<V> vs, Long seconds) {
		rightPushAll(key, vs, seconds, DEFAULT_DB_INDEX);
	}

	@Override
	public V leftPop(K key, final Integer dbIndex) {
		V value = null;
		try {
			final byte[] rawKey = getKeyBytes(key);
			value = redisTemplate.execute(new RedisCallback<V>() {
				@Override
				public V doInRedis(RedisConnection connection) throws DataAccessException {
					connection.select(dbIndex);
					byte[] rawValue = connection.lPop(rawKey);
					return bytesToV(rawValue);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}

	@Override
	public V leftPop(K key) {
		return leftPop(key, DEFAULT_DB_INDEX);
	}

	@Override
	public V rightPop(K key, final Integer dbIndex) {
		V value = null;
		try {
			final byte[] rawKey = getKeyBytes(key);
			value = redisTemplate.execute(new RedisCallback<V>() {
				@Override
				public V doInRedis(RedisConnection connection) throws DataAccessException {
					connection.select(dbIndex);
					byte[] rawValue = connection.rPop(rawKey);
					return bytesToV(rawValue);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}

	@Override
	public V rightPop(K key) {
		return rightPop(key, DEFAULT_DB_INDEX);
	}

	@Override
	public void rightPush(K key, V value, final Long seconds, final Integer dbIndex) {
		try {
			final byte[] rawKey = getKeyBytes(key);
			final byte[] rawValue = getValueBytes(value);
			redisTemplate.execute(new RedisCallback<String>() {
				@Override
				public String doInRedis(RedisConnection connection) throws DataAccessException {
					connection.select(dbIndex);
					connection.rPush(rawKey, rawValue);
					if (seconds != null)
						connection.expire(rawKey, seconds);
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void rightPush(K key, V value, Long seconds) {
		rightPush(key, value, seconds);
	}

	@Override
	public void leftPush(K key, V value, final Long seconds, final Integer dbIndex) {
		try {
			final byte[] rawKey = getKeyBytes(key);
			final byte[] rawValue = getValueBytes(value);
			redisTemplate.execute(new RedisCallback<String>() {
				@Override
				public String doInRedis(RedisConnection connection) throws DataAccessException {
					connection.select(dbIndex);
					connection.lPush(rawKey, rawValue);
					if (seconds != null)
						connection.expire(rawKey, seconds);
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void leftPush(K key, String value, Long seconds) {
		leftPush(key, value, seconds);
	}

	@Override
	public boolean exist(final K key, final Integer dbIndex) {
		boolean isExisted = false;
		try {
			isExisted = redisTemplate.execute(new RedisCallback<Boolean>() {

				@Override
				public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
					connection.select(dbIndex);
					return connection.exists(getKeyBytes(key));
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isExisted;
	}

	@Override
	public boolean exist(K key) {
		return exist(key, DEFAULT_DB_INDEX);
	}

	@Override
	public boolean setTimeOut(K key, final Long seconds, final Integer dbIndex) {
		Boolean isSuccess = true;
		try {
			final byte[] rawKey = getKeyBytes(key);
			isSuccess = redisTemplate.execute(new RedisCallback<Boolean>() {
				@Override
				public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
					connection.select(dbIndex);
					return connection.expire(rawKey, seconds);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess = false;
		}
		return isSuccess;
	}

	@Override
	public boolean setTimeOut(K key, Long seconds) {
		return setTimeOut(key, seconds, DEFAULT_DB_INDEX);
	}

	@Override
	public Long getTimeOut(K key, final Integer dbIndex) {
		long seconds = -3;
		redisTemplate.getHashValueSerializer();
		try {
			final byte[] rawKey = getKeyBytes(key);
			seconds = redisTemplate.execute(new RedisCallback<Long>() {
				@Override
				public Long doInRedis(RedisConnection connection) throws DataAccessException {
					connection.select(dbIndex);
					return connection.ttl(rawKey);

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return seconds;
	}

	@Override
	public Long getTimeOut(K key) {
		return getTimeOut(key, DEFAULT_DB_INDEX);
	}

	@Override
	public   void putMap(K key, final Map<K, V> map, final Long seconds, final Integer dbIndex) {
		try {
			final byte[] rawKey = getKeyBytes(key);
			redisTemplate.execute(new RedisCallback<String>() {

				@Override
				public String doInRedis(RedisConnection connection) throws DataAccessException {
					connection.select(dbIndex);
					connection.hMSet(rawKey, serializeMap(map));
					if (seconds != null) {
						connection.expire(rawKey, seconds);
					}
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public  void putMap(K key, Map<K, V> map, Long seconds) {
		putMap(key,map,seconds,DEFAULT_DB_INDEX);
	}
	@Override
	public  void saveValueInMap(K key, K mapkey, V mapValue, Long seconds) {
		saveValueInMap(key, mapkey, mapValue, seconds, DEFAULT_DB_INDEX);
	}

	@Override
	public  void saveValueInMap(final K key, K mapkey, V mapValue, final Long seconds,
			final Integer dbIndex) {
		if (mapkey != null) {
			final Map<byte[], byte[]> data = new HashMap<byte[], byte[]>();
			data.put(getMapKeyBytes(mapkey), getMapValueBytes(mapValue));
			redisTemplate.execute(new RedisCallback<Object>() {
				@Override
				public Object doInRedis(RedisConnection connection) throws DataAccessException {
					connection.select(dbIndex);
					byte[] serialize = getKeyBytes(key);
					connection.hMSet(serialize, data);
					if (seconds != null) {
						connection.expire(serialize, seconds);
					}
					return null;
				}
			});
		}

	}

	@Override
	public  Map<K, V> getByMapKeys(final K key, final K[] mapElementKeys, final Integer dbIndex) {
		return redisTemplate.execute(new RedisCallback<Map<K, V>>() {
			@Override
			public Map<K, V> doInRedis(RedisConnection connection) throws DataAccessException {
				Map<K, V> map = new HashMap<K, V>();
				connection.select(dbIndex);
				byte[] keybytes = getKeyBytes(key);
				if (connection.exists(keybytes)) {
					for (int i = 0; i < mapElementKeys.length; i++) {
						byte[] mapkeybytes = getMapKeyBytes(mapElementKeys[i]);
						List<byte[]> hMGet = connection.hMGet(keybytes, mapkeybytes);

						if (hMGet != null && hMGet.size() > 0) {
							if (hMGet.get(0) != null) {
								V value = (V) SerializeUtil.unserialize(hMGet.get(0));
								map.put(mapElementKeys[i], value);
							} else {
								map.put(mapElementKeys[i], null);
							}

						} else {
							map.put(mapElementKeys[i], null);
						}
					}
					return map;
				}
				return null;
			}
		});
	}

	@Override
	public  Map<K, V> getByMapKeys(K key, K[] mapElementKeys) {
		return getByMapKeys(key, mapElementKeys, DEFAULT_DB_INDEX);
	}

	@Override
	public  Map<K, V> getAllFromMap(final K k, final Integer dbIndex) {
		if (k == null) {
			return null;
		}
		return redisTemplate.execute(new RedisCallback<Map<K, V>>() {
			@Override
			public Map<K, V> doInRedis(RedisConnection connection) throws DataAccessException {
				Map<K, V> map = new HashMap<K, V>();
				connection.select(dbIndex);
				byte[] keybytes = getKeyBytes(k);
				Map<byte[], byte[]> hGetAll = connection.hGetAll(keybytes);
				if (hGetAll != null) {
					Iterator<Entry<byte[], byte[]>> iterator = hGetAll.entrySet().iterator();
					while (iterator.hasNext()) {
						Entry<byte[], byte[]> next = iterator.next();
						K key = bytesToMapKey(next.getKey());
						V obj = bytesToMapVaule(next.getValue());
						map.put(key, obj);
					}
				}
				return map;
			}
		});
	}

	@Override
	public  Map<K, V> getAllFromMap(K k) {
		return getAllFromMap(k, DEFAULT_DB_INDEX);
	}

	@Override
	public <T> T getByMapKey(K key, K mapElementKey, Class<T> elementType,Integer dbIndex) {
		return (T) bytesToMapVaule(getByMapKey(key, mapElementKey, dbIndex));
	}

	@Override
	public <T> T getByMapKey(K key, K mapElementKey, Class<T> elementType) {
		return   getByMapKey(key, mapElementKey, elementType, DEFAULT_DB_INDEX);
	}
	@Override
	public byte[] getByMapKey(final K key, final K mapElementKey, final Integer dbIndex) {
		return redisTemplate.execute(new RedisCallback<byte[]>() {
			@Override
			public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
				connection.select(dbIndex);
				byte[] keybytes = getKeyBytes(key);
				if (connection.exists(keybytes)) {
					List<byte[]> list = connection.hMGet(keybytes, getMapKeyBytes(mapElementKey));
					if (list != null && list.size() > 0 && list.get(0) != null) {
						return     list.get(0) ;
					}
					return null;
				}
				return null;
			}
		});
	}
	@Override
	public byte[] getByMapKey(K key, K mapElementKey) {
		return   getByMapKey(key, mapElementKey, DEFAULT_DB_INDEX);
	}
	@Override
	public Long getMapSize(final K mapkey) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				connection.select(DEFAULT_DB_INDEX);
				byte[] keybytes = getKeyBytes(mapkey);
				return connection.hLen(keybytes);
		 
			}
		});	}
	@Override
	public  void deleteByMapKey(final K key, final K mapElementKey, final Integer dbIndex) {
		redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.select(dbIndex);
				byte[] keybytes = getKeyBytes(key);
				byte[] mapkeybytes = getMapKeyBytes(mapElementKey);
				connection.hDel(keybytes, mapkeybytes);
				return null;
			}
		});
	}


	@Override
	public  void deleteByMapKey(K key, K mapElementKey) {
		deleteByMapKey(key, mapElementKey, DEFAULT_DB_INDEX);
	}

	@Override
	public boolean removeKey(K key, final Integer dbIndex) {
		boolean isSuccess = true;
		try {
			final byte[] rawKey = getKeyBytes(key);
			isSuccess = redisTemplate.execute(new RedisCallback<Boolean>() {
				@Override
				public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
					connection.select(dbIndex);
					Long result = connection.del(rawKey);
					return result > 0 ? true : false;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess = false;
		}
		return isSuccess;
	}

	@Override
	public boolean removeKey(K key) {
		return removeKey(key, DEFAULT_DB_INDEX);
	}



	@Override
	public List<Boolean> removeKeys(final String keysPattern, final Integer dbIndex) {
		return redisTemplate.execute(new RedisCallback<List<Boolean>>() {
			List<Boolean> rs = new ArrayList<Boolean>();
			@Override
			public List<Boolean> doInRedis(RedisConnection connection) throws DataAccessException {
				try {
					connection.select(dbIndex);
					Set<byte[]> keys = new HashSet<byte[]>(0);
					try {
						keys = connection.keys(keysPattern.getBytes("utf-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					for (byte[] b : keys) {
						Long r = null;
						rs.add((r = connection.del(b)) != null && r > 0);
						;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return rs;
			}
		});
	}
	@Override
	public List<Boolean> removeKeys(String keysPattern ) {
		 return removeKeys(keysPattern, DEFAULT_DB_INDEX);
	}
	@Override
	public List<K> keys(String keysPattern) {
		return keys(keysPattern, DEFAULT_DB_INDEX);
	}

	@Override
	public List<K> keys(final String keysPattern, final Integer dbIndex) {
		return redisTemplate.execute(new RedisCallback<List<K>>() {
			List<K> rs = new ArrayList<K>();

			@Override
			public List<K> doInRedis(RedisConnection connection) throws DataAccessException {
				connection.select(dbIndex);
				Set<byte[]> keys = new HashSet<byte[]>(0);
				try {
					keys = connection.keys(keysPattern.getBytes("utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				for (byte[] b : keys) {
					rs.add(bytesToK(b));
				}
				return rs;
			}
		});

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> doTransaction(final com.gzseeing.sys.redis.RedisDao.TransactionalTask task, final Integer dbIndex,
			final String... watch) {
		try {
			return (List<Object>) redisTemplate.execute(new RedisCallback<Object>() {
				@Override
				public Object doInRedis(RedisConnection connection) throws DataAccessException {
					RedisSerializer<String> stringSerializer = redisTemplate.getStringSerializer();
					connection.select(dbIndex);
					// 监视以下内容
					for (String string : watch) {
						byte[] keybytes = stringSerializer.serialize(string);
						connection.watch(keybytes);
					}
					connection.multi();
					task.execute(connection);
					return connection.exec();
				}
			});
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public List<Object> doTransaction(com.gzseeing.sys.redis.RedisDao.TransactionalTask task, String... watch) {
		return doTransaction(task,DEFAULT_DB_INDEX,watch);
	}
	//////-------------------------------------serialize start-----------------------------

	@Override
	public byte[] getKeyBytes(K k) {
		if (k == null) {
			return null;
		}
		if (k instanceof String) {
			return redisTemplate.getStringSerializer().serialize((String) k);
		}
		return SerializeUtil.serialize(k);
	}

	@Override
	public byte[] getValueBytes(V v) {
		if (v == null) {
			return null;
		}
		return SerializeUtil.serialize(v);
	}
	@Override
	public  byte[] getMapKeyBytes(K mapkey) {
		if (mapkey == null) {
			return null;
		}
		if (mapkey instanceof String) {
			return redisTemplate.getStringSerializer().serialize((String) mapkey);
		}
		return SerializeUtil.serialize(mapkey);
	}
	
	
	@Override
	public abstract V bytesToV(byte[] bs);

	@Override
	public abstract K bytesToK(byte[] bs);

	
	
	@Override
	public abstract  K bytesToMapKey(byte[] bs);

	@Override
	public abstract  V bytesToMapVaule(byte[] bs);

	


	@Override
	public  byte[] getMapValueBytes(V mapvalue) {
		if (mapvalue == null) {
			return null;
		}
		return SerializeUtil.serialize(mapvalue);
	}
	//////-------------------------------------serialize end-----------------------------
	
	protected byte[][] serializeCollection(Collection<V> values) {

		Assert.notEmpty(values, "Values must not be 'null' or empty.");
		Assert.noNullElements(values.toArray(), "Values must not contain 'null' value.");

		byte[][] rawValues = new byte[values.size()][];
		int i = 0;
		for (V v : values) {
			rawValues[i++] = getValueBytes(v);
		}

		return rawValues;
	}



	protected  Map<byte[], byte[]> serializeMap(Map<K, V> map) {
		Map<byte[], byte[]> ret = new LinkedHashMap<byte[], byte[]>(map.size());

		for (Map.Entry<K, V> entry : map.entrySet()) {
			ret.put(getMapKeyBytes(entry.getKey()), getMapValueBytes(entry.getValue()));
		}
		return ret;
	}

	@Override
	public RedisTemplate<Serializable, Serializable> getRedisTemplate() {
		return redisTemplate;
	}
	
	
}
