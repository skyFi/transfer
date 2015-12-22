CREATE TABLE `campaign_instance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `campaign_id` bigint(20) DEFAULT NULL,
  `campaign_type_id` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `current_step` int(11) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `last_step_trigger_time` datetime DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `result_id` varchar(255) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `total_trades` longtext,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
