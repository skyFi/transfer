
CREATE TABLE `action_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `action_content` longtext,
  `action_name` varchar(255) DEFAULT NULL,
  `action_time` datetime DEFAULT NULL,
  `action_type` varchar(255) DEFAULT NULL,
  `buyer_nick` varchar(255) DEFAULT NULL,
  `buyer_phone` varchar(255) DEFAULT NULL,
  `buyer_real_name` varchar(255) DEFAULT NULL,
  `campaign_id` bigint(20) DEFAULT NULL,
  `campaign_type_id` bigint(20) DEFAULT NULL,
  `fail_reason` varchar(255) DEFAULT NULL,
  `is_success` bit(1) NOT NULL,
  `trade_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
