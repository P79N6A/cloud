package com.springframework.log.service.impl;

import com.springframework.log.service.InterfaceLogService;
import com.springframework.log.dao.InterfaceLogDao;
import com.springframework.log.domain.InterfaceLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author summer
 */
@Service
@Slf4j
public class InterfaceLogServiceImpl implements InterfaceLogService {
    @Autowired
    private InterfaceLogDao interfaceLogDao;

    @Override
    public int insertSuccessLog(InterfaceLog interfaceLog) {
        try {
            return interfaceLogDao.insertSuccessLog(interfaceLog);
        } catch (Exception e) {
            log.error("insertSuccessLog error", e);
            return 0;
        }
    }

    @Override
    public int insertErrorLog(InterfaceLog interfaceLog) {
        try {
            return interfaceLogDao.insertErrorLog(interfaceLog);
        } catch (Exception e) {
            log.error("insertSuccessLog error", e);
            return 0;
        }
    }

    @Override
    public int deleteErrorLogByTime(Date date) {
        return interfaceLogDao.deleteErrorLogByTime(date);
    }

    @Override
    public int deleteErrorLog() {
        return interfaceLogDao.deleteErrorLog();
    }

    @Override
    public int deleteSuccessLogByTime(Date date) {
        return interfaceLogDao.deleteSuccessLogByTime(date);
    }

    @Override
    public int deleteSuccessLog() {
        return interfaceLogDao.deleteSuccessLog();
    }

    @Override
    public Long getSuccessLogCount() {
        return interfaceLogDao.getSuccessLogCount();
    }

    @Override
    public Long getErrorLogCount() {
        return interfaceLogDao.getErrorLogCount();
    }

}
