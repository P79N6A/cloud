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

import com.springframework.sms.common.DingTalkMsgTemplate;
import com.springframework.sms.config.DingTalkPropertiesConfig;
import com.sun.deploy.net.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * @author summer
 * @date 2018/4/22
 * 发送钉钉消息逻辑
 */
@Slf4j
@Component
public class DingTalkMessageHandler {
    @Autowired
    private DingTalkPropertiesConfig dingTalkPropertiesConfig;
    @Autowired
    private RestTemplate restTemplate;
    /**
     * 业务处理
     *
     * @param text 消息
     */
    public boolean process(String text) {
        String webhook = dingTalkPropertiesConfig.getWebhook();
        if (StringUtils.isBlank(webhook)) {
            log.error("钉钉配置错误，webhook为空");
            return false;
        }

        DingTalkMsgTemplate dingTalkMsgTemplate = new DingTalkMsgTemplate();
        dingTalkMsgTemplate.setMsgtype("text");
        DingTalkMsgTemplate.TextBean textBean = new DingTalkMsgTemplate.TextBean();
        textBean.setContent(text);
        dingTalkMsgTemplate.setText(textBean);
        final String result = restTemplate.postForObject(URI.create(webhook), dingTalkMsgTemplate, String.class);
        log.info("钉钉提醒成功,报文响应:{}", result);
        return true;
    }

}
