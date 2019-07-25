package com.gzseeing.sys.shiro.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gzseeing.sys.redis.RedisDao;
import com.gzseeing.sys.shiro.ShiroConfig;
import com.gzseeing.sys.varriable.SysConfig;

// only support string
/**
 * 这个自己写的，不用了
 * 
 * @author haowen
 *
 * @param <K>
 * @param <V>
 */
@Deprecated
public class MyRedisCache<K, V> implements Cache<K, V> {
    private Logger logger = LoggerFactory.getLogger(super.getClass());
    private RedisDao<String, Object> cache;
    private Long expire = null;

    private String cacheName;

    public MyRedisCache(RedisDao cache, String cacheName) {
        if (cache == null) {
            throw new IllegalArgumentException("Cache argument cannot be null.");
        }
        this.cache = cache;
        this.cacheName = cacheName;
        expire = SysConfig.CONFIG_APP_SESSION_TIMEOUT_INT * 1L;
    }

    private byte[] getKeyByte(K key) {
        if (key instanceof String) {
            return ((String)key).getBytes();
        }
        return serialize(key);
    }

    @Override
    public V get(K key) throws CacheException {
        try {
            if (key == null) {
                return null;
            }
            // bug need to fix,redisDao support string
            byte[] rawValue = this.cache.getByMapKey(cacheName, (String)key);

            Object value = deserialize(rawValue);
            return (V)value;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public V put(K key, V value) throws CacheException {
        this.logger.debug("根据key从存储 key [" + key + "]");
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(ShiroConfig.MAP_SHIRO_KEY, value);
            this.cache.saveValueInMap(cacheName, (String)key, value, expire);
            return value;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public V remove(K key) throws CacheException {
        this.logger.debug("从redis中删除 key [" + key + "]");
        try {
            Object previous = get(key);
            this.cache.deleteByMapKey((String)key, ShiroConfig.MAP_SHIRO_KEY);
            return (V)previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public void clear() throws CacheException {
        this.logger.debug("从redis中删除所有元素");
        try {
            this.cache.removeKeys(cacheName);
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public int size() {
        try {
            return this.cache.getMapSize(cacheName).intValue();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public Set<K> keys() {
        try {
            Map<String, Object> allFromMap = this.cache.getAllFromMap(cacheName);
            if (CollectionUtils.isEmpty(allFromMap)) {
                return Collections.emptySet();
            }

            return (Set<K>)allFromMap.keySet();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public Collection<V> values() {
        try {
            Map<String, Object> allFromMap = this.cache.getAllFromMap(cacheName);
            if (CollectionUtils.isEmpty(allFromMap)) {
                return Collections.emptySet();
            }

            return (Collection<V>)allFromMap.values();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    public Object deserialize(byte[] bytes) {
        Object result = null;

        if (isEmpty(bytes)) {
            return null;
        }
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);
                try {
                    result = objectInputStream.readObject();
                } catch (ClassNotFoundException ex) {
                    throw new Exception("Failed to deserialize object type", ex);
                }
            } catch (Throwable ex) {
                throw new Exception("Failed to deserialize", ex);
            }
        } catch (Exception e) {
            this.logger.error("Failed to deserialize", e);
        }
        return result;
    }

    private boolean isEmpty(byte[] data) {
        return ((data == null) || (data.length == 0));
    }

    public byte[] serialize(Object object) {
        byte[] result = null;

        if (object == null)
            return new byte[0];
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream(128);
            try {
                if (!(object instanceof Serializable)) {
                    throw new IllegalArgumentException(
                        super.getClass().getSimpleName() + " requires a Serializable payload "
                            + "but received an object of type [" + object.getClass().getName() + "]");
                }

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
                objectOutputStream.writeObject(object);
                objectOutputStream.flush();
                result = byteStream.toByteArray();
            } catch (Throwable ex) {
                throw new Exception("Failed to serialize", ex);
            }
        } catch (Exception ex) {
            this.logger.error("Failed to serialize", ex);
        }
        return result;
    }
}