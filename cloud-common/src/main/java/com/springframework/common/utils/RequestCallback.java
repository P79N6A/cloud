package com.springframework.common.utils;

import java.io.InputStream;
import java.util.Map;

/**
 * @author summer
 */
public interface RequestCallback {
	public abstract boolean processResult(InputStream stream, Map<String, String> resHeader);
}
