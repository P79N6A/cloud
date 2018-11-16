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

package com.springframework.admin.filter;

import com.springframework.admin.config.notifier.MonitorPropertiesConfig;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;


/**
 * @author summer
 */
@Slf4j
public class StatusChangeNotifier extends AbstractStatusChangeNotifier {
    private RabbitTemplate rabbitTemplate;
    private MonitorPropertiesConfig monitorMobilePropertiesConfig;
    private InstanceRepository repositpry;

    public StatusChangeNotifier(InstanceRepository repositpry, MonitorPropertiesConfig monitorMobilePropertiesConfig, RabbitTemplate rabbitTemplate) {
        super(repositpry);
        this.rabbitTemplate = rabbitTemplate;
        this.monitorMobilePropertiesConfig = monitorMobilePropertiesConfig;
    }

    /**
     * 通知逻辑
     *
     * @param event 事件
     * @throws Exception 异常
     */
//    protected void doNotify(InstanceRegisteredEvent event) {
//        if (event instanceof ClientApplicationStatusChangedEvent) {
//            log.info("Application {} ({}) is {}", event.getApplication().getName(),
//                    event.getApplication().getId(), ((ClientApplicationStatusChangedEvent) event).getTo().getStatus());
//            String text = String.format("应用:%s 服务ID:%s 状态改变为：%s，时间：%s"
//                    , event.getApplication().getName()
//                    , event.getApplication().getId()
//                    , ((ClientApplicationStatusChangedEvent) event).getTo().getStatus()
//                    , DateUtil.formatTimestamp(event.getTimestamp()));
//
//            JSONObject contextJson = new JSONObject();
//            contextJson.put("name", event.getApplication().getName());
//            contextJson.put("seid", event.getApplication().getId());
//            contextJson.put("time", DateUtil.formatTimestamp(event.getTimestamp()));
//
//            //开启短信通知
//            if (monitorMobilePropertiesConfig.getMobile().getEnabled()) {
//                log.info("开始短信通知，内容：{}", text);
//                rabbitTemplate.convertAndSend( MqQueueConstant.MOBILE_SERVICE_STATUS_CHANGE,
//                        new MobileMsgTemplate(
//                                Joiner.on(",").join(monitorMobilePropertiesConfig.getMobile().getMobiles()),
//                                contextJson.toJSONString(),
//                                CommonConstant.ALIYUN_SMS,
//                                EnumSmsChannelTemplate.SERVICE_STATUS_CHANGE.getSignName(),
//                                EnumSmsChannelTemplate.SERVICE_STATUS_CHANGE.getTemplate()
//                        ));
//            }
//
//            if (monitorMobilePropertiesConfig.getDingTalk().getEnabled()) {
//                log.info("开始钉钉通知，内容：{}", text);
//                rabbitTemplate.convertAndSend(MqQueueConstant.DINGTALK_SERVICE_STATUS_CHANGE, text);
//            }
//
//
//        } else {
//            log.info("Application {} ({}) {}", event.getApplication().getName(),
//                    event.getApplication().getId(), event.getType());
//        }
//    }
    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return null;
    }
}
