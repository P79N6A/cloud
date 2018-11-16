/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.springframework.enums.sms;

import com.springframework.enums.StringValueDescription;
import lombok.Getter;
import lombok.Setter;

/**
 * @author summer
 * 短信通道枚举
 */

public enum EnumSmsChannel implements StringValueDescription {
    /**
     * 阿里大鱼短信通道
     */
    ALIYUN("ALIYUN_SMS", "阿里大鱼");
    /**
     * 通道名称
     */
    @Getter
    @Setter
    private String name;
    /**
     * 通道描述
     */
    @Setter
    private String description;

    EnumSmsChannel(String name, String description) {
        this.name = name;
        this.description = description;
    }


    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getValue() {
        return name;
    }

}
