CREATE TABLE IF NOT EXISTS `users` (
    `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(20) NOT NULL,
    `email` VARCHAR(50) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `created_at` DATETIME NOT NULL,
    `created_by` INT(11) NOT NULL,
    `modified_at` DATETIME NOT NULL,
    `modified_by` INT(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;