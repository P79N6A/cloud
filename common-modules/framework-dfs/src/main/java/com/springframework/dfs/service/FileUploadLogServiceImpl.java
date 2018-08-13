package com.springframework.dfs.service;

import com.springframework.dfs.api.FileUploadLogService;
import com.springframework.dfs.domain.entity.FileUploadLog;
import com.springframework.dfs.mapper.FileUploadLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 连接fds数据库，保存上传记录
 *
 * @author summer
 * 2018/8/13
 */
@Service
@Slf4j
public class FileUploadLogServiceImpl implements FileUploadLogService {

    @Autowired
    private FileUploadLogMapper fileUploadLogMapper;

    @Override
    public int insertSuccessLog(FileUploadLog interfaceLog) {
        try {
            return fileUploadLogMapper.insertSuccessLog(interfaceLog);
        } catch (Exception e) {
            log.error("insertSuccessLog error", e);
            return 0;
        }
    }

    @Override
    public int insertErrorLog(FileUploadLog interfaceLog) {
        try {
            return fileUploadLogMapper.insertErrorLog(interfaceLog);
        } catch (Exception e) {
            log.error("insertSuccessLog error", e);
            return 0;
        }
    }

    @Override
    public int deleteErrorLogByTime(Date date) {
        return fileUploadLogMapper.deleteErrorLogByTime(date);
    }

    @Override
    public int deleteErrorLog() {
        return fileUploadLogMapper.deleteErrorLog();
    }

    @Override
    public int deleteSuccessLogByTime(Date date) {
        return fileUploadLogMapper.deleteSuccessLogByTime(date);
    }

    @Override
    public int deleteSuccessLog() {
        return fileUploadLogMapper.deleteSuccessLog();
    }

    @Override
    public Long getSuccessLogCount() {
        return fileUploadLogMapper.getSuccessLogCount();
    }

    @Override
    public Long getErrorLogCount() {
        return fileUploadLogMapper.getErrorLogCount();
    }

    @Override
    public String findFileName(@NotNull String path) {
        return fileUploadLogMapper.findFileName(path);
    }
}
