package com.springframework.dfs.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author summer
 * 2018/8/13
 */
public interface FFileUploadService {

    public String upload(@RequestParam(value = "file") MultipartFile file);

    public ResponseEntity<byte[]> download(@RequestParam(value = "path") String path);

}
