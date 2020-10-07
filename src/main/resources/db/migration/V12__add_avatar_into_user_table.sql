ALTER TABLE `users`
ADD `avatar` VARCHAR (255) AFTER `color`;

CREATE TABLE IF NOT EXISTS `uploading_image_logs` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` bigint(20) NOT NULL,
    `data` text,
    `created_at` DATETIME NOT NULL,
    `created_by` bigint(20) NOT NULL,
    `modified_at` DATETIME NOT NULL,
    `modified_by` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_uploading_image_log_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;