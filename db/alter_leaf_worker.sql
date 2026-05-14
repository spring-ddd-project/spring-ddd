-- ----------------------------
-- Table structure for leaf_worker
-- ----------------------------
DROP TABLE IF EXISTS `leaf_worker`;
CREATE TABLE `leaf_worker` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `worker_id` int NOT NULL COMMENT 'worker id (0-31)',
  `datacenter_id` int NOT NULL DEFAULT 0 COMMENT 'datacenter id (0-31)',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'IP 地址',
  `port` int NOT NULL COMMENT '端口',
  `last_timestamp` bigint NOT NULL DEFAULT 0 COMMENT '最后上报时间戳',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `delete_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_worker`(`worker_id` ASC, `datacenter_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
