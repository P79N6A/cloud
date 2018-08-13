package com.springframework.dfs.domain.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author summer
 * 2018/8/13
 */
@Data
@Table(name = "file_upload_log")
public class FileUploadLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String application;
    @Column
    private String host;
    @Column
    private String ip;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_type")
    private Integer fileType;
    @Column
    private Integer status;
    @Column
    private String path;
    @Column(name = "request_param")
    private String requestParam;
    @Column(name = "create_time")
    private Date createTime;
    @Column
    private String operater;
    @Column
    private String response;

    private final static String ID = "id";
    private final static String APPLICATION = "application";
    private final static String HOST = "host";
    private final static String FILE_NAME = "file_name";
    private final static String FILE_TYPE = "file_type";
    private final static String STATUS = "status";
    private final static String PATH = "path";
    private final static String REQUEST_PARAM = "request_param";
    private final static String CREATE_TIME = "create_time";
    private final static String OPERATER = "operater";
    private final static String RESPONSE = "response";


}
