package com.gzseeing.sys.redis;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

public interface RedisDao<K, V> {

	public final static Integer DEFAULT_DB_INDEX = 0;

	V get(K key);

	V get(K key, Integer dbIndex);

	void put(K k, V v, Long seconds, Integer dbIndex);

	void put(K k, V v, Long seconds);

	// -------------------------list
	// start-------------------------------------------

	void leftPushAll(K v, Collection<V> vs, Long seconds);

	void leftPushAll(K v, Collection<V> vs, Long seconds, Integer dbIndex);

	void rightPushAll(K key, Collection<V> vs, Long seconds, Integer dbIndex);

	void rightPushAll(K key, Collection<V> vs, Long seconds);

	V leftPop(K key, Integer dbIndex);

	V leftPop(K key);

	V rightPop(K key, Integer dbIndex);

	V rightPop(K key);

	void rightPush(K key, V value, Long seconds, Integer dbIndex);

	void rightPush(K key, V value, Long seconds);

	void leftPush(K key, V value, Long seconds, Integer dbIndex);

	void leftPush(K key, String value, Long seconds);

	// -------------------------list
	// end-------------------------------------------

	// --------------------------------------fundamental
	// start-----------------------------------

	boolean exist(K key, Integer dbIndex);

	boolean exist(K key);

	boolean removeKey(K key, Integer dbIndex);

	boolean removeKey(K key);

	List<Boolean> removeKeys(String keysPattern, Integer dbIndex);

	List<Boolean> removeKeys(String keysPattern);

	List<K> keys(String keysPattern, Integer dbIndex);

	List<K> keys(String keysPattern);

	boolean setTimeOut(K key, Long seconds, Integer dbIndex);

	boolean setTimeOut(K key, Long seconds);

	// --------------------------------------fundamental
	// end-----------------------------------

	/**
	 * -2表示不存在 -3 网络中断
	 * 
	 * @author haowen
	 * @time 2018年7月20日下午2:21:17
	 * @Description
	 * @param key
	 * @param dbIndex
	 * @return
	 */
	Long getTimeOut(K key, Integer dbIndex);

	Long getTimeOut(K key);

	// --------------------------------------------------------------------MAP
	// start-----------------------------
	void putMap(K key, Map<K, V> map, Long seconds, Integer dbIndex);

	void putMap(K key, Map<K, V> map, Long seconds);

	/**
	 * 
	 * @author haowen
	 * @time 2018年3月15日上午11:57:55
	 * @Description
	 * @param key
	 * @param mapkey
	 * @param value2
	 * @param dbIndex
	 * @param TimeOut
	 */
	void saveValueInMap(K key, K mapkey, V mapValue, Long seconds, Integer dbIndex);

	void saveValueInMap(K key, K mapkey, V mapValue, Long seconds);

	/**
	 * 一次拿出多个MapKeys
	 * 
	 * @author haowen
	 * @param <MapValue>
	 * @time 2017年11月27日下午3:48:15
	 * @Description
	 * @param key
	 * @param mapElementKeys
	 * @param dbIndex
	 * @return
	 */
	Map<K, V> getByMapKeys(K key, K[] mapElementKeys, Integer dbIndex);

	Map<K, V> getByMapKeys(K key, K[] mapElementKeys);

	/**
	 * 从Map中获取所有数据
	 * 
	 * @author haowen
	 * @time 2017年11月28日下午5:12:18
	 * @Description
	 * @param hasMapKey
	 * @param dbIndex
	 * @return
	 */
	Map<K, V> getAllFromMap(K k, Integer dbIndex);

	Map<K, V> getAllFromMap(K k);

	/**
	 * 从Map中拿出key的内容
	 * 
	 * @author haowen
	 * @Description
	 * @param key
	 *            redis的key
	 * @param mapElementKey
	 *            map的key
	 * @param elementType
	 *            map的value
	 * @param dbIndex
	 * @return
	 */
	<T> T getByMapKey(K key, K mapElementKey, Class<T> elementType, Integer dbIndex);

