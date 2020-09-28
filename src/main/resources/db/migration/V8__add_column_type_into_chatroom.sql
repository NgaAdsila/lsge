ALTER TABLE `chatrooms`
ADD `type` varchar(16) NOT NULL default 'NORMAL' AFTER `name`;