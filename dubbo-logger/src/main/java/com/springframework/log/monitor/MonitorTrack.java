package com.springframework.log.monitor;

public interface MonitorTrack {
	/**
	 * 获取dubbo监控trackid，可以使用java.util.UUID来设置
	 * @return
	 */
	String getMonitorTrackId();
	
	/**
	 * 获取业务数据，方便业务跟踪
	 * @return
	 */
	String getLogBizData();
	
	/**
	 * 是否成功:true 成功,false 失败，监控系统后续根据该值来进行预警
	 */
	Boolean  isSuccessForMornitor();
	
	/**
	 * 获取业务数据错误码，方便业务跟踪
	 * @return
	 */
	String getReturnCode();
	
	/**
	 * 获取业务数据对应错误信息，方便业务跟踪
	 * @return
	 */
	String getReturnMsg();
	
	/**
	 * 全局id
	 * @return
	 */
	String getGlobalTicket();
	
	/**
	 * 全局id
	 * @param globalTicket
	 */
	void setGlobalTicket(String globalTicket);
}
