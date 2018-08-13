package com.springframework.dfs.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author summer
 * 2018/8/13
 */
public interface FFileUploadService {

    public String upload(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "file") MultipartFile file, @RequestParam(value = "application") String application);

    public ResponseEntity<byte[]> download(@RequestParam(value = "path") String path);

}
