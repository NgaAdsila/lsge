ALTER TABLE `users`
ADD `avatar` VARCHAR(255) AFTER `color`;

ALTER TABLE `users`
ADD `avatar_path` VARCHAR(255) AFTER `avatar`;