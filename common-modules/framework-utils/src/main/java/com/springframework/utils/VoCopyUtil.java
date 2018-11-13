package com.springframework.utils;

import com.springframework.utils.bytecode.Wrapper;
import com.springframework.utils.core.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.collections.list.UnmodifiableList;
import org.apache.commons.lang.ArrayUtils;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * @author summer
 */
@Slf4j
public class VoCopyUtil {
    /* srcClass+dstClass*/
    private static Map<String, List<String>> copyPropsMap = new FastHashMap();

    private static <K, V> Map<K, V> toVoMap(Map<K, V> map) {
        Map result = new HashMap(map.size());
        result.putAll(map);
        return result;
    }

    public static <K, V> List<Map<K, V>> toVoMapList(List<Map<K, V>> mapList) {
        List<Map<K, V>> result = new ArrayList<>(mapList.size());
        for (Map<K, V> map : mapList) {
            result.add(toVoMap(map));
        }
        return result;
    }

    public static <S, T> ResultCode<List<S>> copyListProperties(Class<S> clazz, List<T> itemList) {
        List<S> voItemList = new ArrayList<S>();
        if (CollectionUtils.isEmpty(itemList)) {
            return ResultCode.getSuccessReturn(voItemList);
        }
        try {
            for (T item : itemList) {
                if (item == null) {
                    continue;
                }
                Class src = item.getClass();
                Wrapper srcWrapper = Wrapper.getWrapper(src);
                Wrapper destWrapper = Wrapper.getWrapper(clazz);
                S itemVo = (S) destWrapper.gainNewInstance();
                List<String> props = getJoinProperties(srcWrapper.getReadPropertyNames(), src, clazz);
                copyInternal(itemVo, item, srcWrapper, destWrapper, props);
                voItemList.add(itemVo);
            }
        } catch (Exception e) {
            log.warn("", e);
        }
        return ResultCode.getSuccessReturn(voItemList);
    }

    public static <S, T> ResultCode<S> copyProperties(Class<S> clazz, T item) {
        if (item == null) {
            return ResultCode.getFailure(ResultCode.CODE_DATA_ERROR, "数据不存在！");
        }
        try {
            Wrapper srcWrapper = Wrapper.getWrapper(item.getClass());
            Wrapper destWrapper = Wrapper.getWrapper(clazz);
            S itemVo = (S) destWrapper.gainNewInstance();
            List<String> props = getJoinProperties(srcWrapper.getReadPropertyNames(), item.getClass(), clazz);
            copyInternal(itemVo, item, srcWrapper, destWrapper, props);
            return ResultCode.getSuccessReturn(itemVo);
        } catch (Exception e) {
            log.warn("", e);
            return ResultCode.getFailure(ResultCode.CODE_UNKNOWN_ERROR, "未知错误！");
        }
    }

    private static List<String> getJoinProperties(String[] srcProps, Class src, Class dest) {
        String key = src.getCanonicalName() + "2" + dest.getCanonicalName();
        List<String> propList = copyPropsMap.get(key);
        if (propList == null) {
            propList = new ArrayList<String>();
            synchronized (src) {
                for (String name : srcProps) {
                    try {
                        PropertyDescriptor descriptor = new PropertyDescriptor(name, dest);
                        if (PropertyUtils.getWriteMethod(descriptor) != null) {
                            propList.add(name);
                        }
                    } catch (Exception e) {
                        //ignore e.printStackTrace();
                    }
                }
                copyPropsMap.put(key, UnmodifiableList.decorate(propList)/*不允许变更*/);
            }
        }
        return propList;
    }

    /**
     * 两个Bean之前简单属性copy
     * 限制：1）dst,src不能是Map，只能是POJO  2）无法级联
     *
     * @param dst
     * @param src
     */
    public static void copy(Object dst, Object src) {
        Wrapper srcWrapper = Wrapper.getWrapper(src.getClass());
        Wrapper destWrapper = Wrapper.getWrapper(dst.getClass());
        List<String> props = getJoinProperties(srcWrapper.getReadPropertyNames(), src.getClass(), dst.getClass());
        copyInternal(dst, src, srcWrapper, destWrapper, props);

    }

    public static void copy(Object dst, Object src, boolean allow, String... fields) {
        if (ArrayUtils.isEmpty(fields)) {
            copy(dst, src);
        } else {
            Wrapper srcWrapper = Wrapper.getWrapper(src.getClass());
            Wrapper destWrapper = Wrapper.getWrapper(dst.getClass());
            List<String> copyProps = Arrays.asList(fields);
            if (!allow) {
                List<String> props = getJoinProperties(srcWrapper.getReadPropertyNames(), src.getClass(), dst.getClass());
                List<String> remove = copyProps;

                copyProps = new ArrayList<String>(props);
                copyProps.removeAll(remove);
            }
            copyInternal(dst, src, srcWrapper, destWrapper, copyProps);
        }
    }

    private static void copyInternal(Object dst, Object src, Wrapper srcWrapper, Wrapper destWrapper, List<String> props) {
        for (String name : props) {
            try {
                destWrapper.setPropertyValue(dst, name, srcWrapper.getPropertyValue(src, name));
            } catch (Throwable e) {
                log.warn(name, e, 10);
            }
        }
    }

    /**
     * 目前只支持简单对象copy</br>
     * 参数beanmap，key是对象的属性，value是属性对应的值
     *
     * @param dst
     * @param beanmap
     * @author leo
     * @addTime 2016年4月27日下午6:35:58
     */
    public static void copy(Object dst, Map<String, Object> beanmap) {
        Wrapper destWrapper = Wrapper.getWrapper(dst.getClass());
        copyInternal(dst, destWrapper, beanmap);
    }

    private static void copyInternal(Object dst, Wrapper destWrapper, Map<String, Object> beanmap) {
        for (Map.Entry<String, Object> entry : beanmap.entrySet()) {
            try {
                destWrapper.setPropertyValue(dst, entry.getKey(), entry.getValue());
            } catch (Throwable e) {
                log.warn(entry.getKey(), e, 10);
            }
        }
    }
}
