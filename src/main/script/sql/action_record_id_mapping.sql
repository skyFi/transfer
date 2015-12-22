CREATE TABLE `action_record_id_mapping` (
  `id` bigint(20) NOT NULL,
  `old_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
