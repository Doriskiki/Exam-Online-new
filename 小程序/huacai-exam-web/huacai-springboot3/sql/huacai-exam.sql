/*
 Navicat Premium Data Transfer

 Source Server         : test
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : huacai-exam

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 03/10/2025 23:42:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `category_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '分类ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '分类名称',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`category_id`) USING BTREE,
  UNIQUE INDEX `category_id`(`category_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '题目分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES ('179a325a8a02462184ea68ab669977c0', '软件工程', 4, '2025-09-18 10:53:40');
INSERT INTO `category` VALUES ('47675c6483ef4073b7cdcc3fa4f59f68', '计算机基础', 1, '2025-09-18 10:53:40');
INSERT INTO `category` VALUES ('5bc67c96a1f540128fd9968a3eab0350', '编程语言', 2, '2025-09-18 10:53:40');
INSERT INTO `category` VALUES ('980d855fb8504745818fc2a05e5732cd', '信息安全', 6, '2025-09-18 10:53:40');
INSERT INTO `category` VALUES ('be1e29fc527c4727aae7350f7211fc65', '数据库', 3, '2025-09-18 10:53:40');
INSERT INTO `category` VALUES ('d779eef6d38149b89e85f0e35ae65c44', '网络基础', 5, '2025-09-18 10:53:40');

-- ----------------------------
-- Table structure for eq
-- ----------------------------
DROP TABLE IF EXISTS `eq`;
CREATE TABLE `eq`  (
  `eq_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '试题ID',
  `exam_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '考试ID',
  `question_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '题目ID',
  `score` int NOT NULL DEFAULT 10 COMMENT '题目分数',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`eq_id`) USING BTREE,
  UNIQUE INDEX `eq_id`(`eq_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '试题' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of eq
-- ----------------------------
INSERT INTO `eq` VALUES ('01121e0b447a42588143cdde3fba368b', '6436a2f987424082a889f58a0c9f64e4', 'a5a6d96d-9abf-11f0-9832-fc3497b6c58b', 10, '2025-09-28 23:21:30');
INSERT INTO `eq` VALUES ('073b43abdb4c4685827f0805ea290351', '6436a2f987424082a889f58a0c9f64e4', 'a59c340d-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:30');
INSERT INTO `eq` VALUES ('1386b77bf6f84335844b529d147b6092', 'b63685adc7424b2b8ad42a0c51e0886d', 'a5ff4837-9abf-11f0-9832-fc3497b6c58b', 10, '2025-09-28 23:21:59');
INSERT INTO `eq` VALUES ('1d34fbdc8402470eae89b889e8281427', '6436a2f987424082a889f58a0c9f64e4', 'a5a134e0-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:30');
INSERT INTO `eq` VALUES ('1f15f1ea2c45490f9656cfd34f8da786', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5c31406-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:45');
INSERT INTO `eq` VALUES ('258cdea306304a5795fdce2c5e4c185a', '6436a2f987424082a889f58a0c9f64e4', 'a59e19fa-9abf-11f0-9832-fc3497b6c58b', 10, '2025-09-28 23:21:30');
INSERT INTO `eq` VALUES ('2be74ceaf77c478ea904991425f0b272', '6436a2f987424082a889f58a0c9f64e4', 'a593c845-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:30');
INSERT INTO `eq` VALUES ('307a2b5cc6ae4152b6ef17d03029c305', '6436a2f987424082a889f58a0c9f64e4', 'a5a4d7d7-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:30');
INSERT INTO `eq` VALUES ('31402a7748c34fb9b6ecd96578d3f518', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5b302be-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:45');
INSERT INTO `eq` VALUES ('43413dee63b74c08b6c1143fd3fd4bbc', 'b63685adc7424b2b8ad42a0c51e0886d', 'a5f03c3c-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:59');
INSERT INTO `eq` VALUES ('498071155e904eeba7814533c1a12a71', 'b63685adc7424b2b8ad42a0c51e0886d', 'a5f8e333-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:59');
INSERT INTO `eq` VALUES ('5101f9038db44da2b57caafb362beecf', 'b63685adc7424b2b8ad42a0c51e0886d', 'a5fa9e4b-9abf-11f0-9832-fc3497b6c58b', 10, '2025-09-28 23:21:59');
INSERT INTO `eq` VALUES ('533f5aae2450487084e9ecdb6f6c3245', '6436a2f987424082a889f58a0c9f64e4', 'a5971b17-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:30');
INSERT INTO `eq` VALUES ('61fbd6f7ee984f7888279c6506ebd0cf', 'b63685adc7424b2b8ad42a0c51e0886d', 'a601989d-9abf-11f0-9832-fc3497b6c58b', 10, '2025-09-28 23:21:59');
INSERT INTO `eq` VALUES ('6be82fab30224938ae3164e01a998686', '6436a2f987424082a889f58a0c9f64e4', 'a5a988ca-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:30');
INSERT INTO `eq` VALUES ('6c4764251b5646b3b22e74cac97bac78', 'b63685adc7424b2b8ad42a0c51e0886d', 'a603ded7-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:59');
INSERT INTO `eq` VALUES ('6eb836a3228b417a894cc32dd7e9f73a', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5cc7c50-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:45');
INSERT INTO `eq` VALUES ('707e108fbc1e4d3d91977ad5bbf25340', 'b63685adc7424b2b8ad42a0c51e0886d', 'a60670f0-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:59');
INSERT INTO `eq` VALUES ('728ee335e3d54500a0e7f616aacb8c35', '6436a2f987424082a889f58a0c9f64e4', 'a5959c38-9abf-11f0-9832-fc3497b6c58b', 10, '2025-09-28 23:21:30');
INSERT INTO `eq` VALUES ('7ed515da05b34a38856660117d3daa99', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5ba6943-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:45');
INSERT INTO `eq` VALUES ('81710f491103408eba43b5d1d189b380', '6436a2f987424082a889f58a0c9f64e4', 'a5b011d7-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:30');
INSERT INTO `eq` VALUES ('901f564818f44e189967c86b546b89af', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5c45b63-9abf-11f0-9832-fc3497b6c58b', 10, '2025-09-28 23:21:45');
INSERT INTO `eq` VALUES ('9678b0b47f0649958ff593db94812899', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5c942ef-9abf-11f0-9832-fc3497b6c58b', 10, '2025-09-28 23:21:45');
INSERT INTO `eq` VALUES ('9f5793961e894922872fab73d03dc213', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5cf03a8-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:45');
INSERT INTO `eq` VALUES ('a0aa08d0f83e4b6db3946d980e2c5bd6', 'b63685adc7424b2b8ad42a0c51e0886d', 'a5f2d2bf-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:59');
INSERT INTO `eq` VALUES ('a4a09e635a4042b1a069c8a35e9f11fe', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5b6f56b-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:45');
INSERT INTO `eq` VALUES ('afee5163631447d895c06d12dff92d7b', '6436a2f987424082a889f58a0c9f64e4', 'a598f697-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:30');
INSERT INTO `eq` VALUES ('b1401930566f4855856d3ead7e1f464c', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5b474bf-9abf-11f0-9832-fc3497b6c58b', 10, '2025-09-28 23:21:45');
INSERT INTO `eq` VALUES ('c4a6b74324e9424bad9e641e1f4826b5', '6436a2f987424082a889f58a0c9f64e4', 'a5ac0359-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:30');
INSERT INTO `eq` VALUES ('c4f98090a41f4d38b113f7efdc5f1516', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5c0ec1c-9abf-11f0-9832-fc3497b6c58b', 10, '2025-09-28 23:21:45');
INSERT INTO `eq` VALUES ('c86a167f22f746459eb0e774e668b23a', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5c5d76a-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:45');
INSERT INTO `eq` VALUES ('cbc1a14d802c48308dc54758c51392a3', 'b63685adc7424b2b8ad42a0c51e0886d', 'a5f5ebab-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:59');
INSERT INTO `eq` VALUES ('d2a6615a477b44618a4d2b0573a82f91', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5d03da6-9abf-11f0-9832-fc3497b6c58b', 10, '2025-09-28 23:21:45');
INSERT INTO `eq` VALUES ('d41b4257e8ca495faaa2a9d59f9a2cab', '6436a2f987424082a889f58a0c9f64e4', 'a5a2af2f-9abf-11f0-9832-fc3497b6c58b', 10, '2025-09-28 23:21:30');
INSERT INTO `eq` VALUES ('d77e29d1a959495cafc33e086c44d4fe', 'b63685adc7424b2b8ad42a0c51e0886d', 'a602c45a-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:59');
INSERT INTO `eq` VALUES ('d924a1f11f2b413a983c742ff38a2c80', '6436a2f987424082a889f58a0c9f64e4', 'a5aaafa6-9abf-11f0-9832-fc3497b6c58b', 10, '2025-09-28 23:21:30');
INSERT INTO `eq` VALUES ('dc74585d8b5e4052808f977a4ed70116', 'b63685adc7424b2b8ad42a0c51e0886d', 'a60a4ec0-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:59');
INSERT INTO `eq` VALUES ('e08c2ff34b4d423db72e486322d84a4b', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5caf033-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:45');
INSERT INTO `eq` VALUES ('e2d8f19a4b9b48febd1eb936b97c9960', 'b63685adc7424b2b8ad42a0c51e0886d', 'a60ba4b3-9abf-11f0-9832-fc3497b6c58b', 10, '2025-09-28 23:21:59');
INSERT INTO `eq` VALUES ('e2ed44e939044fa6a0bc0c1b7d145af8', 'b63685adc7424b2b8ad42a0c51e0886d', 'a607a657-9abf-11f0-9832-fc3497b6c58b', 10, '2025-09-28 23:21:59');
INSERT INTO `eq` VALUES ('e6be9e6609e24925ae28d317e123f34e', 'b63685adc7424b2b8ad42a0c51e0886d', 'a6007086-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:59');
INSERT INTO `eq` VALUES ('ece4e087bd2a49fdbeefee694e7fcf5a', '6436a2f987424082a889f58a0c9f64e4', 'a5b19b7f-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:30');
INSERT INTO `eq` VALUES ('ede73c776bdf4f36838c5bae2b1268dd', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5c79424-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:45');
INSERT INTO `eq` VALUES ('ffd8f5a8f63549918cb21024617dd5ca', 'b63685adc7424b2b8ad42a0c51e0886d', 'a5fdbfeb-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:59');
INSERT INTO `eq` VALUES ('ffdc4aea62824d3ebe9239cdebec3d33', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5bec690-9abf-11f0-9832-fc3497b6c58b', 5, '2025-09-28 23:21:45');

-- ----------------------------
-- Table structure for er
-- ----------------------------
DROP TABLE IF EXISTS `er`;
CREATE TABLE `er`  (
  `er_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '考试结果ID',
  `exam_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '考试ID',
  `question_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '题目ID',
  `user_answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户答案',
  `correct_answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '正确答案',
  `is_correct` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '是否正确',
  `user_id` int NOT NULL COMMENT '用户ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`er_id`) USING BTREE,
  UNIQUE INDEX `er_id`(`er_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '考试结果' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of er
-- ----------------------------
INSERT INTO `er` VALUES ('051f2688deab4807ac6f3d3501cb81e5', 'b63685adc7424b2b8ad42a0c51e0886d', 'a5ff4837-9abf-11f0-9832-fc3497b6c58b', '0,1,2,3', '0,1,2,3', '正确', 104, '2025-10-03 00:05:09');
INSERT INTO `er` VALUES ('15c54bbeed23412a9ccce512a466059a', 'b63685adc7424b2b8ad42a0c51e0886d', 'a5f03c3c-9abf-11f0-9832-fc3497b6c58b', '0', '0', '正确', 104, '2025-10-03 00:05:09');
INSERT INTO `er` VALUES ('15de98f39e684cce832919ce42bbb9eb', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5c5d76a-9abf-11f0-9832-fc3497b6c58b', '1', '0', '错误', 104, '2025-10-01 22:55:38');
INSERT INTO `er` VALUES ('1b2968e6e6f947e6ab7bbf631d65be22', 'b63685adc7424b2b8ad42a0c51e0886d', 'a5f2d2bf-9abf-11f0-9832-fc3497b6c58b', '0,1,2', '0,1,2', '正确', 104, '2025-10-03 00:05:09');
INSERT INTO `er` VALUES ('30355a472d4b4d6681e30fdbec1c9410', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5c79424-9abf-11f0-9832-fc3497b6c58b', '3', '0', '错误', 104, '2025-10-01 22:55:38');
INSERT INTO `er` VALUES ('321085c7064e4101befdee2df10c2d42', 'b63685adc7424b2b8ad42a0c51e0886d', 'a603ded7-9abf-11f0-9832-fc3497b6c58b', '3', '3', '正确', 104, '2025-10-03 00:05:09');
INSERT INTO `er` VALUES ('4151d1e33506449fa06ef865df331869', 'b63685adc7424b2b8ad42a0c51e0886d', 'a60ba4b3-9abf-11f0-9832-fc3497b6c58b', '0,1,2,3', '0,1,2,3', '正确', 104, '2025-10-03 00:05:09');
INSERT INTO `er` VALUES ('687218848df94829a27ce01de815220d', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5ba6943-9abf-11f0-9832-fc3497b6c58b', '3', '1', '错误', 104, '2025-10-01 22:55:38');
INSERT INTO `er` VALUES ('69b4ff8d3ded4b13b91fad79831ce0e1', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5c0ec1c-9abf-11f0-9832-fc3497b6c58b', '3', '0,1,2,3', '错误', 104, '2025-10-01 22:55:38');
INSERT INTO `er` VALUES ('6b7c72bf331449d4a2498dcd3fc77b47', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5c31406-9abf-11f0-9832-fc3497b6c58b', '3', '2', '错误', 104, '2025-10-01 22:55:38');
INSERT INTO `er` VALUES ('74335f9afd784cf28457b79f3c3d7556', 'b63685adc7424b2b8ad42a0c51e0886d', 'a607a657-9abf-11f0-9832-fc3497b6c58b', '0,1,2,3', '0,1,2,3', '正确', 104, '2025-10-03 00:05:09');
INSERT INTO `er` VALUES ('7708073444554394a0be9c617c3230b1', 'b63685adc7424b2b8ad42a0c51e0886d', 'a5fa9e4b-9abf-11f0-9832-fc3497b6c58b', '0,1,2', '0,1,2', '正确', 104, '2025-10-03 00:05:09');
INSERT INTO `er` VALUES ('8030021a04b64e88ad53be8262e0d6bd', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5d03da6-9abf-11f0-9832-fc3497b6c58b', '3', '0,1,2', '错误', 104, '2025-10-01 22:55:38');
INSERT INTO `er` VALUES ('87fb9281cf0f49718a42b8c0d34432d5', 'b63685adc7424b2b8ad42a0c51e0886d', 'a6007086-9abf-11f0-9832-fc3497b6c58b', '0', '0', '正确', 104, '2025-10-03 00:05:09');
INSERT INTO `er` VALUES ('89beea1d36b04da4bbaa85e348528977', 'b63685adc7424b2b8ad42a0c51e0886d', 'a5f5ebab-9abf-11f0-9832-fc3497b6c58b', '1', '1', '正确', 104, '2025-10-03 00:05:09');
INSERT INTO `er` VALUES ('920d3edc31c1421db1befb7b1f34f731', 'b63685adc7424b2b8ad42a0c51e0886d', 'a5fdbfeb-9abf-11f0-9832-fc3497b6c58b', '1', '1', '正确', 104, '2025-10-03 00:05:09');
INSERT INTO `er` VALUES ('9ff9b959e9e34429b91681baac006fac', 'b63685adc7424b2b8ad42a0c51e0886d', 'a601989d-9abf-11f0-9832-fc3497b6c58b', '0,1,2,3', '0,1,2,3', '正确', 104, '2025-10-03 00:05:09');
INSERT INTO `er` VALUES ('a5037957624b4881bef5d35d0a077c5a', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5b302be-9abf-11f0-9832-fc3497b6c58b', '3', '1', '错误', 104, '2025-10-01 22:55:38');
INSERT INTO `er` VALUES ('b426291f9ca9490e8da918584de0c33b', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5cf03a8-9abf-11f0-9832-fc3497b6c58b', '3', '3', '正确', 104, '2025-10-01 22:55:38');
INSERT INTO `er` VALUES ('b6474f11f7e843ddb3b93b99b067aba8', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5c45b63-9abf-11f0-9832-fc3497b6c58b', '3', '0,1,2', '错误', 104, '2025-10-01 22:55:38');
INSERT INTO `er` VALUES ('b8dfc7bec3da4b0b8f9a89c5f9f557da', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5b474bf-9abf-11f0-9832-fc3497b6c58b', '3', '0,2,3', '错误', 104, '2025-10-01 22:55:38');
INSERT INTO `er` VALUES ('be189db5ed334e778ac93b700503a34f', 'b63685adc7424b2b8ad42a0c51e0886d', 'a602c45a-9abf-11f0-9832-fc3497b6c58b', '1', '1', '正确', 104, '2025-10-03 00:05:09');
INSERT INTO `er` VALUES ('c3e44dc8146344fbab9510bb139f6933', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5c942ef-9abf-11f0-9832-fc3497b6c58b', '3', '0,1,2,3', '错误', 104, '2025-10-01 22:55:38');
INSERT INTO `er` VALUES ('e1e734aa8436403799e8701bea8a722c', 'b63685adc7424b2b8ad42a0c51e0886d', 'a60a4ec0-9abf-11f0-9832-fc3497b6c58b', '3', '3', '正确', 104, '2025-10-03 00:05:09');
INSERT INTO `er` VALUES ('e297eafb20454ee294eb0bc28493b126', 'b63685adc7424b2b8ad42a0c51e0886d', 'a60670f0-9abf-11f0-9832-fc3497b6c58b', '0', '0', '正确', 104, '2025-10-03 00:05:09');
INSERT INTO `er` VALUES ('e31ab127c84f472aad0d0130a5295651', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5b6f56b-9abf-11f0-9832-fc3497b6c58b', '3', '3', '正确', 104, '2025-10-01 22:55:38');
INSERT INTO `er` VALUES ('e690b024becd4d009e95958e86d73d2e', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5bec690-9abf-11f0-9832-fc3497b6c58b', '3', '1', '错误', 104, '2025-10-01 22:55:38');
INSERT INTO `er` VALUES ('ec40db4cc260472bb8ab440c45a0698b', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5caf033-9abf-11f0-9832-fc3497b6c58b', '3', '0', '错误', 104, '2025-10-01 22:55:38');
INSERT INTO `er` VALUES ('fa43877b4e8c44b48565d80b531d3430', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'a5cc7c50-9abf-11f0-9832-fc3497b6c58b', '3', '0,1,2', '错误', 104, '2025-10-01 22:55:38');
INSERT INTO `er` VALUES ('fc4ac4a105044692aad2b26202d0c47b', 'b63685adc7424b2b8ad42a0c51e0886d', 'a5f8e333-9abf-11f0-9832-fc3497b6c58b', '3', '3', '正确', 104, '2025-10-03 00:05:09');

-- ----------------------------
-- Table structure for eu
-- ----------------------------
DROP TABLE IF EXISTS `eu`;
CREATE TABLE `eu`  (
  `eu_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '考试用户分配ID',
  `exam_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '考试ID',
  `question_count` int NOT NULL COMMENT '题目总数',
  `correct_count` int NULL DEFAULT NULL COMMENT '正确题数',
  `score` int NULL DEFAULT NULL COMMENT '得分',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '进行中' COMMENT '状态',
  `user_id` int NOT NULL COMMENT '用户ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`eu_id`) USING BTREE,
  UNIQUE INDEX `eu_id`(`eu_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '考试用户分配' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of eu
-- ----------------------------
INSERT INTO `eu` VALUES ('10120038f58546a5b2ec947beffc2037', 'b63685adc7424b2b8ad42a0c51e0886d', 15, NULL, NULL, '进行中', 106, '2025-09-29 21:56:52');
INSERT INTO `eu` VALUES ('21b4b9c8acd2479b895acb09d535c80b', '75b0e2f5d7ad4b72bcbb945f2c5dbc64', 15, 2, 10, '考试未通过', 104, '2025-09-29 22:33:04');
INSERT INTO `eu` VALUES ('a871a040c1264fa08b19cf27b0417bfe', 'b63685adc7424b2b8ad42a0c51e0886d', 15, 15, 100, '考试通过', 104, '2025-09-29 21:56:52');
INSERT INTO `eu` VALUES ('c642d511fd3a4749badae54b478f894c', 'b63685adc7424b2b8ad42a0c51e0886d', 15, NULL, NULL, '进行中', 105, '2025-09-29 21:56:52');

-- ----------------------------
-- Table structure for exam
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam`  (
  `exam_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '考试ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '考试标题',
  `category_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '分类ID',
  `time` decimal(10, 1) NULL DEFAULT 1.0 COMMENT '考试时长(小时)',
  `total_score` int NULL DEFAULT 100 COMMENT '总分',
  `pass_score` int NULL DEFAULT 60 COMMENT '及格分',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`exam_id`) USING BTREE,
  UNIQUE INDEX `exam_id`(`exam_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '考试' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exam
-- ----------------------------
INSERT INTO `exam` VALUES ('6436a2f987424082a889f58a0c9f64e4', '计算机基础能力测试', '47675c6483ef4073b7cdcc3fa4f59f68', 1.0, 100, 60, '2025-09-27 14:38:51');
INSERT INTO `exam` VALUES ('75b0e2f5d7ad4b72bcbb945f2c5dbc64', 'java工程师', '5bc67c96a1f540128fd9968a3eab0350', 1.0, 100, 60, '2025-09-27 14:39:05');
INSERT INTO `exam` VALUES ('b63685adc7424b2b8ad42a0c51e0886d', '数据库系统工程师', 'be1e29fc527c4727aae7350f7211fc65', 1.0, 100, 60, '2025-09-27 14:34:39');

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '代码生成业务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table
-- ----------------------------
INSERT INTO `gen_table` VALUES (15, 'guide', '学习指南', NULL, NULL, 'Guide', 'crud', 'element-plus', 'com.huacai.exam', 'exam', 'guide', '学习指南', 'huacai', '0', '/', '{}', 'admin', '2025-10-02 00:03:43', '', '2025-10-02 00:04:39', NULL);

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典类型',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 108 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '代码生成业务表字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
INSERT INTO `gen_table_column` VALUES (102, 15, 'guide_id', '学习指南ID', 'varchar(255)', 'String', 'guideId', '1', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-10-02 00:03:43', '', '2025-10-02 00:04:39');
INSERT INTO `gen_table_column` VALUES (103, 15, 'type', '类型', 'varchar(50)', 'String', 'type', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'select', 'guide_type', 2, 'admin', '2025-10-02 00:03:43', '', '2025-10-02 00:04:39');
INSERT INTO `gen_table_column` VALUES (104, 15, 'title', '标题', 'varchar(255)', 'String', 'title', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2025-10-02 00:03:43', '', '2025-10-02 00:04:39');
INSERT INTO `gen_table_column` VALUES (105, 15, 'content', '内容', 'text', 'String', 'content', '0', '0', '1', '1', '1', '1', '0', 'EQ', 'editor', '', 4, 'admin', '2025-10-02 00:03:43', '', '2025-10-02 00:04:39');
INSERT INTO `gen_table_column` VALUES (106, 15, 'summary', '要点总结', 'varchar(255)', 'String', 'summary', '0', '0', '1', '1', '1', '1', '0', 'EQ', 'input', '', 5, 'admin', '2025-10-02 00:03:43', '', '2025-10-02 00:04:39');
INSERT INTO `gen_table_column` VALUES (107, 15, 'tips', '实用技巧', 'varchar(500)', 'String', 'tips', '0', '0', '1', '1', '1', '1', '0', 'EQ', 'input', '', 6, 'admin', '2025-10-02 00:03:43', '', '2025-10-02 00:04:39');
INSERT INTO `gen_table_column` VALUES (108, 15, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2025-10-02 00:03:43', '', '2025-10-02 00:04:39');

-- ----------------------------
-- Table structure for guide
-- ----------------------------
DROP TABLE IF EXISTS `guide`;
CREATE TABLE `guide`  (
  `guide_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '学习指南ID',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '类型',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '内容',
  `summary` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '要点总结',
  `tips` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '实用技巧',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE INDEX `guide_id`(`guide_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '学习指南' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of guide
-- ----------------------------
INSERT INTO `guide` VALUES ('092bf69069b64c8a880ebd02bd18c667', '方法篇', '高效记忆法：如何快速记住大量知识点', '<p>在学习过程中，我们经常需要记住大量的知识点，这可能会让人感到压力。</p><p>以下是一些<strong>高效的记忆方法</strong>：</p><ol><li><strong>分段记忆法</strong>：将大量信息分成小段，每次只记忆一小部分，逐步累积。</li><li><strong>关联记忆法</strong>：将新知识与已有的知识或经验联系起来，形成关联网络。</li><li><strong>图像记忆法</strong>：将抽象的信息转化为具体的图像，利用视觉记忆的优势。</li><li><strong>重复记忆法</strong>：根据遗忘曲线规律，合理安排复习时间，巩固记忆效果。</li></ol>', '分段记忆、关联记忆、图像记忆和重复记忆是四种高效的记忆方法。', '建议结合使用多种记忆方法，根据不同的学习内容选择最适合的方法。', '2025-10-02 00:09:08');
INSERT INTO `guide` VALUES ('1105baa66f7a4fbea3d55f0721eacf04', '技巧篇', '答题技巧：如何在选择题中快速找到正确答案', '<p>选择题是各类考试中的常见题型，掌握一些答题技巧可以提高正确率：</p><ol><li><strong>先易后难</strong>：先做有把握的题目，确保拿到基础分数。</li><li><strong>排除法</strong>：排除明显错误的选项，缩小选择范围。</li><li><strong>关键词法</strong>：仔细审题，找出题干中的关键词，避免被干扰信息误导。</li><li><strong>对比法</strong>：对比各选项的差异，找出最符合题意的答案。</li></ol>', '先易后难、排除法、关键词法和对比法是解答选择题的四大技巧。', '平时练习时要有意识地运用这些技巧，形成习惯后在考试中自然运用。', '2025-10-02 00:09:08');
INSERT INTO `guide` VALUES ('ce4c8c753b3a4cf883187a3e94a21cb2', '心态篇', '考试心态调整：如何在考场上保持冷静', '<p>良好的心态是考试成功的重要因素，以下方法可以帮助你调整心态：</p><ol><li><strong>充分准备</strong>：做好充分的复习准备，增强自信心。</li><li><strong>深呼吸放松</strong>：感到紧张时，进行深呼吸，放松身心。</li><li><strong>积极暗示</strong>：给自己积极的心理暗示，相信自己能够成功。</li><li><strong>合理分配时间</strong>：合理安排答题时间，避免因时间不足而紧张。</li></ol>', '充分准备、深呼吸放松、积极暗示和合理分配时间有助于保持良好心态。', '平时模拟考试时也要注意心态调整，培养在压力下保持冷静的能力。', '2025-10-02 00:09:08');

-- ----------------------------
-- Table structure for questions
-- ----------------------------
DROP TABLE IF EXISTS `questions`;
CREATE TABLE `questions`  (
  `question_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '题目ID',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '题型',
  `category_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '分类ID',
  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '题目标题',
  `options` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '选项',
  `answer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '正确答案',
  `analysis` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '题目解析',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`question_id`) USING BTREE,
  UNIQUE INDEX `question_id`(`question_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '题目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of questions
-- ----------------------------
INSERT INTO `questions` VALUES ('a5709ff9-9abf-11f0-9832-fc3497b6c58b', '单选题', '179a325a8a02462184ea68ab669977c0', '下列哪项不是软件工程的基本特征？', '系统性,规范性,随意性,可维护性', '2', '软件工程强调规范化和系统性，不能随意开发。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5738474-9abf-11f0-9832-fc3497b6c58b', '多选题', '179a325a8a02462184ea68ab669977c0', '软件生命周期包括哪些阶段？', '需求分析,设计,编码,测试维护', '0,1,2,3', '软件生命周期包含需求、设计、实现、测试和维护全过程。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5753abf-9abf-11f0-9832-fc3497b6c58b', '判断题', '179a325a8a02462184ea68ab669977c0', '敏捷开发是一种重量级的开发方法。', '正确,错误', '1', '敏捷开发是轻量级方法，强调快速迭代。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5772a5e-9abf-11f0-9832-fc3497b6c58b', '单选题', '179a325a8a02462184ea68ab669977c0', '黑盒测试主要关注？', '内部逻辑,输入输出,代码结构,内存使用', '1', '黑盒测试只关注功能输入输出，不关心内部实现。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a578e761-9abf-11f0-9832-fc3497b6c58b', '多选题', '179a325a8a02462184ea68ab669977c0', 'UML图中属于行为图的是？', '用例图,类图,状态图,活动图', '2,3', '状态图和活动图用于描述系统行为。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a57aa708-9abf-11f0-9832-fc3497b6c58b', '单选题', '179a325a8a02462184ea68ab669977c0', '瀑布模型的特点是？', '迭代开发,线性顺序,灵活变更,快速交付', '1', '瀑布模型是线性的开发过程，阶段间顺序进行。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a57c8216-9abf-11f0-9832-fc3497b6c58b', '多选题', '179a325a8a02462184ea68ab669977c0', '软件需求分析的主要任务包括？', '需求获取,需求分析,需求规格说明,需求验证', '0,1,2,3', '需求分析包括获取、分析、规格说明和验证四个主要任务。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a57e514e-9abf-11f0-9832-fc3497b6c58b', '判断题', '179a325a8a02462184ea68ab669977c0', '软件测试的目的是证明程序没有错误。', '正确,错误', '1', '软件测试的目的是发现错误，而不是证明没有错误。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a57fda9f-9abf-11f0-9832-fc3497b6c58b', '单选题', '179a325a8a02462184ea68ab669977c0', '下列哪项不是面向对象的特点？', '封装,继承,多态,顺序', '3', '面向对象的三大特点是封装、继承和多态。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5819c10-9abf-11f0-9832-fc3497b6c58b', '多选题', '179a325a8a02462184ea68ab669977c0', '软件质量特性包括？', '功能性,可靠性,易用性,效率', '0,1,2,3', '软件质量特性包括功能性、可靠性、易用性、效率等。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5837419-9abf-11f0-9832-fc3497b6c58b', '单选题', '179a325a8a02462184ea68ab669977c0', '白盒测试主要关注？', '输入输出,内部逻辑,用户界面,性能指标', '1', '白盒测试关注程序内部逻辑和结构。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5853b72-9abf-11f0-9832-fc3497b6c58b', '多选题', '179a325a8a02462184ea68ab669977c0', '敏捷开发方法包括？', 'Scrum,XP,瀑布模型,迭代开发', '0,1', 'Scrum和XP是典型的敏捷开发方法。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a586e4d7-9abf-11f0-9832-fc3497b6c58b', '判断题', '179a325a8a02462184ea68ab669977c0', '软件重构会改变程序的外部行为。', '正确,错误', '1', '软件重构只改变内部结构，不改变外部行为。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a588ba7c-9abf-11f0-9832-fc3497b6c58b', '单选题', '179a325a8a02462184ea68ab669977c0', '软件配置管理的主要目的是？', '提高性能,控制变更,优化代码,减少成本', '1', '软件配置管理用于控制软件的变更过程。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a58a311e-9abf-11f0-9832-fc3497b6c58b', '多选题', '179a325a8a02462184ea68ab669977c0', '软件度量的指标包括？', '代码行数,圈复杂度,功能点,缺陷密度', '0,1,2,3', '软件度量可以从多个维度评估软件质量。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a58b6e2e-9abf-11f0-9832-fc3497b6c58b', '单选题', '179a325a8a02462184ea68ab669977c0', '下列哪项不是软件危机的主要表现？', '软件开发进度难以预测,软件质量难以保证,软件开发成本难以控制,软件开发工具过多', '3', '软件开发工具过多不是软件危机的表现。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a58cc68e-9abf-11f0-9832-fc3497b6c58b', '多选题', '179a325a8a02462184ea68ab669977c0', '软件维护的类型包括？', '改正性维护,适应性维护,完善性维护,预防性维护', '0,1,2,3', '软件维护包括四种主要类型。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a58e5e63-9abf-11f0-9832-fc3497b6c58b', '判断题', '179a325a8a02462184ea68ab669977c0', '软件工程只关注技术问题，不关注管理问题。', '正确,错误', '1', '软件工程既关注技术问题，也关注管理问题。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5907494-9abf-11f0-9832-fc3497b6c58b', '单选题', '179a325a8a02462184ea68ab669977c0', '下列哪项不是软件过程模型？', '瀑布模型,螺旋模型,敏捷模型,编译模型', '3', '编译模型不是软件过程模型。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5927bb7-9abf-11f0-9832-fc3497b6c58b', '多选题', '179a325a8a02462184ea68ab669977c0', '软件风险管理的步骤包括？', '风险识别,风险分析,风险规划,风险监控', '0,1,2,3', '风险管理包括识别、分析、规划和监控四个步骤。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a593c845-9abf-11f0-9832-fc3497b6c58b', '单选题', '47675c6483ef4073b7cdcc3fa4f59f68', 'CPU的主要功能是？', '存储数据,执行指令,显示图像,输出声音', '1', 'CPU是中央处理器，负责执行程序指令。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5959c38-9abf-11f0-9832-fc3497b6c58b', '多选题', '47675c6483ef4073b7cdcc3fa4f59f68', '下列属于输出设备的是？', '键盘,显示器,打印机,鼠标', '1,2', '显示器和打印机用于输出信息。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5971b17-9abf-11f0-9832-fc3497b6c58b', '判断题', '47675c6483ef4073b7cdcc3fa4f59f68', 'RAM是只读存储器。', '正确,错误', '1', 'RAM是随机存取存储器，可读写。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a598f697-9abf-11f0-9832-fc3497b6c58b', '单选题', '47675c6483ef4073b7cdcc3fa4f59f68', '二进制数1101对应的十进制是？', '10,11,12,13', '3', '1*8+1*4+0*2+1*1=13', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a59aa51e-9abf-11f0-9832-fc3497b6c58b', '多选题', '47675c6483ef4073b7cdcc3fa4f59f68', '操作系统的主要功能包括？', '进程管理,内存管理,文件管理,设备管理', '0,1,2,3', '操作系统负责管理所有硬件和软件资源。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a59c340d-9abf-11f0-9832-fc3497b6c58b', '单选题', '47675c6483ef4073b7cdcc3fa4f59f68', '下列哪项不是计算机硬件组成部分？', 'CPU,内存,操作系统,硬盘', '2', '操作系统是软件，不是硬件组成部分。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a59e19fa-9abf-11f0-9832-fc3497b6c58b', '多选题', '47675c6483ef4073b7cdcc3fa4f59f68', '下列属于输入设备的是？', '键盘,鼠标,扫描仪,显示器', '0,1,2', '键盘、鼠标和扫描仪是输入设备，显示器是输出设备。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a59fb347-9abf-11f0-9832-fc3497b6c58b', '判断题', '47675c6483ef4073b7cdcc3fa4f59f68', 'ROM是只读存储器，内容不可更改。', '正确,错误', '0', 'ROM是只读存储器，内容在出厂时固定。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5a134e0-9abf-11f0-9832-fc3497b6c58b', '单选题', '47675c6483ef4073b7cdcc3fa4f59f68', '1GB等于多少MB？', '100,256,512,1024', '3', '1GB=1024MB', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5a2af2f-9abf-11f0-9832-fc3497b6c58b', '多选题', '47675c6483ef4073b7cdcc3fa4f59f68', '计算机总线类型包括？', '数据总线,地址总线,控制总线,电源总线', '0,1,2', '计算机总线包括数据总线、地址总线和控制总线。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5a4d7d7-9abf-11f0-9832-fc3497b6c58b', '单选题', '47675c6483ef4073b7cdcc3fa4f59f68', '下列哪项不是操作系统？', 'Windows,Linux,Excel,MacOS', '2', 'Excel是应用程序，不是操作系统。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5a6d96d-9abf-11f0-9832-fc3497b6c58b', '多选题', '47675c6483ef4073b7cdcc3fa4f59f68', '计算机网络的拓扑结构包括？', '星型,总线型,环型,网状', '0,1,2,3', '常见的网络拓扑结构包括星型、总线型、环型和网状。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5a83d6a-9abf-11f0-9832-fc3497b6c58b', '判断题', '47675c6483ef4073b7cdcc3fa4f59f68', 'CPU的主频越高，计算机性能就一定越好。', '正确,错误', '1', 'CPU性能还受架构、缓存等因素影响，不是单纯看主频。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5a988ca-9abf-11f0-9832-fc3497b6c58b', '单选题', '47675c6483ef4073b7cdcc3fa4f59f68', '下列哪项是 volatile memory？', '硬盘,光盘,RAM,ROM', '2', 'RAM是易失性存储器，断电后数据丢失。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5aaafa6-9abf-11f0-9832-fc3497b6c58b', '多选题', '47675c6483ef4073b7cdcc3fa4f59f68', '计算机语言按层次分为？', '机器语言,汇编语言,高级语言,自然语言', '0,1,2', '计算机语言分为机器语言、汇编语言和高级语言。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5ac0359-9abf-11f0-9832-fc3497b6c58b', '单选题', '47675c6483ef4073b7cdcc3fa4f59f68', 'ASCII码用多少位二进制表示一个字符？', '4,7,8,16', '2', 'ASCII码最初使用7位，扩展ASCII使用8位。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5ad45e7-9abf-11f0-9832-fc3497b6c58b', '多选题', '47675c6483ef4073b7cdcc3fa4f59f68', '计算机病毒的特点包括？', '传染性,隐蔽性,破坏性,可触发性', '0,1,2,3', '计算机病毒具有传染性、隐蔽性、破坏性和可触发性。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5ae9f00-9abf-11f0-9832-fc3497b6c58b', '判断题', '47675c6483ef4073b7cdcc3fa4f59f68', '云计算是一种基于互联网的计算方式。', '正确,错误', '0', '云计算通过互联网提供计算资源和服务。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5b011d7-9abf-11f0-9832-fc3497b6c58b', '单选题', '47675c6483ef4073b7cdcc3fa4f59f68', '下列哪项不是数据库管理系统？', 'MySQL,Oracle,Word,SQL Server', '2', 'Word是文字处理软件，不是数据库管理系统。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5b19b7f-9abf-11f0-9832-fc3497b6c58b', '多选题', '47675c6483ef4073b7cdcc3fa4f59f68', '人工智能的主要应用领域包括？', '机器学习,自然语言处理,计算机视觉,专家系统', '0,1,2,3', '人工智能在多个领域都有广泛应用。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5b302be-9abf-11f0-9832-fc3497b6c58b', '单选题', '5bc67c96a1f540128fd9968a3eab0350', 'Java中基本数据类型不包括？', 'int,String,double,boolean', '1', 'String是类类型，不是基本数据类型。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5b474bf-9abf-11f0-9832-fc3497b6c58b', '多选题', '5bc67c96a1f540128fd9968a3eab0350', 'Python中可变数据类型有？', 'list,tuple,dict,set', '0,2,3', '列表、字典和集合是可变的，元组不可变。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5b5c8ab-9abf-11f0-9832-fc3497b6c58b', '判断题', '5bc67c96a1f540128fd9968a3eab0350', 'C++支持面向对象编程。', '正确,错误', '0', 'C++是面向对象的编程语言。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5b6f56b-9abf-11f0-9832-fc3497b6c58b', '单选题', '5bc67c96a1f540128fd9968a3eab0350', 'JavaScript中声明变量的关键字是？', 'var,let,const,以上都是', '3', 'ES6支持多种变量声明方式。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5b90e84-9abf-11f0-9832-fc3497b6c58b', '多选题', '5bc67c96a1f540128fd9968a3eab0350', 'Go语言的特点包括？', '并发编程,垃圾回收,指针运算,类继承', '0,1,2', 'Go支持并发和指针，但没有类继承概念。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5ba6943-9abf-11f0-9832-fc3497b6c58b', '单选题', '5bc67c96a1f540128fd9968a3eab0350', 'Python中使用哪个关键字定义函数？', 'function,def,define,func', '1', 'Python使用def关键字定义函数。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5bbaabd-9abf-11f0-9832-fc3497b6c58b', '多选题', '5bc67c96a1f540128fd9968a3eab0350', 'Java的访问修饰符包括？', 'public,private,protected,default', '0,1,2,3', 'Java有四种访问修饰符。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5bcfcfa-9abf-11f0-9832-fc3497b6c58b', '判断题', '5bc67c96a1f540128fd9968a3eab0350', 'Python是编译型语言。', '正确,错误', '1', 'Python是解释型语言。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5bec690-9abf-11f0-9832-fc3497b6c58b', '单选题', '5bc67c96a1f540128fd9968a3eab0350', 'C语言中用于输出到控制台的函数是？', 'print,printf,cout,console.log', '1', 'C语言使用printf函数输出。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5c0ec1c-9abf-11f0-9832-fc3497b6c58b', '多选题', '5bc67c96a1f540128fd9968a3eab0350', 'JavaScript的数据类型包括？', 'number,string,boolean,object', '0,1,2,3', 'JavaScript有多种数据类型。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5c31406-9abf-11f0-9832-fc3497b6c58b', '单选题', '5bc67c96a1f540128fd9968a3eab0350', '下列哪项不是Python的循环语句？', 'for,while,do-while,foreach', '2', 'Python没有do-while循环。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5c45b63-9abf-11f0-9832-fc3497b6c58b', '多选题', '5bc67c96a1f540128fd9968a3eab0350', '面向对象编程的三大特性是？', '封装,继承,多态,抽象', '0,1,2', '封装、继承和多态是面向对象的三大特性。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5c5d76a-9abf-11f0-9832-fc3497b6c58b', '判断题', '5bc67c96a1f540128fd9968a3eab0350', 'Java是纯面向对象语言。', '正确,错误', '0', 'Java的所有代码都必须写在类中。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5c79424-9abf-11f0-9832-fc3497b6c58b', '单选题', '5bc67c96a1f540128fd9968a3eab0350', 'HTML是用于？', '网页结构,样式设计,逻辑编程,数据库操作', '0', 'HTML用于定义网页的结构。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5c942ef-9abf-11f0-9832-fc3497b6c58b', '多选题', '5bc67c96a1f540128fd9968a3eab0350', 'CSS可以控制网页的？', '布局,颜色,字体,动画', '0,1,2,3', 'CSS用于控制网页的表现样式。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5caf033-9abf-11f0-9832-fc3497b6c58b', '单选题', '5bc67c96a1f540128fd9968a3eab0350', 'SQL中用于查询数据的关键字是？', 'SELECT,UPDATE,INSERT,DELETE', '0', 'SELECT用于数据查询。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5cc7c50-9abf-11f0-9832-fc3497b6c58b', '多选题', '5bc67c96a1f540128fd9968a3eab0350', '前端开发的主要技术包括？', 'HTML,CSS,JavaScript,MySQL', '0,1,2', 'HTML、CSS和JavaScript是前端三大技术。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5cdbf33-9abf-11f0-9832-fc3497b6c58b', '判断题', '5bc67c96a1f540128fd9968a3eab0350', 'Python的列表是有序的。', '正确,错误', '0', 'Python列表是有序的数据结构。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5cf03a8-9abf-11f0-9832-fc3497b6c58b', '单选题', '5bc67c96a1f540128fd9968a3eab0350', '下列哪项不是编程范式？', '面向对象,函数式,声明式,编译式', '3', '编译式是语言执行方式，不是编程范式。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5d03da6-9abf-11f0-9832-fc3497b6c58b', '多选题', '5bc67c96a1f540128fd9968a3eab0350', '代码优化的目标包括？', '提高性能,减少内存占用,提高可读性,减少代码行数', '0,1,2', '代码优化主要关注性能、内存和可读性。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5d1a50e-9abf-11f0-9832-fc3497b6c58b', '单选题', '980d855fb8504745818fc2a05e5732cd', '对称加密的特点是？', '加密解密密钥相同,加密解密密钥不同,速度较慢,适合密钥分发', '0', '对称加密使用相同密钥，速度快但密钥分发困难。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5d2fd42-9abf-11f0-9832-fc3497b6c58b', '多选题', '980d855fb8504745818fc2a05e5732cd', '常见网络攻击手段包括？', 'DDoS,SQL注入,phishing,防火墙', '0,1,2', 'DDoS、SQL注入和钓鱼都是常见攻击手段。', '2025-09-26 18:00:35', '2025-09-26 18:00:35');
INSERT INTO `questions` VALUES ('a5d44857-9abf-11f0-9832-fc3497b6c58b', '判断题', '980d855fb8504745818fc2a05e5732cd', 'MD5是安全的加密算法。', '正确,错误', '1', 'MD5存在碰撞漏洞，已不推荐用于安全加密。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5d5f813-9abf-11f0-9832-fc3497b6c58b', '单选题', '980d855fb8504745818fc2a05e5732cd', '数字证书用于验证？', '身份真实性,网络速度,存储容量,程序性能', '0', '数字证书验证身份的真实性和可靠性。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5d7ec5a-9abf-11f0-9832-fc3497b6c58b', '多选题', '980d855fb8504745818fc2a05e5732cd', '安全通信协议包括？', 'HTTP,HTTPS,SSL,TLS', '1,2,3', 'HTTPS、SSL和TLS都是安全通信协议。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5d99db8-9abf-11f0-9832-fc3497b6c58b', '单选题', '980d855fb8504745818fc2a05e5732cd', '下列哪项不是防火墙的功能？', '包过滤,病毒查杀,访问控制,日志记录', '1', '防火墙不负责病毒查杀。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5db7eed-9abf-11f0-9832-fc3497b6c58b', '多选题', '980d855fb8504745818fc2a05e5732cd', '信息安全的三要素是？', '机密性,完整性,可用性,可靠性', '0,1,2', 'CIA三要素：机密性、完整性、可用性。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5dced24-9abf-11f0-9832-fc3497b6c58b', '判断题', '980d855fb8504745818fc2a05e5732cd', '公钥加密比对称加密速度快。', '正确,错误', '1', '公钥加密速度较慢，对称加密速度快。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5de8f22-9abf-11f0-9832-fc3497b6c58b', '单选题', '980d855fb8504745818fc2a05e5732cd', '下列哪项是社交工程攻击？', 'DDoS,钓鱼攻击,SQL注入,缓冲区溢出', '1', '钓鱼攻击属于社交工程。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5dfff01-9abf-11f0-9832-fc3497b6c58b', '多选题', '980d855fb8504745818fc2a05e5732cd', '身份认证的方式包括？', '密码认证,生物特征,数字证书,IP地址', '0,1,2', '密码、生物特征和数字证书都是认证方式。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5e1eafd-9abf-11f0-9832-fc3497b6c58b', '单选题', '980d855fb8504745818fc2a05e5732cd', 'VPN的主要作用是？', '提高网速,加密通信,减少延迟,增加存储', '1', 'VPN用于建立安全的加密通信通道。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5e31ebd-9abf-11f0-9832-fc3497b6c58b', '多选题', '980d855fb8504745818fc2a05e5732cd', '安全审计的内容包括？', '日志分析,漏洞扫描,渗透测试,性能测试', '0,1,2', '安全审计包括日志分析、漏洞扫描和渗透测试。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5e4c3bc-9abf-11f0-9832-fc3497b6c58b', '判断题', '980d855fb8504745818fc2a05e5732cd', '所有的病毒都是恶意软件。', '正确,错误', '0', '病毒是恶意软件的一种类型。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5e603dc-9abf-11f0-9832-fc3497b6c58b', '单选题', '980d855fb8504745818fc2a05e5732cd', '下列哪项不是安全编码的原则？', '最小权限,纵深防御,最大复杂度,输入验证', '2', '安全编码要求简单清晰，不是最大复杂度。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5e7ab82-9abf-11f0-9832-fc3497b6c58b', '多选题', '980d855fb8504745818fc2a05e5732cd', '数据加密的用途包括？', '保密性,完整性,身份认证,访问控制', '0,1,2', '加密用于保证保密性、完整性和身份认证。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5e8dbe1-9abf-11f0-9832-fc3497b6c58b', '单选题', '980d855fb8504745818fc2a05e5732cd', '下列哪项是网络层安全协议？', 'SSL,IPSec,TLS,HTTPS', '1', 'IPSec是网络层安全协议。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5ea3652-9abf-11f0-9832-fc3497b6c58b', '多选题', '980d855fb8504745818fc2a05e5732cd', '安全风险评估的步骤包括？', '资产识别,威胁分析,脆弱性分析,风险处理', '0,1,2,3', '风险评估包括资产识别、威胁分析、脆弱性分析和风险处理。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5ebfe5d-9abf-11f0-9832-fc3497b6c58b', '判断题', '980d855fb8504745818fc2a05e5732cd', '双因子认证比单密码认证更安全。', '正确,错误', '0', '双因子认证提供更高的安全性。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5ed50ff-9abf-11f0-9832-fc3497b6c58b', '单选题', '980d855fb8504745818fc2a05e5732cd', '下列哪项不是常见的安全漏洞？', 'XSS,CSRF,SQL注入,HTML解析', '3', 'HTML解析不是安全漏洞类型。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5eeac1d-9abf-11f0-9832-fc3497b6c58b', '多选题', '980d855fb8504745818fc2a05e5732cd', '安全事件响应的步骤包括？', '准备,检测,遏制,恢复', '0,1,2,3', '安全事件响应包括准备、检测、遏制和恢复。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5f03c3c-9abf-11f0-9832-fc3497b6c58b', '单选题', 'be1e29fc527c4727aae7350f7211fc65', 'SQL中查询数据使用？', 'SELECT,UPDATE,INSERT,DELETE', '0', 'SELECT用于数据查询操作。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5f2d2bf-9abf-11f0-9832-fc3497b6c58b', '多选题', 'be1e29fc527c4727aae7350f7211fc65', '关系数据库的特点包括？', '结构化存储,数据独立性,事务支持,非关系模型', '0,1,2', '关系数据库支持结构化存储、独立性和事务。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5f476c5-9abf-11f0-9832-fc3497b6c58b', '判断题', 'be1e29fc527c4727aae7350f7211fc65', 'NoSQL数据库都不支持事务。', '正确,错误', '1', '部分NoSQL数据库支持有限的事务功能。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5f5ebab-9abf-11f0-9832-fc3497b6c58b', '单选题', 'be1e29fc527c4727aae7350f7211fc65', '数据库索引的主要作用是？', '增加存储空间,提高查询速度,备份数据,加密数据', '1', '索引可大幅提高数据检索效率。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5f76f19-9abf-11f0-9832-fc3497b6c58b', '多选题', 'be1e29fc527c4727aae7350f7211fc65', '数据库事务的ACID特性包括？', '原子性,一致性,隔离性,持久性', '0,1,2,3', 'ACID是原子性、一致性、隔离性、持久性的缩写。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5f8e333-9abf-11f0-9832-fc3497b6c58b', '单选题', 'be1e29fc527c4727aae7350f7211fc65', '下列哪项不是SQL语言类型？', 'DDL,DML,DCL,HTML', '3', 'HTML不是SQL语言类型。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5fa9e4b-9abf-11f0-9832-fc3497b6c58b', '多选题', 'be1e29fc527c4727aae7350f7211fc65', '数据库规范化的重要性包括？', '减少数据冗余,提高数据一致性,改善查询性能,增加存储空间', '0,1,2', '规范化可以减少冗余、提高一致性和改善性能。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5fc25f8-9abf-11f0-9832-fc3497b6c58b', '判断题', 'be1e29fc527c4727aae7350f7211fc65', '视图是物理存储的表。', '正确,错误', '1', '视图是虚拟表，不物理存储数据。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5fdbfeb-9abf-11f0-9832-fc3497b6c58b', '单选题', 'be1e29fc527c4727aae7350f7211fc65', '下列哪项是NoSQL数据库？', 'MySQL,MongoDB,Oracle,SQL Server', '1', 'MongoDB是文档型NoSQL数据库。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a5ff4837-9abf-11f0-9832-fc3497b6c58b', '多选题', 'be1e29fc527c4727aae7350f7211fc65', '数据库连接类型包括？', '内连接,左连接,右连接,全连接', '0,1,2,3', 'SQL支持多种连接方式。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a6007086-9abf-11f0-9832-fc3497b6c58b', '单选题', 'be1e29fc527c4727aae7350f7211fc65', 'SQL中用于排序的关键字是？', 'ORDER BY,GROUP BY,WHERE,HAVING', '0', 'ORDER BY用于对结果排序。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a601989d-9abf-11f0-9832-fc3497b6c58b', '多选题', 'be1e29fc527c4727aae7350f7211fc65', '数据库备份类型包括？', '完全备份,增量备份,差异备份,日志备份', '0,1,2,3', '数据库有多种备份策略。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a602c45a-9abf-11f0-9832-fc3497b6c58b', '判断题', 'be1e29fc527c4727aae7350f7211fc65', '主键允许为空值。', '正确,错误', '1', '主键不能为空，必须唯一标识每条记录。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a603ded7-9abf-11f0-9832-fc3497b6c58b', '单选题', 'be1e29fc527c4727aae7350f7211fc65', '下列哪项不是数据库对象？', '表,视图,存储过程,编译器', '3', '编译器不是数据库对象。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a605470e-9abf-11f0-9832-fc3497b6c58b', '多选题', 'be1e29fc527c4727aae7350f7211fc65', '数据库事务的隔离级别包括？', '读未提交,读已提交,可重复读,串行化', '0,1,2,3', 'SQL标准定义了四种隔离级别。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a60670f0-9abf-11f0-9832-fc3497b6c58b', '单选题', 'be1e29fc527c4727aae7350f7211fc65', 'SQL中用于分组的关键字是？', 'GROUP BY,ORDER BY,WHERE,HAVING', '0', 'GROUP BY用于对结果分组。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a607a657-9abf-11f0-9832-fc3497b6c58b', '多选题', 'be1e29fc527c4727aae7350f7211fc65', '数据库性能优化方法包括？', '建立索引,查询优化,分区表,规范化', '0,1,2,3', '多种方法可以优化数据库性能。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a608fc83-9abf-11f0-9832-fc3497b6c58b', '判断题', 'be1e29fc527c4727aae7350f7211fc65', '外键用于建立表之间的关系。', '正确,错误', '0', '外键用于维护表间的引用完整性。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a60a4ec0-9abf-11f0-9832-fc3497b6c58b', '单选题', 'be1e29fc527c4727aae7350f7211fc65', '下列哪项是数据库管理系统的功能？', '数据定义,数据操纵,数据控制,以上都是', '3', 'DBMS提供完整的数据管理功能。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a60ba4b3-9abf-11f0-9832-fc3497b6c58b', '多选题', 'be1e29fc527c4727aae7350f7211fc65', '数据库安全措施包括？', '用户权限管理,数据加密,审计日志,网络隔离', '0,1,2,3', '多种措施可以保障数据库安全。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a60cd324-9abf-11f0-9832-fc3497b6c58b', '单选题', 'd779eef6d38149b89e85f0e35ae65c44', 'TCP/IP模型中网络层对应？', '物理层,数据链路层,网络层,传输层', '2', 'OSI网络层对应TCP/IP的网络层。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a60e58b9-9abf-11f0-9832-fc3497b6c58b', '多选题', 'd779eef6d38149b89e85f0e35ae65c44', '下列属于应用层协议的是？', 'HTTP,FTP,TCP,IP', '0,1', 'HTTP和FTP是应用层协议，TCP和IP是传输层和网络层。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a60f7d3d-9abf-11f0-9832-fc3497b6c58b', '判断题', 'd779eef6d38149b89e85f0e35ae65c44', 'IPv6地址长度为128位。', '正确,错误', '0', 'IPv6采用128位地址解决地址耗尽问题。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a610f787-9abf-11f0-9832-fc3497b6c58b', '单选题', 'd779eef6d38149b89e85f0e35ae65c44', 'DNS的主要功能是？', '域名解析,数据加密,流量控制,错误检测', '0', 'DNS将域名转换为IP地址。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a6121ad8-9abf-11f0-9832-fc3497b6c58b', '多选题', 'd779eef6d38149b89e85f0e35ae65c44', '网络拓扑结构包括？', '星型,总线型,环型,网状', '0,1,2,3', '常见网络拓扑包括星型、总线、环型和网状结构。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a613619d-9abf-11f0-9832-fc3497b6c58b', '单选题', 'd779eef6d38149b89e85f0e35ae65c44', '下列哪项是传输层协议？', 'IP,ICMP,TCP,ARP', '2', 'TCP是传输层协议。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a614ef2a-9abf-11f0-9832-fc3497b6c58b', '多选题', 'd779eef6d38149b89e85f0e35ae65c44', 'OSI参考模型包括哪些层？', '物理层,数据链路层,网络层,传输层', '0,1,2,3', 'OSI模型有7层，包括物理、数据链路、网络和传输层。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a6164e42-9abf-11f0-9832-fc3497b6c58b', '判断题', 'd779eef6d38149b89e85f0e35ae65c44', 'HTTP是安全协议。', '正确,错误', '1', 'HTTP是明文传输，HTTPS才是安全协议。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a6176ea5-9abf-11f0-9832-fc3497b6c58b', '单选题', 'd779eef6d38149b89e85f0e35ae65c44', '下列哪项是网络设备？', '路由器,交换机,集线器,以上都是', '3', '路由器、交换机和集线器都是网络设备。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a61891a5-9abf-11f0-9832-fc3497b6c58b', '多选题', 'd779eef6d38149b89e85f0e35ae65c44', 'TCP的特点包括？', '面向连接,可靠传输,流量控制,拥塞控制', '0,1,2,3', 'TCP提供可靠的、面向连接的传输服务。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a61a4c85-9abf-11f0-9832-fc3497b6c58b', '单选题', 'd779eef6d38149b89e85f0e35ae65c44', 'IP地址192.168.1.1属于哪类地址？', 'A类,B类,C类,D类', '2', 'C类地址范围是192.0.0.0-223.255.255.255', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a61bf640-9abf-11f0-9832-fc3497b6c58b', '多选题', 'd779eef6d38149b89e85f0e35ae65c44', '无线网络技术包括？', 'Wi-Fi,蓝牙,ZigBee,NFC', '0,1,2,3', '这些都是常见的无线网络技术。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a61d28b3-9abf-11f0-9832-fc3497b6c58b', '判断题', 'd779eef6d38149b89e85f0e35ae65c44', '子网掩码用于划分IP地址的网络部分和主机部分。', '正确,错误', '0', '子网掩码标识IP地址中网络位的长度。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a61e8451-9abf-11f0-9832-fc3497b6c58b', '单选题', 'd779eef6d38149b89e85f0e35ae65c44', '下列哪项是应用层协议？', 'TCP,IP,HTTP,ICMP', '2', 'HTTP是应用层协议。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a61fbabf-9abf-11f0-9832-fc3497b6c58b', '多选题', 'd779eef6d38149b89e85f0e35ae65c44', '网络安全的威胁包括？', '病毒,木马,黑客攻击,自然灾害', '0,1,2', '病毒、木马和黑客攻击是网络安全威胁。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a62183db-9abf-11f0-9832-fc3497b6c58b', '单选题', 'd779eef6d38149b89e85f0e35ae65c44', '下列哪项不是网络拓扑结构？', '星型,总线型,环型,线性', '3', '线性不是标准的网络拓扑结构。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a622d15e-9abf-11f0-9832-fc3497b6c58b', '多选题', 'd779eef6d38149b89e85f0e35ae65c44', '网络协议的三要素是？', '语法,语义,时序,编码', '0,1,2', '网络协议包括语法、语义和时序三要素。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a6247b36-9abf-11f0-9832-fc3497b6c58b', '判断题', 'd779eef6d38149b89e85f0e35ae65c44', 'UDP提供可靠的数据传输服务。', '正确,错误', '1', 'UDP是无连接的不可靠传输协议。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a625b650-9abf-11f0-9832-fc3497b6c58b', '单选题', 'd779eef6d38149b89e85f0e35ae65c44', '下列哪项是数据链路层设备？', '路由器,交换机,集线器,网桥', '3', '网桥是数据链路层设备。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');
INSERT INTO `questions` VALUES ('a626d072-9abf-11f0-9832-fc3497b6c58b', '多选题', 'd779eef6d38149b89e85f0e35ae65c44', 'Internet接入方式包括？', 'ADSL,光纤, Cable,卫星', '0,1,2,3', '这些都是常见的Internet接入方式。', '2025-09-26 18:00:36', '2025-09-26 18:00:36');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2025-08-30 08:59:54', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (2, '验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-11 13:30:28', '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (3, '是否开启用户注册功能', 'sys.account.registerUser', 'true', 'Y', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-11 13:31:46', '是否开启注册用户功能（true开启，false关闭）');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 202 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '花菜编程', 0, 'huacai', '15888888888', 'huacai@qq.com', '0', '0', 'admin', '2025-08-30 08:59:53', 'admin', '2025-08-30 10:36:00');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 111 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (9, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (100, 1, '单选题', '单选题', 'question_type', NULL, 'primary', 'N', '0', 'admin', '2025-09-26 15:59:29', 'admin', '2025-09-26 15:59:51', NULL);
INSERT INTO `sys_dict_data` VALUES (101, 2, '多选题', '多选题', 'question_type', NULL, 'primary', 'N', '0', 'admin', '2025-09-26 16:00:00', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (102, 3, '判断题', '判断题', 'question_type', NULL, 'primary', 'N', '0', 'admin', '2025-09-26 16:00:07', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (103, 1, '进行中', '进行中', 'exam_status', NULL, 'warning', 'N', '0', 'admin', '2025-09-29 16:21:35', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (104, 2, '考试通过', '考试通过', 'exam_status', NULL, 'success', 'N', '0', 'admin', '2025-09-29 16:21:50', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (105, 3, '考试未通过', '考试未通过', 'exam_status', NULL, 'danger', 'N', '0', 'admin', '2025-09-29 16:21:58', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (106, 1, '正确', '正确', 'is_correct', NULL, 'success', 'N', '0', 'admin', '2025-09-30 22:52:15', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (107, 2, '错误', '错误', 'is_correct', NULL, 'danger', 'N', '0', 'admin', '2025-09-30 22:52:25', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (108, 1, '方法篇', '方法篇', 'guide_type', NULL, 'primary', 'N', '0', 'admin', '2025-10-02 00:03:19', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (109, 2, '技巧篇', '技巧篇', 'guide_type', NULL, 'primary', 'N', '0', 'admin', '2025-10-02 00:03:26', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (110, 3, '心态篇', '心态篇', 'guide_type', NULL, 'primary', 'N', '0', 'admin', '2025-10-02 00:03:32', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '系统是否', 'sys_yes_no', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (100, '题型', 'question_type', '0', 'admin', '2025-09-26 15:58:45', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (101, '用户考试状态', 'exam_status', '0', 'admin', '2025-09-29 16:21:21', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (102, '是否正确', 'is_correct', '0', 'admin', '2025-09-30 22:52:06', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (103, '学习指南', 'guide_type', '0', 'admin', '2025-10-02 00:03:10', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '路由名称',
  `is_frame` int NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int NULL DEFAULT 1 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2050 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 999, 'system', NULL, '', '', 1, 0, 'M', '0', '0', '', '系统管理', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-25 21:37:48', '系统管理目录');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 1, 1, 'C', '0', '0', 'system:user:list', '用户管理', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:20:55', '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', 1, 1, 'C', '0', '0', 'system:role:list', '角色管理', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:26:22', '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', 1, 1, 'C', '0', '0', 'system:menu:list', '菜单管理', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:26:28', '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', 1, 1, 'C', '0', '0', 'system:dept:list', '部门管理', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:26:33', '部门管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', 1, 1, 'C', '0', '0', 'system:dict:list', '字典管理', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:26:38', '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', 1, 1, 'C', '0', '0', 'system:config:list', '参数设置', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:26:44', '参数设置菜单');
INSERT INTO `sys_menu` VALUES (116, '代码生成', 1, 8, 'gen', 'tool/gen/index', '', '', 1, 1, 'C', '0', '0', 'tool:gen:list', '代码生成', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:26:49', '代码生成菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '参数查询', 106, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数新增', 106, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数修改', 106, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数删除', 106, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数导出', 106, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成查询', 116, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成修改', 116, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '生成删除', 116, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '导入代码', 116, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '预览代码', 116, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1060, '生成代码', 116, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2006, '题目分类管理', 2012, 1, 'category', 'exam/category/index', NULL, '', 1, 1, 'C', '0', '0', 'exam:category:list', '题目分类管理', 'admin', '2025-09-25 21:36:40', 'admin', '2025-09-25 21:41:56', '题目分类菜单');
INSERT INTO `sys_menu` VALUES (2007, '题目分类查询', 2006, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:category:query', '#', 'admin', '2025-09-25 21:36:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2008, '题目分类新增', 2006, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:category:add', '#', 'admin', '2025-09-25 21:36:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2009, '题目分类修改', 2006, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:category:edit', '#', 'admin', '2025-09-25 21:36:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2010, '题目分类删除', 2006, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:category:remove', '#', 'admin', '2025-09-25 21:36:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2011, '题目分类导出', 2006, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:category:export', '#', 'admin', '2025-09-25 21:36:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2012, '基本信息管理', 0, 1, 'examInfo', NULL, NULL, '', 1, 1, 'M', '0', '0', '', '基本信息管理', 'admin', '2025-09-25 21:38:21', 'admin', '2025-09-25 21:41:47', '');
INSERT INTO `sys_menu` VALUES (2013, '题目管理', 2012, 2, 'questions', 'exam/questions/index', NULL, '', 1, 1, 'C', '0', '0', 'exam:questions:list', '题目管理', 'admin', '2025-09-26 16:03:26', 'admin', '2025-09-26 16:07:07', '题目菜单');
INSERT INTO `sys_menu` VALUES (2014, '题目查询', 2013, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:questions:query', '#', 'admin', '2025-09-26 16:03:26', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2015, '题目新增', 2013, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:questions:add', '#', 'admin', '2025-09-26 16:03:26', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2016, '题目修改', 2013, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:questions:edit', '#', 'admin', '2025-09-26 16:03:26', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2017, '题目删除', 2013, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:questions:remove', '#', 'admin', '2025-09-26 16:03:26', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2018, '题目导出', 2013, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:questions:export', '#', 'admin', '2025-09-26 16:03:26', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2019, '考试管理', 2025, 1, 'exam', 'exam/exam/index', NULL, '', 1, 1, 'C', '0', '0', 'exam:exam:list', '考试管理', 'admin', '2025-09-27 14:27:20', 'admin', '2025-09-27 14:31:41', '考试菜单');
INSERT INTO `sys_menu` VALUES (2020, '考试查询', 2019, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:exam:query', '#', 'admin', '2025-09-27 14:27:20', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2021, '考试新增', 2019, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:exam:add', '#', 'admin', '2025-09-27 14:27:20', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2022, '考试修改', 2019, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:exam:edit', '#', 'admin', '2025-09-27 14:27:20', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2023, '考试删除', 2019, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:exam:remove', '#', 'admin', '2025-09-27 14:27:20', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2024, '考试导出', 2019, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:exam:export', '#', 'admin', '2025-09-27 14:27:20', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2025, '考试业务管理', 0, 2, 'examService', NULL, NULL, '', 1, 1, 'M', '0', '0', '', '考试业务管理', 'admin', '2025-09-27 14:29:47', 'admin', '2025-09-27 14:31:36', '');
INSERT INTO `sys_menu` VALUES (2026, '试题管理', 2025, 2, 'eq', 'exam/eq/index', NULL, '', 1, 1, 'C', '0', '0', 'exam:eq:list', '试题管理', 'admin', '2025-09-27 16:03:21', 'admin', '2025-09-27 16:06:36', '试题菜单');
INSERT INTO `sys_menu` VALUES (2027, '试题查询', 2026, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:eq:query', '#', 'admin', '2025-09-27 16:03:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2028, '试题新增', 2026, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:eq:add', '#', 'admin', '2025-09-27 16:03:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2029, '试题修改', 2026, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:eq:edit', '#', 'admin', '2025-09-27 16:03:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2030, '试题删除', 2026, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:eq:remove', '#', 'admin', '2025-09-27 16:03:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2031, '试题导出', 2026, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:eq:export', '#', 'admin', '2025-09-27 16:03:21', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2032, '考试用户分配管理', 2025, 3, 'eu', 'exam/eu/index', NULL, '', 1, 1, 'C', '0', '0', 'exam:eu:list', '考试用户分配管理', 'admin', '2025-09-29 16:28:42', 'admin', '2025-09-29 22:02:48', '考试用户分配菜单');
INSERT INTO `sys_menu` VALUES (2033, '考试用户分配查询', 2032, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:eu:query', '#', 'admin', '2025-09-29 16:28:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2034, '考试用户分配新增', 2032, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:eu:add', '#', 'admin', '2025-09-29 16:28:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2035, '考试用户分配修改', 2032, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:eu:edit', '#', 'admin', '2025-09-29 16:28:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2036, '考试用户分配删除', 2032, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:eu:remove', '#', 'admin', '2025-09-29 16:28:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2037, '考试用户分配导出', 2032, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:eu:export', '#', 'admin', '2025-09-29 16:28:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2038, '考试结果管理', 2025, 4, 'er', 'exam/er/index', NULL, '', 1, 1, 'C', '0', '0', 'exam:er:list', '考试结果管理', 'admin', '2025-09-30 22:54:08', 'admin', '2025-09-30 22:57:17', '考试结果菜单');
INSERT INTO `sys_menu` VALUES (2039, '考试结果查询', 2038, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:er:query', '#', 'admin', '2025-09-30 22:54:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2040, '考试结果新增', 2038, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:er:add', '#', 'admin', '2025-09-30 22:54:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2041, '考试结果修改', 2038, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:er:edit', '#', 'admin', '2025-09-30 22:54:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2042, '考试结果删除', 2038, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:er:remove', '#', 'admin', '2025-09-30 22:54:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2043, '考试结果导出', 2038, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:er:export', '#', 'admin', '2025-09-30 22:54:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2044, '学习指南管理', 2012, 3, 'guide', 'exam/guide/index', NULL, '', 1, 1, 'C', '0', '0', 'exam:guide:list', '学习指南管理', 'admin', '2025-10-02 00:05:13', 'admin', '2025-10-02 00:07:28', '学习指南菜单');
INSERT INTO `sys_menu` VALUES (2045, '学习指南查询', 2044, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:guide:query', '#', 'admin', '2025-10-02 00:05:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2046, '学习指南新增', 2044, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:guide:add', '#', 'admin', '2025-10-02 00:05:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2047, '学习指南修改', 2044, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:guide:edit', '#', 'admin', '2025-10-02 00:05:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2048, '学习指南删除', 2044, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:guide:remove', '#', 'admin', '2025-10-02 00:05:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2049, '学习指南导出', 2044, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'exam:guide:export', '#', 'admin', '2025-10-02 00:05:13', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '2', 'admin', '2025-08-30 08:59:54', 'admin', '2025-08-30 10:34:10', '普通角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '角色和部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 107 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 100, 'admin', '花菜', '00', 'huacai@163.com', '15888888888', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2025-10-03 21:36:38', 'admin', '2025-08-30 08:59:53', '', '2025-10-03 21:36:38', '管理员');
INSERT INTO `sys_user` VALUES (104, NULL, '张三', '张三', '00', '', '', '0', '', '$2a$10$NwmQj7qrWhGFl01/tKd1LeHoPvncgO1bHOiJGVt/NUVqb0B36uthK', '0', '0', '127.0.0.1', '2025-10-01 23:36:59', '', '2025-09-29 16:50:35', '', '2025-10-01 23:36:58', NULL);
INSERT INTO `sys_user` VALUES (105, NULL, '李四', '李四', '00', '', '', '0', '', '$2a$10$Y6hOz7LJa11Qh3bqAilzHu1ZNESVoVaCyhlTIZb4vAw6fL078hfmm', '0', '0', '127.0.0.1', '2025-10-01 23:36:43', '', '2025-09-29 16:51:08', '', '2025-10-01 23:36:42', NULL);
INSERT INTO `sys_user` VALUES (106, NULL, '王五', '王五', '00', '', '', '0', '', '$2a$10$bhAnloxJGIijjvx0qW8ue.oQ/Vt6AI.E4rxzC4pYVMqQDbbJXR8Ca', '0', '0', '', NULL, '', '2025-09-29 16:51:18', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);

SET FOREIGN_KEY_CHECKS = 1;
