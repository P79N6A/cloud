/*
 *    Copyright (c) 2018-2025, summer All rights reserved.
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
 * Author: summer (wangiegie@gmail.com)
 */

package com.springframework.sms.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author summer
 * @date 2018/1/15
 * 钉钉消息模板
 * msgtype : text
 * text : {"content":"服务: serviceId 状态：UP"}
 */
@EqualsAndHashCode()
@Data
@ToString
public class DingTalkMsgTemplateDTO {
    private String msgtype;
    private TextBean text;

    @Data
    public static class TextBean {
        private String content;

    }
}
