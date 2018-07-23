package com.springframework.common.datasource;

/**
 * @author summer
 */
public class DynamicDataSourceHolders {

    public static final ThreadLocal<String> HOLDER = new ThreadLocal<>();

    public static void putDataSource(String name) {
        HOLDER.set(name);
    }

    public static String getDataSource() {
        return HOLDER.get();
    }
}
