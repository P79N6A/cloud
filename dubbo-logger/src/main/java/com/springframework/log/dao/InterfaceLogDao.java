package com.springframework.log.dao;

import com.springframework.log.domain.InterfaceLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface InterfaceLogDao {

    int insertSuccessLog(InterfaceLog log);

    int insertErrorLog(InterfaceLog log);

    int deleteErrorLogByTime(@Param("date") Date date);

    int deleteErrorLog();

    int deleteSuccessLogByTime(@Param("date") Date date);

    int deleteSuccessLog();

    Long getSuccessLogCount();

    Long getErrorLogCount();
}
