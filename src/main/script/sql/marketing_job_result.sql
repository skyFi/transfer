CREATE TABLE `marketing_job_result` (
  `buyer_nick` varchar(255) NOT NULL,
  `job_id` bigint(20) NOT NULL,
  `mobile` varchar(255) NOT NULL,
  `created_item_num` int(11) NOT NULL,
  `created_payment` decimal(19,2) DEFAULT NULL,
  `marketing_time` datetime DEFAULT NULL,
  `paid` bit(1) NOT NULL,
  `paid_item_num` int(11) NOT NULL,
  `paid_payment` decimal(19,2) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  PRIMARY KEY (`buyer_nick`,`job_id`,`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
