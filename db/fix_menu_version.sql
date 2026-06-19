USE test_ddd;
ALTER TABLE `sys_menu` MODIFY COLUMN `version` int NULL DEFAULT 0;
UPDATE `sys_menu` SET `version` = 0 WHERE `version` IS NULL;
