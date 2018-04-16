/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50626
Source Host           : localhost:3306
Source Database       : db_user

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2018-04-16 13:24:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_record`
-- ----------------------------
DROP TABLE IF EXISTS `t_record`;
CREATE TABLE `t_record` (
  `recordId` varchar(32) NOT NULL COMMENT '发布记录的id',
  `recordName` varchar(32) NOT NULL,
  `recordNum` varchar(32) NOT NULL COMMENT '物料编码',
  `recordLevel` varchar(8) NOT NULL DEFAULT '五级' COMMENT '一级、二级、三级、四级、五级',
  `recordContent` text,
  `recordImgUrl` varchar(2560) DEFAULT NULL,
  `recordStatus` char(1) NOT NULL DEFAULT '0' COMMENT '0可查看，1禁止查看（管理员可见，即逻辑删除）',
  `recordCreateUserId` varchar(32) NOT NULL COMMENT '创建者id',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`recordId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_record
-- ----------------------------
