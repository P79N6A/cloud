package com.springframework.domain.base.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @function BeanUtil工具类针对对象的属性进行set/get 等操作
 * @author summer
 */
@Slf4j
public class BeanUtil {
  private static Map<Class, List<String>> writeListPropertyMap =
      new ConcurrentHashMap<Class, List<String>>();

  public static void copyProperties(Object dest, Object orig) {
    try {
      PropertyUtils.copyProperties(dest, orig);
    } catch (Exception e) {
      // ignore
    }
  }
  /**
   * array,collection,map
   *
   * @param
   * @return <code>true</code> if the List/array/map is null or empty.
   */
  public static final boolean isEmptyContainer(Object container) {
    if (container == null) {
      return true;
    }
    if (container.getClass().isArray()) {
      // Thanks to Eric Fixler for this refactor.
      return Array.getLength(container) == 0;
    }
    if (container instanceof Collection) {
      return ((Collection) container).size() == 0;
    }
    if (container instanceof Map) {
      return ((Map) container).size() == 0;
    }
    return false;
  }

  public static void set(Object object, String property, Object newproperty) {
    try {
      PropertyUtils.setProperty(object, property, newproperty);
    } catch (IllegalAccessException e) {
    } catch (InvocationTargetException e) {
    } catch (Exception e) {
    }
  }

  public static List<Long> getNotNullId(final List<Long> idList) {
    List<Long> result = new ArrayList<Long>();
    if (idList == null || idList.isEmpty()) {
      return result;
    }
    for (Long id : idList) {
      if (id != null) {
        result.add(id);
      }
    }
    return result;
  }

  public static List<Long> getIdList(final String idListStr, String spliter) {
    List<Long> result = new ArrayList<Long>();
    if (StringUtils.isEmpty(idListStr)) {
      return result;
    }
    String[] idList = idListStr.split(spliter);
    for (String idStr : idList) {
      try {
        long id = Long.parseLong(idStr.trim());
        result.add(id);
      } catch (Exception e) {
      }
    }
    return result;
  }

  public static List<Integer> getIntgerList(final String idListStr, String spliter) {
    List<Integer> result = new ArrayList<Integer>();
    if (StringUtils.isEmpty(idListStr)) {
      return result;
    }
    String[] idList = idListStr.split(spliter);
    for (String idStr : idList) {
      try {
        int id = Integer.parseInt(idStr.trim());
        result.add(id);
      } catch (Exception e) {
      }
    }
    return result;
  }

  /**
   * 获取beanList中的所有properties
   *
   * @param beanList
   * @param properties
   * @return
   */
  public static List<Map> getBeanMapList(final Collection beanList, String... properties) {
    List<Map> result = new ArrayList<Map>();
    if (beanList == null) {
      return result;
    }
    Iterator it = beanList.iterator();
    Object bean = null;
    while (it.hasNext()) {
      bean = it.next();
      Map beanMap = new HashMap();
      boolean hasProperty = false;
      for (String property : properties) {
        try {
          Object pv = null;
          String keyname = property;
          if (property.indexOf(".") > 0) { // 复合属性
            pv = PropertyUtils.getNestedProperty(bean, property);
            keyname = keyname.replace('.', '_');
          } else {
            pv = PropertyUtils.getProperty(bean, property);
          }
          if (pv != null) {
            hasProperty = true;
            beanMap.put(keyname, pv);
          }
        } catch (Exception e) {
        }
      }
      if (hasProperty) {
        result.add(beanMap);
      }
    }
    return result;
  }

  private static String buildString(Map map) {
    StringBuilder result = new StringBuilder();
    for (Object key : map.keySet()) {
      result.append("," + key + "=");
      if (map.get(key) instanceof Collection) {
        result.append("[");
        Collection vlist = (Collection) map.get(key);
        for (Object el : vlist) {
          if (el == null) {
            continue;
          }
          if (isSimpleProperty(el.getClass())) {
            result.append(el);
          } else if (el instanceof Map) {
            result.append(buildString((Map) el));
          }
          result.append(",");
        }
        if (vlist.size() > 0) {
          result.deleteCharAt(result.length() - 1);
        }
        result.append("]");
      } else if (map.get(key) instanceof Map) {
        result.append("{" + buildString((Map) map.get(key)) + "}");
      } else {
        result.append(map.get(key));
      }
    }
    if (result.length() > 0) {
      result.deleteCharAt(0);
    }
    return result.toString();
  }

  /**
   * 获取beanList中以keyproperty值为key，valueProperty值为value的Map
   *
   * @param beanList
   * @param keyProperty
   * @param valueProperty
   * @return
   */
  public static Map getKeyValuePairMap(List beanList, String keyProperty, String valueProperty) {
    Map result = new HashMap();
    for (Object bean : beanList) {
      try {
        Object keyvalue = PropertyUtils.getProperty(bean, keyProperty);
        if (keyvalue != null) {
          result.put(keyvalue, PropertyUtils.getProperty(bean, valueProperty));
        }
      } catch (Exception e) { // ignore
      }
    }
    return result;
  }

