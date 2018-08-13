package com.springframework.dfs.service;

import com.springframework.dfs.api.FFileUploadService;
import com.springframework.dfs.api.FileUploadLogService;
import com.springframework.dfs.configure.FastDFSClientWrapper;
import com.springframework.dfs.domain.entity.FileUploadLog;
import com.springframework.enums.fds.FileTypeEnum;
import com.springframework.enums.fds.FileUploadLogStatus;
import com.springframework.log.util.HostUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;

/**
 * @author summer
 * 2018/8/13
 */
@RestController
public class FFileUploadServiceImpl implements FFileUploadService {
    private static final Logger log = LoggerFactory.getLogger(FFileUploadServiceImpl.class);

    @Autowired
    private FastDFSClientWrapper dfsClient;

    @Autowired
    private FileUploadLogService fileUploadLogService;

    /**
     * 文件上传
     */
    @Override
    public String upload(HttpServletRequest request, @RequestParam(value = "file") MultipartFile file, @RequestParam(value = "application") String application) {
        try {
            String filePath = dfsClient.uploadFile(file);
            //TODO 保存文件上传记录，文件下载时需要通过filePath获取文件的原名称
            //fileUploadLogService 保存文件上传记录，保证对应关系参考logger做法
            FileUploadLog fileUploadLog = new FileUploadLog();
            fileUploadLog.setCreateTime(new Date());
            fileUploadLog.setApplication(application);
            fileUploadLog.setHost(HostUtils.getHostName());
            fileUploadLog.setIp(HostUtils.getIp());
            fileUploadLog.setFileName(file.getName());
            fileUploadLog.setFileType(FileTypeEnum.PIC.getValue());
            fileUploadLog.setId(null);
            fileUploadLog.setPath(filePath);
            fileUploadLog.setRequestParam(request.getQueryString());
            fileUploadLog.setStatus(FileUploadLogStatus.VALID.getValue());
            fileUploadLogService.insertSuccessLog(fileUploadLog);
            return filePath;
        } catch (IOException e) {
            log.error("文件上传错误", e);
        }
        return null;
    }

    /**
     * 文件下载
     * path Fdfs文件上传后得到的路径，如：group1/M00/00/02/ChNBRFnwLEyAEV80AABLs-J8J04329.png
     */
    @Override
    public ResponseEntity<byte[]> download(@NotNull @RequestParam(value = "path") String path) {
        //通过path获取该文件的原名称
        String fileName = fileUploadLogService.findFileName(path);
        try {
            byte[] body = dfsClient.downloadFile(path);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment;filename=" + fileName);
            HttpStatus statusCode = HttpStatus.OK;
            return new ResponseEntity<>(body, headers, statusCode);
        } catch (IOException e) {
            log.error("文件下载错误", e);
        }
        return null;
    }
}