	<T> T getByMapKey(K key, K mapElementKey, Class<T> elementType);
	 byte[] getByMapKey(K key, K mapElementKey , Integer dbIndex);

	 byte[]  getByMapKey(K key, K mapElementKey );

	/**
	 * 删除Map的key
	 * 
	 * @author haowen
	 * @Description
	 * @param key
	 * @param mapElementKey
	 * @param dbIndex
	 */
	void deleteByMapKey(K key, K mapElementKey, Integer dbIndex);

	void deleteByMapKey(K key, K mapElementKey);

	Long getMapSize(K mapkey);
	// --------------------------------------------------------------------MAP
	// end-----------------------------
	/**
	 * 自定义方法接口
	 * 
	 * @Description
	 * @author haowen
	 * @version 1.0.0 2017年8月7日 下午3:45:10
	 * @see [相关类/方法]
	 * @since [产品/模块版本]
	 */
	public interface TransactionalTask {
		public void execute(RedisConnection connection);
	}

	/**
	 * 返回null表示事务失败。 有回滚作用
	 * 
	 * @author haowen
	 * @Description
	 * @param task
	 * @param dbIndex
	 * @param watch
	 * @return
	 */
	List<Object> doTransaction(TransactionalTask task, Integer dbIndex, String... watch);

	List<Object> doTransaction(TransactionalTask task, String... watch);

	/**
	 * key的处理办法,一般是变成RedisString保存，不是对象保存；其他的对象字节保存
	 * 
	 * @author haowen
	 * @time 2018年7月20日下午2:27:31
	 * @override 特别需求请重写
	 * @Description 
	 * @Warn 不建议对不同类特别处理，因为在反序列化无法得知如何处理。
	 * @param k
	 * @return
	 */
	byte[] getKeyBytes(K k);

	/**
	 * value处理方法
	 * 
	 * @author haowen
	 * @time 2018年7月20日下午2:27:17
	 * @override 特别需求请重写
	 * @Description
	 * @Warn 不建议对不同类特别处理，因为在反序列化无法得知如何处理。
	 * @param v
	 * @return
	 */
	byte[] getValueBytes(V v);


	/**
	 * java中的map的key序列化，一般是默认变成RedisString保存，不是对象保存；其他的对象字节保存（尽量避免）
	 * <br>
	 * @author haowen
	 * @time 2018年7月21日下午2:24:50
	 * @Description  
	 * @Warn 不建议对不同类特别处理，因为在反序列化无法得知如何处理。
	 * @param mapkey
	 * @return
	 */
	byte[] getMapKeyBytes(K mapkey);

	/**
	 * javaMap中的value反序列化方法，
	 * @author haowen
	 * @time 2018年7月21日下午2:24:25
	 * @Description 
	 * @Warn 不建议对不同类特别处理，因为在反序列化无法得知如何处理。
	 * @param mapvalue
	 * @return
	 */
	byte[] getMapValueBytes(V mapvalue);

	/**
	 *  redis中map的key反序列化，一般是默认变成RedisString保存，不是对象保存；其他的对象字节保存（尽量避免）
	 * @author haowen
	 * @time 2018年7月21日下午2:23:41
	 * @Description  
	 * @param bs
	 * @return
	 */
	K bytesToMapKey(byte[] bs);

	/**
	 * redisMap里面的数据反序列化，
	 * @author haowen
	 * @time 2018年7月21日下午2:23:18
	 * @Description 
	 * @param bs
	 * @return
	 */
	V bytesToMapVaule(byte[] bs);

	/**
	 * redis的Key数据反序列化，一般是按字符反序列化
	 * @author haowen
	 * @time 2018年7月21日下午2:22:45
	 * @Description  
	 * @param bs
	 * @return
	 */
	K bytesToK(byte[] bs);
	/**
	 * redis里面的value反序列化
	 * 
	 * @author haowen
	 * @time 2018年7月20日下午2:28:04
	 * @Description
	 * @param bs
	 * @return
	 */
	V bytesToV(byte[] bs);

	RedisTemplate<Serializable, Serializable> getRedisTemplate();

}
