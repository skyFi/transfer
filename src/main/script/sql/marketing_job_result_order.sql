CREATE TABLE `marketing_job_result_order` (
  `job_id` bigint(20) NOT NULL,
  `oid` bigint(20) NOT NULL,
  `buyer_nick` varchar(255) DEFAULT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `item_num` int(11) NOT NULL,
  `marketing_time` datetime DEFAULT NULL,
  `paid` bit(1) NOT NULL,
  `payment` decimal(19,2) DEFAULT NULL,
  `tid` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`job_id`,`oid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
