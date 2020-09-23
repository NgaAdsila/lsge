CREATE TABLE IF NOT EXISTS `relationships` (
    `i_user_id` bigint(20) NOT NULL,
    `y_user_id` bigint(20) NOT NULL,
    `status` varchar(16) NOT NULL default 'PENDING',
    `created_at` DATETIME NOT NULL,
    `created_by` bigint(20) NOT NULL,
    `modified_at` DATETIME NOT NULL,
    `modified_by` bigint(20) NOT NULL,
    PRIMARY KEY (`i_user_id`, `y_user_id`),
    CONSTRAINT `fk_relationships_i_user_id` FOREIGN KEY (`i_user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_relationships_y_user_id` FOREIGN KEY (`y_user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;