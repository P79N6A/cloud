CREATE TABLE `route_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `route_id` varchar(255) NOT NULL DEFAULT '' COMMENT '路由id',
  `service_id` varchar(255) NOT NULL DEFAULT '' COMMENT '服务id',
  `service_name` varchar(255) NOT NULL DEFAULT '' COMMENT '服务名称',
  `status` bit(1) NOT NULL COMMENT '是否有效（0无效，1有效）',
  `uri` varchar(255) NOT NULL DEFAULT '' COMMENT 'uri',
  `order` int(11) NOT NULL DEFAULT '0' COMMENT '匹配路由优先级',
  `filters` varchar(255) NOT NULL DEFAULT '' COMMENT '过滤器',
  `predicates` varchar(255) DEFAULT NULL COMMENT '路由匹配法则，多个用逗号隔开（ps:如果为空，默认以service_id匹配）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator` varchar(255) NOT NULL DEFAULT '' COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `t_mobile_sms` (
  `id` bigint(20) NOT NULL,
  `operators_id` bigint(20) DEFAULT NULL COMMENT '运营商',
  `mobile` varchar(255) DEFAULT '' COMMENT '手机号',
  `context` text COMMENT '短信文本',
  `channel` varchar(255) DEFAULT '' COMMENT '短信通道',
  `status` bit(1) DEFAULT NULL,
  `type` varchar(255) DEFAULT '' COMMENT '短信类型',
  `signName` varchar(255) DEFAULT '' COMMENT '签名',
  `template` varchar(500) DEFAULT '' COMMENT '短信模板',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',,
  `operator` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;