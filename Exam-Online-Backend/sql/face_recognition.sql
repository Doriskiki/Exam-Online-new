-- 人脸识别记录表
CREATE TABLE IF NOT EXISTS `face_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `exam_id` int(11) DEFAULT NULL COMMENT '考试ID（验证时使用）',
  `record_type` varchar(20) NOT NULL COMMENT '记录类型：register-注册, verify-验证',
  `matched` tinyint(1) DEFAULT NULL COMMENT '是否匹配成功',
  `similarity` double DEFAULT NULL COMMENT '相似度分数（0-1之间）',
  `embedding` text COMMENT '人脸特征向量（512维，JSON格式）',
  `record_time` datetime NOT NULL COMMENT '记录时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_exam_id` (`exam_id`),
  KEY `idx_record_time` (`record_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人脸识别记录表';

-- 为user表添加人脸识别相关字段（如果需要）
-- ALTER TABLE `user` ADD COLUMN `face_registered` tinyint(1) DEFAULT 0 COMMENT '是否已注册人脸';
-- ALTER TABLE `user` ADD COLUMN `face_register_time` datetime DEFAULT NULL COMMENT '人脸注册时间';
