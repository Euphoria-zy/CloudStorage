/*
 Navicat Premium Data Transfer

 Source Server         : zy
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : cloud_db

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 13/02/2023 09:25:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_store
-- ----------------------------
DROP TABLE IF EXISTS `file_store`;
CREATE TABLE `file_store`  (
  `file_store_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件仓库id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `current_size` double NOT NULL COMMENT '当前仓库容量',
  `max_size` int(11) NULL DEFAULT 1048576 COMMENT '最大容量（单位KB）',
  PRIMARY KEY (`file_store_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of file_store
-- ----------------------------
INSERT INTO `file_store` VALUES (7, 12, 861, 1048756);
INSERT INTO `file_store` VALUES (9, 14, 0, 1048576);

-- ----------------------------
-- Table structure for folder
-- ----------------------------
DROP TABLE IF EXISTS `folder`;
CREATE TABLE `folder`  (
  `folder_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件夹id',
  `folder_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件夹名称',
  `parent_folder_id` int(11) NOT NULL COMMENT '父文件夹id',
  `file_store_id` int(11) NOT NULL COMMENT '文件仓库id',
  `folder_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件夹路径',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`folder_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of folder
-- ----------------------------
INSERT INTO `folder` VALUES (1, 'zhou', 0, 7, '18372603972/', '2023-01-21 09:23:00');
INSERT INTO `folder` VALUES (2, '漫威', 0, 7, '18372603972/', '2023-01-21 09:23:00');
INSERT INTO `folder` VALUES (3, 'yun', 1, 7, '18372603972/zhou/', '2023-01-21 10:48:00');
INSERT INTO `folder` VALUES (4, '音乐', 3, 7, '18372603972/zhou/yun/', '2023-01-21 12:48:00');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id，主键',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '电子邮箱',
  `file_store_id` int(32) NULL DEFAULT NULL COMMENT '文件仓库id',
  `image_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像地址',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (12, '18372603972', '123456', 'zh99499@163.com', 7, NULL);
INSERT INTO `user` VALUES (14, '18993339604', '123456', 'zh99499@163.com', 9, NULL);

-- ----------------------------
-- Table structure for user_file
-- ----------------------------
DROP TABLE IF EXISTS `user_file`;
CREATE TABLE `user_file`  (
  `file_id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `file_type` int(32) NULL DEFAULT NULL COMMENT '文件类型（1-文档doc, 2-pdf，3-音乐music）,4-视频video，5-图片image,6-其他）',
  `file_store_id` int(11) NULL DEFAULT NULL COMMENT '文件仓库id',
  `parent_folder_id` int(11) NOT NULL COMMENT '父文件夹id',
  `download_count` int(32) NULL DEFAULT NULL COMMENT '下载次数',
  `upload_time` datetime(0) NULL DEFAULT NULL COMMENT '上传时间',
  `file_size` double NULL DEFAULT NULL COMMENT '文件大小',
  `postfix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件后缀',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件路径',
  PRIMARY KEY (`file_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_file
-- ----------------------------
INSERT INTO `user_file` VALUES (1, '123', 2, 7, 3, 1, '2023-01-21 12:00:00', 652, '.jpg', '18372603972/zhou/yun/');
INSERT INTO `user_file` VALUES (2, '611362', 2, 7, 3, 2, '2023-01-21 12:44:00', 203, '.jpg', '18372603972/zhou/yun/');

SET FOREIGN_KEY_CHECKS = 1;
