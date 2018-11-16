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

package com.springframework.admin.config.notifier;

import com.springframework.admin.filter.StatusChangeNotifier;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.notify.RemindingNotifier;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Duration;

/**
 * @author lengleng
 * @date 2018/1/25
 * 监控提醒配置
 */
@Configuration
@EnableScheduling
public class NotifierConfiguration {
    @Autowired
    private InstanceRepository repositpry;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MonitorPropertiesConfig monitorPropertiesConfig;

    @Bean
    @Primary
    public RemindingNotifier remindingNotifier() {
        final StatusChangeNotifier changeNotifier = mobileNotifier();
        RemindingNotifier remindingNotifier = new RemindingNotifier(changeNotifier, repositpry);
        remindingNotifier.setReminderPeriod(Duration.ofMillis(1));
        return remindingNotifier;
    }

    @Bean
    public StatusChangeNotifier mobileNotifier() {
        return new StatusChangeNotifier(repositpry, monitorPropertiesConfig, rabbitTemplate);
    }

//    @Scheduled(fixedRate = 60_000L)
//    public void remind() {
//        RemindingNotifier notifier = remindingNotifier();
//        notifier.sendReminders();
//    }
}