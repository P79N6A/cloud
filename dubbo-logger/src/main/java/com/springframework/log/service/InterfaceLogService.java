package com.springframework.log.service;

import com.springframework.log.domain.InterfaceLog;

import java.util.Date;

public interface InterfaceLogService {
    int insertSuccessLog(InterfaceLog log);

    int insertErrorLog(InterfaceLog log);

    int deleteErrorLogByTime(Date date);

    int deleteErrorLog();

    int deleteSuccessLogByTime(Date date);

    int deleteSuccessLog();

    Long getSuccessLogCount();

    Long getErrorLogCount();
}
