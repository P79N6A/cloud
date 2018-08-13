package com.springframework.dfs.mapper;

import com.springframework.dfs.domain.entity.FileUploadLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author xieshengrong
 */
@Mapper
public interface FileUploadLogMapper {

    int insertSuccessLog(FileUploadLog log);

    int insertErrorLog(FileUploadLog log);

    int deleteErrorLogByTime(@Param("date") Date date);

    int deleteErrorLog();

    int deleteSuccessLogByTime(@Param("date") Date date);

    int deleteSuccessLog();

    Long getSuccessLogCount();

    Long getErrorLogCount();

    /**查询文件名
     * @param path
     * @return
     */
    String findFileName(@Param("path")String path);
}
