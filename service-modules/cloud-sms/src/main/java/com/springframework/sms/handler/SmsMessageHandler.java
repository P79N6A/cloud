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

package com.springframework.sms.handler;


import com.springframework.sms.domain.dto.MobileMsgTemplateDTO;

/**
 * @author summer
 * @date 2018/1/16
 */
public interface SmsMessageHandler {
    /**
     * 执行入口
     *
     * @param mobileMsgTemplateDTO 信息
     */
    void execute(MobileMsgTemplateDTO mobileMsgTemplateDTO);

    /**
     * 数据校验
     *
     * @param mobileMsgTemplateDTO 信息
     */
    void check(MobileMsgTemplateDTO mobileMsgTemplateDTO);

    /**
     * 业务处理
     *
     * @param mobileMsgTemplateDTO 信息
     * @return boolean
     */
    boolean process(MobileMsgTemplateDTO mobileMsgTemplateDTO);

    /**
     * 失败处理
     *
     * @param mobileMsgTemplateDTO 信息
     */
    void fail(MobileMsgTemplateDTO mobileMsgTemplateDTO);
}
