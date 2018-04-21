/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50626
Source Host           : localhost:3306
Source Database       : db_user

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2018-04-16 13:24:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `roleId` varchar(32) NOT NULL,
  `roleName` varchar(64) NOT NULL,
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '超管', '2018-02-24 13:53:15', '2018-02-24 13:53:15');
INSERT INTO `t_role` VALUES ('2', '管理', '2018-02-24 13:53:25', '2018-02-24 13:53:25');
INSERT INTO `t_role` VALUES ('3', '普通', '2018-02-24 13:53:33', '2018-02-24 13:53:33');
INSERT INTO `t_role` VALUES ('4', '游客', '2018-02-24 13:53:45', '2018-02-24 13:53:45');
