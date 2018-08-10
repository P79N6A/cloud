package com.springframework.log.log;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * @author summer LoggerFactory
 */
public abstract class LoggerFactory {

    private static final ConcurrentMap<String, Logger> LOGGER_MAP = new ConcurrentHashMap<>();

    public static Logger getLogger(String name) {
        Logger logger = LOGGER_MAP.get(name);
        if (logger != null) {
            return logger;
        }
        logger = new DefaultLogger(org.slf4j.LoggerFactory.getLogger(name));
        Logger exist = LOGGER_MAP.putIfAbsent(name, logger);
        if (exist != null) {
            return exist;
        }
        return logger;
    }

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }
}
