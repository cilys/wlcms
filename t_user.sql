/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50626
Source Host           : localhost:3306
Source Database       : db_user

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2018-02-07 14:44:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `userId` varchar(32) NOT NULL COMMENT '用户Id',
  `userName` varchar(64) NOT NULL COMMENT '用户名，区别真是姓名',
  `pwd` varchar(32) NOT NULL COMMENT '密码，32位MD5加密值',
  `realName` varchar(256) DEFAULT NULL COMMENT '真实姓名',
  `sex` char(1) NOT NULL DEFAULT '0' COMMENT '性别：0未知，1男，2女',
  `phone` varchar(16) DEFAULT NULL COMMENT '手机号码',
  `address` varchar(512) DEFAULT NULL COMMENT '住址',
  `idCard` varchar(32) DEFAULT NULL COMMENT '身份证号',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '用户状态，0正常可用',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`userId`),
  UNIQUE KEY `uniqUser` (`userName`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('04c7493b4a764abeb0ed7b10f3fb4d34', 'Test07', '123456', '测试07', '1', null, null, null, '0', '2018-02-06 13:46:29');
INSERT INTO `t_user` VALUES ('670b14728ad9902aecba32e22fa4f6bd', 'admin', '123456', null, '0', null, null, null, '0', '2018-02-01 16:13:08');
INSERT INTO `t_user` VALUES ('68c6b66fd8f548dcb69954b2cfbb147b', 'Test02', '123456', '测试02', '0', '13000000002', null, null, '1', '2018-02-06 13:00:07');
INSERT INTO `t_user` VALUES ('9752cc535a30418cab596cb7cc43f4b1', 'Test06', '123456', '测试06', '1', null, null, null, '0', '2018-02-06 13:39:29');
INSERT INTO `t_user` VALUES ('b031c5fe55b942a09f4a6933d379ab9c', 'Test01', '123456', '测试01', '0', null, null, null, '1', '2018-02-06 12:59:30');
INSERT INTO `t_user` VALUES ('da907da68b6448b6994a13e040db7453', 'Test04', '123456', null, '1', null, null, null, '0', '2018-02-06 13:18:45');
INSERT INTO `t_user` VALUES ('db788c26cc484e24a64b8c6893bc9e66', 'Test03', '123456', '测试03', '1', null, null, null, '1', '2018-02-06 13:12:50');
INSERT INTO `t_user` VALUES ('fb2bf2f9394a452184acb3d835dd5241', 'Test08', '123456', '测试08', '1', '13000000008', null, null, '0', '2018-02-06 13:59:56');
