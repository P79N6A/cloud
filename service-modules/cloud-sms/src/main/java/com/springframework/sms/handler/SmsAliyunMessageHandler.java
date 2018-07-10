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

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.springframework.common.utils.Assert;
import com.springframework.sms.domain.dto.MobileMsgTemplateDTO;
import com.springframework.sms.config.SmsAliyunPropertiesConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 浅梦
 * @date 2018/1/16 阿里大鱼短息服务处理
 */
@Slf4j
public class SmsAliyunMessageHandler extends AbstractMessageHandler {
    @Autowired
    private SmsAliyunPropertiesConfig smsAliyunPropertiesConfig;
    private static final String PRODUCT = "Dysmsapi";
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    /**
     * 数据校验
     *
     * @param mobileMsgTemplateDTO 消息
     */
    @Override
    public void check(MobileMsgTemplateDTO mobileMsgTemplateDTO) {
        Assert.isNull(mobileMsgTemplateDTO.getMobile(), "手机号不能为空");
        Assert.isNull(mobileMsgTemplateDTO.getContext(), "短信内容不能为空");
    }

    /**
     * 业务处理
     *
     * @param mobileMsgTemplateDTO 消息
     */
    @Override
    public boolean process(MobileMsgTemplateDTO mobileMsgTemplateDTO) {
        // 可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        // 初始化ascClient需要的几个参数
        // 短信API产品名称（短信产品名固定，无需修改）
        final String accessKeyId = smsAliyunPropertiesConfig.getAccessKey();
        final String accessKeySecret = smsAliyunPropertiesConfig.getSecretKey();
        // 初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        try {
            DefaultProfile.addEndpoint("cn-hou", "cn-hangzhou", PRODUCT, DOMAIN);
        } catch (ClientException e) {
            log.error("初始化SDK 异常", e);
            e.printStackTrace();
        }
        // 组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        // 必填:待发送手机号
        request.setPhoneNumbers(mobileMsgTemplateDTO.getMobile());

        // 必填:短信签名-可在短信控制台中找到
        request.setSignName(mobileMsgTemplateDTO.getSignName());

        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(
                smsAliyunPropertiesConfig.getChannels().get(mobileMsgTemplateDTO.getTemplate()));

        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"
        request.setTemplateParam(mobileMsgTemplateDTO.getContext());
        request.setOutId(mobileMsgTemplateDTO.getMobile());

        // hint 此处可能会抛出异常，注意catch
        try {
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信发送完毕，手机号：{}，返回状态：{}", mobileMsgTemplateDTO.getMobile(), sendSmsResponse.getCode());
        } catch (ClientException e) {
            log.error("发送异常");
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 失败处理
     *
     * @param mobileMsgTemplateDTO 消息
     */
    @Override
    public void fail(MobileMsgTemplateDTO mobileMsgTemplateDTO) {
        log.error(
                "短信发送失败 -> 网关：{} -> 手机号：{}", mobileMsgTemplateDTO.getType(), mobileMsgTemplateDTO.getMobile());
    }
}
