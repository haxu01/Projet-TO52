ALTER TABLE `oauth_user` CHANGE `oauth_id` `oauth_id` INT(11)  NULL;

ALTER TABLE `oauth_user` ADD `refresh_token` VARCHAR(255) NULL  DEFAULT NULL  AFTER `user_id`;
ALTER TABLE `oauth_user` ADD `union_id` VARCHAR(255) NULL  DEFAULT NULL  AFTER `refresh_token`;
ALTER TABLE `oauth_user` ADD `expires_in` VARCHAR(255) NULL  DEFAULT NULL  AFTER `union_id`;

