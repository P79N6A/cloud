package com.springframework.dfs.api;

import com.springframework.dfs.domain.entity.FileUploadLog;

import java.util.Date;

/**
 * @author summer
 * 2018/8/13
 */
public interface FileUploadLogService {

    int insertSuccessLog(FileUploadLog log);

    int insertErrorLog(FileUploadLog log);

    int deleteErrorLogByTime(Date date);

    int deleteErrorLog();

    int deleteSuccessLogByTime(Date date);

    int deleteSuccessLog();

    Long getSuccessLogCount();

    Long getErrorLogCount();

    /**查询文件名
     * @param path
     * @return
     */
    String findFileName(String path);
}
