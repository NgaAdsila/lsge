ALTER TABLE `messages`
ADD `type` varchar(16) NOT NULL default 'NORMAL' AFTER `message`;