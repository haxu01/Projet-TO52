ALTER TABLE `topic`
    ADD COLUMN `style` VARCHAR(50) NULL DEFAULT 'MD' AFTER `user_id`;

ALTER TABLE `comment`
    ADD COLUMN `style` VARCHAR(50) NULL DEFAULT 'MD' AFTER `id`;

UPDATE `topic`
set `style` = "MD"
where `style` is null;

UPDATE `comment`
set `style` = "MD"
where `style` is null;
