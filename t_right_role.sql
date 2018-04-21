/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50626
Source Host           : localhost:3306
Source Database       : db_user

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2018-04-16 13:24:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_right_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_right_role`;
CREATE TABLE `t_right_role` (
  `roleId` varchar(32) NOT NULL,
  `rightId` varchar(32) NOT NULL,
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`roleId`,`rightId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_right_role
-- ----------------------------
