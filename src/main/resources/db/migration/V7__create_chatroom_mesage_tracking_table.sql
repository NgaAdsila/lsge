ALTER TABLE `chatrooms`
ADD CONSTRAINT `fk_chatroom_last_message_id` FOREIGN KEY (`last_message_id`) REFERENCES `messages` (`id`);

CREATE TABLE IF NOT EXISTS `message_tracking_statuses` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `chatroom_id` bigint(20) NOT NULL,
    `message_id` bigint(20) NOT NULL,
    `user_id` bigint(20) NOT NULL,
    `is_seen` bit(1) NOT NULL default b'0',
    `created_at` DATETIME NOT NULL,
    `created_by` bigint(20) NOT NULL,
    `modified_at` DATETIME NOT NULL,
    `modified_by` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_message_tracking_status_chatroom_id` FOREIGN KEY (`chatroom_id`) REFERENCES `chatrooms` (`id`),
    CONSTRAINT `fk_message_tracking_status_message_id` FOREIGN KEY (`message_id`) REFERENCES `messages` (`id`),
    CONSTRAINT `fk_message_tracking_status_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;