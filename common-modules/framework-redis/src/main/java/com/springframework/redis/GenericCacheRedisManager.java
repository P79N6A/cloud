package com.springframework.redis;

import com.google.common.collect.Sets;
import com.springframework.cache.GenericCacheManager;
import com.springframework.enums.rediskey.CacheNamePrefixEnum;
import com.springframework.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author summer
 * 2018/7/12
 */
@Slf4j
public class GenericCacheRedisManager implements GenericCacheManager {
    private static final String MD5 = "md5";
    private static final int SIZE = 128;
    protected RedisTemplate redisTemplate;
    public GenericCacheRedisManager(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 生成key
     *
     * @param regionName
     * @param key
     * @return
     */
    private String getRealKey(String regionName, String key) {
        String result = regionName + key;
        if (result.length() > SIZE) {
            result = MD5 + StringUtil.md5(key);
        }
        return result;
    }

    @Override
    public Object get(String regionName, @NotNull String key) {
        final String realKey = getRealKey(regionName, key);
        return this.get(realKey);
    }

    @Override
    public Boolean set(String regionName, @NotNull String key, Object value) {
        final String realKey = getRealKey(regionName, key);
        return this.set(realKey, value);
    }

    @Override
    public Boolean remove(String regionName, @NotNull String key) {
        final String realKey = getRealKey(regionName, key);
        return this.del(realKey);
    }

    @Override
    public boolean isLocal() {
        return false;
    }

//##################   redis ###################

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     */
    public Boolean expire(@NotNull String key, long time) {
        try {
            return redisTemplate.expire(key, time, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("指定缓存失效时间执行出错,key{},time{}", key, time, e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public Long getExpire(@NotNull String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(@NotNull String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("判断key是否存在执行出错,key{}", key, e);
            return false;
        }
    }

    /**
     * 删除缓存
     */
    public Long del(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    public Boolean del(@NotNull String key) {
        return redisTemplate.delete(key);
    }

    //============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(@NotNull String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public Boolean set(@NotNull String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("普通缓存放入执行出错,key{},value{}", key, value, e);
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public Boolean set(@NotNull String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("普通缓存放入执行出错,key{},value{},time{}", key, value, time, e);
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public Long incr(@NotNull String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public Long decr(@NotNull String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    //================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(@NotNull String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(@NotNull String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public Boolean hmset(@NotNull String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("缓存HashSet执行出错,key{},map{}", key, map, e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public Boolean hmset(@NotNull String key, Map<String, Object> map, long time) {
        try {
            //TODO 无法保证原子性，优化使用luna脚本
            if (time > 0) {
                redisTemplate.opsForHash().putAll(key, map);
                expire(key, time);
                //luna脚本优化方案
                /*RedisScript<Void> luaScript = new DefaultRedisScript<>("", Void.class);
                redisTemplate.execute(luaScript, Lists.newArrayList(key));*/
            } else {
                redisTemplate.opsForHash().putAll(key, map);
            }
            return true;
        } catch (Exception e) {
            log.error("缓存HashSet执行出错,key{},map{}", key, map, e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public Boolean hset(@NotNull String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("缓存HashSet执行出错,key{},ite{},value{}", key, item, value, e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public Boolean hset(@NotNull String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("缓存HashSet执行出错,key{},ite{},value{}", key, item, value, e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public Long hdel(@NotNull String key, Object... item) {
        return redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public Boolean hHasKey(@NotNull String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public Double hincr(@NotNull String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public Double hdecr(@NotNull String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    //============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(@NotNull String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("根据key获取Set中的所有值执行出错,key{}", key, e);
            return Sets.newHashSet();
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public Boolean sHasKey(@NotNull String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error("根据value从一个set中查询,是否存在执行出错,key{}，value{}", key, value, e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long sSet(@NotNull String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("将数据放入set缓存执行出错,key{}，values{}", key, values, e);
            return 0L;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long sSetAndTime(@NotNull String key, long time, Object... values) {
        try {
            Long count;
            if (time > 0) {
                //TODO 需要luna优化保证原子性
                count = redisTemplate.opsForSet().add(key, values);
                expire(key, time);
            } else {
                count = redisTemplate.opsForSet().add(key, values);
            }
            return count;
        } catch (Exception e) {
            log.error("将set数据放入缓存执行出错,key{}，values{}，time{}", key, values, time, e);
            return 0L;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public Long sGetSetSize(@NotNull String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error("获取set缓存的长度执行出错,key{}", key, e);
            return 0L;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Long setRemove(@NotNull String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            log.error("移除值为value的执行出错,key{},values{}", key, values, e);
            return 0L;
        }
    }
    //===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(@NotNull String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("移除值为value的执行出错,key{},start{},end{}", key, start, end, e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public Long lGetListSize(@NotNull String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("获取list缓存的长度执行出错,key{}", key, e);
            return 0L;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(@NotNull String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("通过索引 获取list中的值执行出错,key{},index{}", key, index, e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public Long lSet(@NotNull String key, Object value) {
        try {
            return redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            log.error("将list放入缓存执行出错,key{},value", key, value, e);
            return 0L;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public Long lSet(@NotNull String key, Object value, long time) {
        try {
            Long rightPush;
            if (time > 0) {
                //TODO 需要luna优化保证原子性
                rightPush = redisTemplate.opsForList().rightPush(key, value);
                expire(key, time);
            } else {
                rightPush = redisTemplate.opsForList().rightPush(key, value);
            }
            return rightPush;
        } catch (Exception e) {
            log.error("获取list缓存的长度执行出错,key{}", key, e);
            return 0L;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public Long lSet(@NotNull String key, List<Object> value) {
        try {
            return redisTemplate.opsForList().rightPushAll(key, value);
        } catch (Exception e) {
            log.error("将list放入缓存(rightPushAll)执行出错,key{},value{}", key, value, e);
            return 0L;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public Long lSet(@NotNull String key, List<Object> value, long time) {
        try {
            Long rightPushAll;
            if (time > 0) {
                rightPushAll = redisTemplate.opsForList().rightPushAll(key, value);
                expire(key, time);
            } else {
                rightPushAll = redisTemplate.opsForList().rightPushAll(key, value);
            }
            return rightPushAll;
        } catch (Exception e) {
            log.error("获取list缓存的长度执行出错,key{}", key, e);
            return 0L;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public Boolean lUpdateIndex(@NotNull String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("根据索引修改list中的某条数据执行出错,key{},index{},value{}", key, index, value, e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public Long lRemove(@NotNull String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            log.error("移除N个值为value执行出错,key{},count{},value{}", key, count, value, e);
            return 0L;
        }
    }

    /**
     * 缓存key 前缀
     */
    @Override
    public CacheNamePrefixEnum registerCacheName() {
        return null;
    }
}
