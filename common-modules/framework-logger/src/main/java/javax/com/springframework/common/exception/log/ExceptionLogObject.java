package javax.com.springframework.common.exception.log;

import com.springframework.log.log.JsonUtils;
import com.springframework.log.util.HostUtils;
import com.springframework.trace.Span;
import com.springframework.trace.agent.Tracer;
import org.slf4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author kainlin
 * @date 2015/10/16
 */
public class ExceptionLogObject {
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS Z";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
    private String url;
    private String logLevel;
    private String clientIp;
    private String dateTime;
    private String exception;
    private String callStack;
    private String exceptionMessage;
    private String request;
    private int httpCode;
    private int resultCode;
    private String resultMessage;
    private int internalErrorCode;
    private String internalErrorMessage;
    private String hostName;
    private String serverIp;
    private String componentName;
    private String guid = UUID.randomUUID().toString();
    private String customInfo;
    private String traceid;

    // web log
    public ExceptionLogObject(String url,
            String logLevel,
            String clientIp,
            Date dateTime,
            String exception,
            String callStack,
            String exceptionMessage,
            String request,
            int httpCode,
            int resultCode,
            String resultMessage,
            int internalErrorCode,
            String internalErrorMessage,
            String customInfo
    ) {
        this.url = url;
        this.logLevel = logLevel;
        this.clientIp = clientIp;
        this.dateTime = new SimpleDateFormat(FORMAT).format(dateTime);
        this.exception = exception;
        this.callStack = callStack;
        this.exceptionMessage = exceptionMessage;
        this.request = request;
        this.httpCode = httpCode;
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.internalErrorCode = internalErrorCode;
        this.internalErrorMessage = internalErrorMessage;
        this.hostName = HostUtils.getHostName();
        this.serverIp = HostUtils.getIp();
        this.componentName = HostUtils.getComponentName();
        this.customInfo = customInfo;
        //RequestContext reqContext = (RequestContext) RequestContext.getCurr().get();
        //this.traceid=reqContext.getReqId();
        setTracer();
    }

    // no web context log
    public ExceptionLogObject(String logLevel,
            Date dateTime,
            String exception,
            String callStack,
            String exceptionMessage,
            int internalErrorCode,
            String internalErrorMessage,
            String customInfo
    ) {
        this.url = null;
        this.logLevel = logLevel;
        this.clientIp = null;
        this.dateTime = new SimpleDateFormat(FORMAT).format(dateTime);
        this.exception = exception;
        this.callStack = callStack;
        this.exceptionMessage = exceptionMessage;
        this.request = null;
        this.httpCode = 0;
        this.resultCode = 0;
        this.resultMessage = null;
        this.internalErrorCode = internalErrorCode;
        this.internalErrorMessage = internalErrorMessage;
        this.hostName = HostUtils.getHostName();
        this.serverIp = HostUtils.getIp();
        this.componentName = HostUtils.getComponentName();
        this.customInfo = customInfo;
        //RequestContext reqContext = (RequestContext) RequestContext.getCurr().get();
        //this.traceid=reqContext.getReqId();
        setTracer();
    }

    // only for HostUtils
    public ExceptionLogObject(String logLevel,
            Date dateTime,
            String exception,
            String callStack,
            String exceptionMessage,
            int internalErrorCode,
            String internalErrorMessage,
            String hostName,
            String hostIp,
            String componentName,
            String customInfo
    ) {
        this.url = null;
        this.logLevel = logLevel;
        this.clientIp = null;
        this.dateTime = new SimpleDateFormat(FORMAT).format(dateTime);
        this.exception = exception;
        this.callStack = callStack;
        this.exceptionMessage = exceptionMessage;
        this.request = null;
        this.httpCode = 0;
        this.resultCode = 0;
        this.resultMessage = null;
        this.internalErrorCode = internalErrorCode;
        this.internalErrorMessage = internalErrorMessage;
        this.hostName = hostName;
        this.serverIp = hostIp;
        this.componentName = componentName;
        this.customInfo = customInfo;
        //RequestContext reqContext = (RequestContext) RequestContext.getCurr().get();
        //this.traceid=reqContext.getReqId();
        setTracer();
    }

    public void log(Logger logger) {
        logger.error(JsonUtils.writeObjectToJson(this));
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getCallStack() {
        return callStack;
    }

    public void setCallStack(String callStack) {
        this.callStack = callStack;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }


    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }


    public String getInternalErrorMessage() {
        return internalErrorMessage;
    }

    public void setInternalErrorMessage(String internalErrorMessage) {
        this.internalErrorMessage = internalErrorMessage;
    }

    public int getInternalErrorCode() {
        return internalErrorCode;
    }

    public void setInternalErrorCode(int internalErrorCode) {
        this.internalErrorCode = internalErrorCode;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getCustomInfo() {
        return customInfo;
    }

    public void setCustomInfo(String customInfo) {
        this.customInfo = customInfo;
    }

    public String getTraceid() {
        return traceid;
    }

    public void setTraceid(String traceid) {
        this.traceid = traceid;
    }

    private void setTracer() {
        Tracer tracer = Tracer.getTracer();
        if (tracer != null) {
            Span span = tracer.getParentSpan();
            if (span != null) {
                Long traceId = span.getTraceId();
                this.traceid = traceId == null ? null : traceId.toString();
            }
        }
    }


}
