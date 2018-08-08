package com.springframework.http.utils;

import java.io.InputStream;
import java.util.Map;

/**
 * @author summer
 */
public interface RequestCallback {
    boolean processResult(InputStream stream, Map<String, String> resHeader);
}
