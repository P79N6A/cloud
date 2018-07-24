package com.springframework.common.datasource.sharding;

import com.google.common.collect.Sets;
import com.springframework.common.datasource.utils.ReflectionUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author summer
 * 2018/7/24
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataSourceUtil {

    private static final String SET_METHOD_PREFIX = "set";
    private static final String SEPARATOR = "-";

    private static Collection<Class<?>> generalClassType;

    static {
        generalClassType = Sets.<Class<?>>newHashSet(boolean.class, Boolean.class, int.class, Integer.class, long.class, Long.class, String.class);
    }

    /**
     * Get data source.
     *
     * @param dataSourceClassName  Data source class name
     * @param dataSourceProperties Data source properties
     * @return data source instance
     * @throws ReflectiveOperationException reflective operation exception
     */
    public static DataSource getDataSource(final String dataSourceClassName, final Map<String, Object> dataSourceProperties) throws ReflectiveOperationException {
        DataSource result = (DataSource) Class.forName(dataSourceClassName).newInstance();
        for (Map.Entry<String, Object> entry : dataSourceProperties.entrySet()) {
            callSetterMethod(result, getSetterMethodName(entry.getKey()), null == entry.getValue() ? null : entry.getValue().toString());
        }
        return result;
    }

    private static String getSetterMethodName(final String propertyName) {
        StringBuilder sb = new StringBuilder();
        String property;
        if (propertyName.contains(SEPARATOR)) {
            final String[] strings = propertyName.split(SEPARATOR);
            for (String item : strings) {
                sb.append(String.valueOf(item.charAt(0)).toUpperCase() + item.substring(1, item.length()));
            }
            property = sb.toString();
        } else {
            property = propertyName;
        }
        return SET_METHOD_PREFIX + String.valueOf(property.charAt(0)).toUpperCase() + property.substring(1, property.length());
    }

    private static void callSetterMethod(final DataSource dataSource, final String methodName, final String setterValue) {
        for (Class<?> each : generalClassType) {
            try {
                Method method = ReflectionUtils.getDeclaredMethod(dataSource, methodName, each);
                if (null == method) {
                    continue;
                }
                if (boolean.class == each || Boolean.class == each) {
                    method.invoke(dataSource, Boolean.valueOf(setterValue));
                } else if (int.class == each || Integer.class == each) {
                    method.invoke(dataSource, Integer.parseInt(setterValue));
                } else if (long.class == each || Long.class == each) {
                    method.invoke(dataSource, Long.parseLong(setterValue));
                } else {
                    method.invoke(dataSource, setterValue);
                }
                return;
            } catch (final ReflectiveOperationException ex) {
            }
        }
    }

}

