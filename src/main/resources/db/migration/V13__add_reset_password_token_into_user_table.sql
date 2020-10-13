ALTER TABLE `users`
ADD `reset_password_token` VARCHAR(512) AFTER `password`;