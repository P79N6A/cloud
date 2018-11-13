package com.springframework.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @author summer
 */

@Slf4j
public class TmpFileUtil {

    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else {
                throw new RuntimeException("can't delete directory:" + file);
            }
        } else {
            log.warn("fileNotExists:" + file);
        }
    }

}
