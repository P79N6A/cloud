package com.springframework.domain.base;


import lombok.Data;

import java.util.Date;

@Data
public class BaseDTO {

    protected String dataChangeCreatedBy;

    protected String dataChangeLastModifiedBy;

    protected Date dataChangeCreatedTime;

    protected Date dataChangeLastModifiedTime;

}
