package com.springframework.log.log;
import com.google.gson.Gson;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.core.util.JsonUtils;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author summer
 */
public class LogMessage implements Serializable{
	private static final long serialVersionUID = -1449397931879372657L;
	private Map data = new LinkedHashMap();
	private Gson gson = new Gson();
	public LogMessage(String type, String server, String systemId, Map paramsdata) {
		init(type, server, systemId);
		data.put("message", paramsdata);
	}
	public LogMessage(String type, String server, String systemId, String message) {
		init(type, server, systemId);
		data.put("message", message);
	}
	public LogMessage(String type, String server, String systemId, String message, Throwable t) {
		init(type, server, systemId);
		data.put("message", message);
		data.put("exception", t.getClass().getCanonicalName());
		data.put("exceptionTrace", ExceptionUtils.getStackTrace(t));
	}
	private void init(String type, String server, String systemId){
		if(type!=null){
			data.put("type", type);
		}
		data.put("server", server);
		data.put("systemid", systemId);
	}

	public String getDataStr() {
		return gson.toJson(this.data);
	}
	public Map getDataMap() {
		return this.data;
	}
}
