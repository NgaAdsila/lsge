CREATE TABLE IF NOT EXISTS `chatrooms` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(100),
    `last_message_id` bigint(20),
    `status` varchar(16) NOT NULL default 'CREATED',
    `created_at` DATETIME NOT NULL,
    `created_by` bigint(20) NOT NULL,
    `modified_at` DATETIME NOT NULL,
    `modified_by` bigint(20) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `chatroom_users` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `chatroom_id` bigint(20) NOT NULL,
    `user_id` bigint(20) NOT NULL,
    `nickname` varchar(100),
    `status` varchar(16) NOT NULL default 'JOINING',
    `created_at` DATETIME NOT NULL,
    `created_by` bigint(20) NOT NULL,
    `modified_at` DATETIME NOT NULL,
    `modified_by` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_chatroom_user_chatroom_id` FOREIGN KEY (`chatroom_id`) REFERENCES `chatrooms` (`id`),
    CONSTRAINT `fk_chatroom_user_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `messages` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `chatroom_id` bigint(20) NOT NULL,
    `message` varchar(2000),
    `created_at` DATETIME NOT NULL,
    `created_by` bigint(20) NOT NULL,
    `modified_at` DATETIME NOT NULL,
    `modified_by` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_message_chatroom_id` FOREIGN KEY (`chatroom_id`) REFERENCES `chatrooms` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;