/*
 Navicat Premium Data Transfer

 Source Server         : Mysql
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : dorm_manager

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 28/12/2023 10:31:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for db_account
-- ----------------------------
DROP TABLE IF EXISTS `db_account`;
CREATE TABLE `db_account`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户角色',
  `register_time` datetime(0) NULL DEFAULT NULL COMMENT '注册时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_account
-- ----------------------------
INSERT INTO `db_account` VALUES (1, 'test123', '$2a$10$zsmnikfIwSdNedalO1tFPueE1WSBbBOEWhSZauqamsVHiVxZ7WMN.', '373449@qq.com', 'user', '2023-12-21 14:51:45');
INSERT INTO `db_account` VALUES (2, 'testuser', '$2a$10$HpiEAVBCuuUggPrW34PlheWn2zF8zCb8Jm9iC5S1CAPUd6mn5NADe', '1120505436@qq.com', 'user', '2023-12-28 09:38:25');

-- ----------------------------
-- Table structure for db_bedassignments
-- ----------------------------
DROP TABLE IF EXISTS `db_bedassignments`;
CREATE TABLE `db_bedassignments`  (
  `AssignmentID` int(11) NOT NULL COMMENT '分配ID，主键',
  `StudentID` int(11) NULL DEFAULT NULL COMMENT '学生ID，外键',
  `DormitoryID` int(11) NULL DEFAULT NULL COMMENT '宿舍ID，外键',
  `BedNumber` int(11) NULL DEFAULT NULL COMMENT '床位号',
  PRIMARY KEY (`AssignmentID`) USING BTREE,
  INDEX `StudentID`(`StudentID`) USING BTREE,
  INDEX `DormitoryID`(`DormitoryID`) USING BTREE,
  CONSTRAINT `db_bedassignments_ibfk_1` FOREIGN KEY (`StudentID`) REFERENCES `db_students` (`StudentID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `db_bedassignments_ibfk_2` FOREIGN KEY (`DormitoryID`) REFERENCES `db_dormitories` (`DormitoryID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_bedassignments
-- ----------------------------

-- ----------------------------
-- Table structure for db_dormitories
-- ----------------------------
DROP TABLE IF EXISTS `db_dormitories`;
CREATE TABLE `db_dormitories`  (
  `DormitoryID` int(11) NOT NULL COMMENT '宿舍ID，主键',
  `BuildingNumber` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '宿舍楼号',
  `DormitoryNumber` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '宿舍号',
  `Capacity` int(11) NULL DEFAULT NULL COMMENT '最大容纳人数',
  `OccupiedBeds` int(11) NULL DEFAULT NULL COMMENT '已占用床位数',
  `TotalBeds` int(11) NULL DEFAULT NULL COMMENT '总床位数',
  PRIMARY KEY (`DormitoryID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_dormitories
-- ----------------------------

-- ----------------------------
-- Table structure for db_dormitoryassignments
-- ----------------------------
DROP TABLE IF EXISTS `db_dormitoryassignments`;
CREATE TABLE `db_dormitoryassignments`  (
  `AssignmentID` int(11) NOT NULL COMMENT '分配ID，主键',
  `StudentID` int(11) NULL DEFAULT NULL COMMENT '学生ID，外键',
  `DormitoryID` int(11) NULL DEFAULT NULL COMMENT '宿舍ID，外键',
  `AssignmentDate` date NULL DEFAULT NULL COMMENT '分配日期',
  PRIMARY KEY (`AssignmentID`) USING BTREE,
  INDEX `StudentID`(`StudentID`) USING BTREE,
  INDEX `DormitoryID`(`DormitoryID`) USING BTREE,
  CONSTRAINT `db_dormitoryassignments_ibfk_1` FOREIGN KEY (`StudentID`) REFERENCES `db_students` (`StudentID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `db_dormitoryassignments_ibfk_2` FOREIGN KEY (`DormitoryID`) REFERENCES `db_dormitories` (`DormitoryID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_dormitoryassignments
-- ----------------------------

-- ----------------------------
-- Table structure for db_students
-- ----------------------------
DROP TABLE IF EXISTS `db_students`;
CREATE TABLE `db_students`  (
  `StudentID` int(11) NOT NULL COMMENT '学生ID，主键',
  `Name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `StudentNumber` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学号，唯一',
  `Gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `ContactNumber` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系方式',
  `OccupancyStatus` tinyint(1) NULL DEFAULT NULL COMMENT '入住状态',
  PRIMARY KEY (`StudentID`) USING BTREE,
  UNIQUE INDEX `StudentNumber`(`StudentNumber`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_students
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
