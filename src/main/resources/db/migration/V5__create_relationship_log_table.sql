CREATE TABLE IF NOT EXISTS `relationship_logs` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `req_user_id` bigint(20) NOT NULL,
    `rec_user_id` bigint(20) NOT NULL,
    `status` varchar(16) NOT NULL default 'PENDING',
    `created_at` DATETIME NOT NULL,
    `created_by` bigint(20) NOT NULL,
    `modified_at` DATETIME NOT NULL,
    `modified_by` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_relationship_logs_req_user_id` FOREIGN KEY (`req_user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_relationship_logs_rec_user_id` FOREIGN KEY (`rec_user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;