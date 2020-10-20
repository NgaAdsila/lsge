CREATE TABLE IF NOT EXISTS `posts` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `title` varchar(255) NOT NULL,
    `content` text NOT NULL,
    `has_image` bit(1) NOT NULL DEFAULT b'0',
    `share_mode` varchar(16) NOT NULL DEFAULT 'PUBLIC',
    `status` varchar(16) NOT NULL DEFAULT 'CREATED',
    `root_id` bigint(20),
    `share_title` varchar(255),
    `created_at` DATETIME NOT NULL,
    `created_by` bigint(20) NOT NULL,
    `modified_at` DATETIME NOT NULL,
    `modified_by` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX (`root_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `comments` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `reference_type` VARCHAR(16) NOT NULL,
    `reference_id` bigint(20) NOT NULL,
    `message` text NOT NULL,
    `parent_id` bigint(20) NOT NULL DEFAULT 0,
    `status` varchar(16) NOT NULL DEFAULT 'CREATED',
    `created_at` DATETIME NOT NULL,
    `created_by` bigint(20) NOT NULL,
    `modified_at` DATETIME NOT NULL,
    `modified_by` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX (`parent_id`),
    INDEX (`reference_type`, `reference_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `post_reactive_users` (
    `post_id` bigint(20) NOT NULL,
    `user_id` bigint(20) NOT NULL,
    `type` VARCHAR(16) NOT NULL DEFAULT 'LIKE',
    `created_at` DATETIME NOT NULL,
    `created_by` bigint(20) NOT NULL,
    `modified_at` DATETIME NOT NULL,
    `modified_by` bigint(20) NOT NULL,
    PRIMARY KEY (`post_id`, `user_id`),
    CONSTRAINT `fk_post_reactive_post_id` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
    CONSTRAINT `fk_post_reactive_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;