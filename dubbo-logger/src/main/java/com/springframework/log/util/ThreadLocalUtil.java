package com.springframework.log.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author summer
 */
public final class ThreadLocalUtil {
    private static final ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal();

    public static <T> T get(String key) {
        Map<String, Object> map = (Map<String, Object>) threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        return (T) map.get(key);
    }

    public static <T> T get(String key, Class<T> tClass) {
        Map<String, Object> map = (Map<String, Object>) threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        return (T) map.get(key);
    }

    public static void set(String key, Object value) {
        Map<String, Object> map = (Map<String, Object>) threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            threadLocal.set(map);
        }
        map.put(key, value);
    }

    public static void removeAll() {
        threadLocal.remove();
    }

    public static <T> T remove(String key) {
        Map<String, Object> map = (Map<String, Object>) threadLocal.get();
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        return (T) map.remove(key);
    }

}
