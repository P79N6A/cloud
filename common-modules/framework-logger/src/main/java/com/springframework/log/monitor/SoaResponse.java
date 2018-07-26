package com.springframework.log.monitor;

import java.io.Serializable;
import java.util.UUID;


/**
 * <li>通用SOA响应处理对象</li>
 *
 * @author summer
 **/
public class SoaResponse<T, ErrT> implements MonitorTrack, Serializable {
    private final static String RETURN_SUCCESS = "000000";
    private static final long serialVersionUID = 889695893318362669L;
    /**
     * 通用返回码
     */
    private String returnCode = RETURN_SUCCESS;
    /**
     * 通用返回
     */
    private String returnMsg;
    /**
     * 通用业务信息
     */
    private String logBizData;

    private Boolean processResult = true;

    /**
     * 通用成功响应对象
     */
    private T responseVo;

    private ErrT errT;

    private String monitorTrackId = UUID.randomUUID().toString();

    /**
     * 时间戳(毫秒)
     */
    private String timestamp = System.currentTimeMillis() + "";

    private String globalTicket;

    public Boolean getProcessResult() {
        return processResult;
    }

    public void setProcessResult(Boolean processResult) {
        this.processResult = processResult;
    }

    public T getResponseVo() {
        return responseVo;
    }

    public void setResponseVo(T responseVo) {
        this.responseVo = responseVo;
    }

    public ErrT getErrT() {
        return errT;
    }

    public void setErrT(ErrT errT) {
        this.errT = errT;
    }

    @Override
    public String getMonitorTrackId() {
        return monitorTrackId;
    }

    public void setMonitorTrackId(String monitorTrackId) {
        this.monitorTrackId = monitorTrackId;
    }

    @Override
    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    @Override
    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    @Override
    public String getLogBizData() {
        return logBizData;
    }

    public void setLogBizData(String logBizData) {
        this.logBizData = logBizData;
    }

    @Override
    public Boolean isSuccessForMornitor() {
        return getProcessResult();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getGlobalTicket() {
        return globalTicket;
    }

    @Override
    public void setGlobalTicket(String globalTicket) {
        this.globalTicket = globalTicket;
    }

}