  public static <T> List<T> getSubList(List<T> list, int from, int maxnum) {
    if (list == null || list.size() <= from) {
      return new ArrayList<T>();
    }
    return new ArrayList(list.subList(from, Math.min(from + maxnum, list.size())));
  }

  /**
   * 将longList分成多个List，每个List的长度为length，最后一个可能不足
   *
   * @param longList
   * @param length
   * @return
   */
  public static <T> List<List<T>> partition(List<T> longList, int length) {
    List<List<T>> result = new ArrayList<List<T>>();
    if (longList == null || longList.isEmpty()) {
      return result;
    }
    if (longList.size() <= length) {
      result.add(longList);
    } else {
      int groups = (longList.size() - 1) / length + 1;
      for (int i = 0; i < groups - 1; i++) {
        result.add(new ArrayList(longList.subList(length * i, length * (i + 1))));
      }
      result.add(new ArrayList(longList.subList(length * (groups - 1), longList.size())));
    }
    return result;
  }

  // ~~~~~~~~~~~~~~~~copy from spring for reduce dependency~~~~~~~~~~~~~~~~~~~~~~~~~~
  /**
   * Check if the given type represents a "simple" property: a primitive, a String or other
   * CharSequence, a Number, a Date, a URI, a URL, a Locale, a Class, or a corresponding array.
   *
   * <p>Used to determine properties to check for a "simple" dependency-check.
   *
   * @param clazz the type to check
   * @return whether the given type represents a "simple" property
   * @see org.springframework.beans.factory.support.RootBeanDefinition#DEPENDENCY_CHECK_SIMPLE
   * @see
   *     org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#checkDependencies
   */
  public static boolean isSimpleProperty(Class<?> clazz) {
    Assert.notNull(clazz, "Class must not be null");
    return isSimpleValueType(clazz)
        || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
  }

  /**
   * Check if the given type represents a "simple" value type: a primitive, a String or other
   * CharSequence, a Number, a Date, a URI, a URL, a Locale or a Class.
   *
   * @param clazz the type to check
   * @return whether the given type represents a "simple" value type
   */
  public static boolean isSimpleValueType(Class<?> clazz) {
    return ClassUtils.isPrimitiveOrWrapper(clazz)
        || clazz.isEnum()
        || CharSequence.class.isAssignableFrom(clazz)
        || Number.class.isAssignableFrom(clazz)
        || Date.class.isAssignableFrom(clazz)
        || clazz.equals(URI.class)
        || clazz.equals(URL.class)
        || clazz.equals(Locale.class)
        || clazz.equals(Class.class);
  }

  public static String getIdentityHashCode(Object object) {
    return "0x" + Integer.toHexString(System.identityHashCode(object));
  }

  /**
   * Sorts map by values in ascending order.
   *
   * @param <K> map keys type
   * @param <V> map values type
   * @param map
   * @return
   */
  public static <K, V extends Comparable<V>> LinkedHashMap<K, V> sortMapByValue(
      Map<K, V> map, boolean asc) {
    List<Entry<K, V>> sortedEntries = sortEntriesByValue(map.entrySet(), asc);
    LinkedHashMap<K, V> sortedMap = new LinkedHashMap<K, V>(map.size());
    for (Entry<K, V> entry : sortedEntries) {
      sortedMap.put(entry.getKey(), entry.getValue());
    }

    return sortedMap;
  }

  /**
   * Sorts map entries by value in ascending order.
   *
   * @param <K> map keys type
   * @param <V> map values type
   * @param entries
   * @return
   */
  private static <K, V extends Comparable<V>> List<Entry<K, V>> sortEntriesByValue(
      Set<Entry<K, V>> entries, boolean asc) {
    List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(entries);
    Collections.sort(sortedEntries, new ValueComparator<V>(asc));
    return sortedEntries;
  }

  /**
   * Komparator podla hodnot v polozkach mapy.
   *
   * @param <V> typ hodnot
   */
  private static class ValueComparator<V extends Comparable<V>> implements Comparator<Entry<?, V>> {
    private boolean asc;

    public ValueComparator(boolean asc) {
      this.asc = asc;
    }

    @Override
    public int compare(Entry<?, V> entry1, Entry<?, V> entry2) {
      int result = entry1.getValue().compareTo(entry2.getValue());
      return asc ? result : -result;
    }
  }

  public static <T> List<T> getEmptyList(Class<T> clazz) {
    return new ArrayList<T>();
  }
}
