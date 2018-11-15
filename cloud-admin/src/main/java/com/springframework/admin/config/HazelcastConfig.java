package com.springframework.admin.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.MapConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author summer
 * 2018/11/15
 */
@Configuration
@EnableAutoConfiguration
public class HazelcastConfig {
//    @Bean
//    @ConditionalOnMissingBean
//    public Config hazelcastConfig() {
//        MapConfig mapConfig = new MapConfig("spring-boot-admin-event-store").setInMemoryFormat(InMemoryFormat.OBJECT)
//                .setBackupCount(1)
//                .setEvictionPolicy(EvictionPolicy.NONE);
//        return new Config().setProperty("hazelcast.jmx", "true").addMapConfig(mapConfig);
//    }
}
