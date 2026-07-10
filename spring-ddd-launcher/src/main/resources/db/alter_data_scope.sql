-- ============================================================
-- Data Scope 改造数据库变更脚本
-- 适用数据库：spring_ddd
-- 前置条件：当前 schema 与 spring_ddd_0330.sql 一致
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 新增 sys_post 岗位表
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post` (
  `id` bigint NOT NULL COMMENT '主键 ID',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `post_name` varchar(128) NOT NULL COMMENT '岗位名称',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '上级岗位 ID',
  `sort_order` int NOT NULL COMMENT '排序',
  `post_status` tinyint(1) NOT NULL COMMENT '岗位状态',
  `create_by` varchar(64) NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `delete_status` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1已删除',
  `version` int NULL DEFAULT NULL COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 2. 新增 sys_user_post 用户岗位关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post` (
  `id` bigint NOT NULL COMMENT '主键 ID',
  `user_id` bigint NOT NULL COMMENT '用户 ID',
  `post_id` bigint NOT NULL COMMENT '岗位 ID',
  `create_by` varchar(64) NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `delete_status` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1已删除',
  `version` int NULL DEFAULT NULL COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 3. 新增 sys_role_menu_data_scope 角色菜单数据权限表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu_data_scope`;
CREATE TABLE `sys_role_menu_data_scope` (
  `id` bigint NOT NULL COMMENT '主键 ID',
  `role_id` bigint NOT NULL COMMENT '角色 ID',
  `menu_id` bigint NOT NULL COMMENT '菜单 ID',
  `data_scope` int NOT NULL COMMENT '数据范围（0:全部 1:仅当前部门 2:当前部门及子部门 3:个人 4:岗位）',
  `create_by` varchar(64) NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `delete_status` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记，0未删除，1已删除',
  `version` int NULL DEFAULT NULL COMMENT '乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单数据权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 4. 迁移 sys_role.data_scope 历史数据
--    旧值：1=All, 2=Department
--    新值：0=All, 1=DeptOnly, 2=DeptAndChildren, 3=Personal, 4=Post
-- ----------------------------
UPDATE `sys_role` SET `data_scope` = 0 WHERE `data_scope` = 1;
UPDATE `sys_role` SET `data_scope` = 2 WHERE `data_scope` = 2;

-- ----------------------------
-- 5. 删除非业务必需的 dept_id 字段
--    保留 sys_user.dept_id 用于部门权限反查
-- ----------------------------
ALTER TABLE `sys_role` DROP COLUMN `dept_id`;
ALTER TABLE `sys_role_menu` DROP COLUMN `dept_id`;
ALTER TABLE `sys_user_role` DROP COLUMN `dept_id`;
ALTER TABLE `sys_dict` DROP COLUMN `dept_id`;
ALTER TABLE `sys_dict_item` DROP COLUMN `dept_id`;
ALTER TABLE `sys_menu` DROP COLUMN `dept_id`;

-- ----------------------------
-- 6. 用户名校识：sys_user.username 加唯一索引
-- ----------------------------
ALTER TABLE `sys_user` ADD UNIQUE INDEX `uk_username` (`username`) USING BTREE;

-- ----------------------------
-- 7. 业务表 create_by 加索引，加速数据范围过滤
-- ----------------------------
ALTER TABLE `sys_dict` ADD INDEX `idx_create_by` (`create_by`) USING BTREE;
ALTER TABLE `sys_dict_item` ADD INDEX `idx_create_by` (`create_by`) USING BTREE;
ALTER TABLE `sys_menu` ADD INDEX `idx_create_by` (`create_by`) USING BTREE;
ALTER TABLE `sys_role_menu` ADD INDEX `idx_create_by` (`create_by`) USING BTREE;
ALTER TABLE `sys_user_role` ADD INDEX `idx_create_by` (`create_by`) USING BTREE;
ALTER TABLE `gen_project_info` ADD INDEX `idx_create_by` (`create_by`) USING BTREE;
ALTER TABLE `gen_columns` ADD INDEX `idx_create_by` (`create_by`) USING BTREE;
ALTER TABLE `gen_column_bind` ADD INDEX `idx_create_by` (`create_by`) USING BTREE;
ALTER TABLE `gen_aggregate` ADD INDEX `idx_create_by` (`create_by`) USING BTREE;
ALTER TABLE `gen_template` ADD INDEX `idx_create_by` (`create_by`) USING BTREE;
ALTER TABLE `sys_role_menu_data_scope` ADD INDEX `idx_create_by` (`create_by`) USING BTREE;
ALTER TABLE `sys_post` ADD INDEX `idx_create_by` (`create_by`) USING BTREE;
ALTER TABLE `sys_user_post` ADD INDEX `idx_create_by` (`create_by`) USING BTREE;

-- ----------------------------
-- 8. 角色-菜单数据权限查询索引
-- ----------------------------
ALTER TABLE `sys_role_menu_data_scope` ADD UNIQUE INDEX `uk_role_menu` (`role_id`, `menu_id`) USING BTREE;
ALTER TABLE `sys_role_menu_data_scope` ADD INDEX `idx_role_id` (`role_id`) USING BTREE;

-- ----------------------------
-- 9. 用户岗位关联查询索引
-- ----------------------------
ALTER TABLE `sys_user_post` ADD INDEX `idx_user_id` (`user_id`) USING BTREE;
ALTER TABLE `sys_user_post` ADD INDEX `idx_post_id` (`post_id`) USING BTREE;

-- ----------------------------
-- 10. 岗位树查询索引
-- ----------------------------
ALTER TABLE `sys_post` ADD INDEX `idx_parent_id` (`parent_id`) USING BTREE;

SET FOREIGN_KEY_CHECKS = 1;
