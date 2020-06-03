CREATE TABLE IF NOT EXISTS `login_histories` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` bigint(20) NOT NULL,
    `ip_address` varchar(32) NOT NULL,
    `created_at` DATETIME NOT NULL,
    `created_by` bigint(20) NOT NULL,
    `modified_at` DATETIME NOT NULL,
    `modified_by` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_login_histories_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;