package com.springframework.domain.base;

import lombok.Data;

import java.util.Date;

/** @author summer */
@Data
public class BaseDTO {

  protected String createdBy;

  protected String lastModifiedBy;

  protected Date createdTime;

  protected Date lastModifiedTime;
}
