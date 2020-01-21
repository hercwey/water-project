/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50716
Source Host           : 127.0.0.1:3306
Source Database       : wsd_db

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2019-04-26 11:41:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_privilege
-- ----------------------------
DROP TABLE IF EXISTS `sys_privilege`;
CREATE TABLE `sys_privilege` (
  `privilege_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '权限ID，主键，自动生成',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父节点，自关联',
  `name` varchar(64) DEFAULT NULL COMMENT '权限名',
  `type` int(11) DEFAULT NULL COMMENT '类型，0:菜单目录 1:菜单 2:功能',
  `href` varchar(255) DEFAULT NULL COMMENT '路径',
  `module` varchar(32) DEFAULT NULL COMMENT '模块名',
  `seq` int(11) DEFAULT NULL COMMENT '排序的序号',
  `create_time` datetime DEFAULT NULL COMMENT '时间戳',
  `is_https` int(11) DEFAULT NULL COMMENT '是否HTTPS访问，0为否，1为是',
  `description` varchar(255) DEFAULT NULL COMMENT '备注',
  `privilege_code` varchar(32) DEFAULT NULL COMMENT '权限码',
  `sys_code` int(11) DEFAULT NULL COMMENT '1：系统管理,2：配置管理,3：PIVAS管理,4：扫描核对',
  `privilege_level` int(11) DEFAULT NULL COMMENT '菜单深度',
  `icon_img` varchar(255) DEFAULT NULL COMMENT '菜单图标文件名',
  PRIMARY KEY (`privilege_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='权限表';

-- ----------------------------
-- Records of sys_privilege
-- ----------------------------
INSERT INTO `sys_privilege` VALUES ('1', '0', '系统管理', '0', null, '系统管理', '6', '2018-04-10 20:11:03', '0', null, 'PIVAS_MENU_6', '6', '1', '/index/images/li6.png');
INSERT INTO `sys_privilege` VALUES ('2', '1', '用户权限', '1', '', '用户权限', '0', null, null, '用户权限', null, null, null, null);
INSERT INTO `sys_privilege` VALUES ('3', '2', '用户管理', '2', '/user/starter', '用户管理', '1', null, null, '', null, null, null, null);
INSERT INTO `sys_privilege` VALUES ('4', '2', '角色管理', '2', '/role/starter', '角色管理', '2', null, null, '', null, null, null, null);
INSERT INTO `sys_privilege` VALUES ('5', '2', '菜单权限', '2', '/privilege/starter', '菜单权限', '3', null, null, '', null, null, null, null);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID，主键，自动生成',
  `name` varchar(32) NOT NULL COMMENT '角色名',
  `description` varchar(255) DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '系统管理员', '系统管理员', null, '2019-03-08 10:51:23');

-- ----------------------------
-- Table structure for sys_role_ref_privilege
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_ref_privilege`;
CREATE TABLE `sys_role_ref_privilege` (
  `role_privilege_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色权限ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `privilege_id` bigint(20) NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`role_privilege_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色权限关系表';

-- ----------------------------
-- Records of sys_role_ref_privilege
-- ----------------------------
INSERT INTO `sys_role_ref_privilege` VALUES ('1', '1', '1');
INSERT INTO `sys_role_ref_privilege` VALUES ('2', '1', '2');
INSERT INTO `sys_role_ref_privilege` VALUES ('3', '1', '3');
INSERT INTO `sys_role_ref_privilege` VALUES ('4', '1', '4');
INSERT INTO `sys_role_ref_privilege` VALUES ('5', '1', '5');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键，自动生成',
  `account` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `name` varchar(32) NOT NULL COMMENT '用户名称',
  `gender` bigint(20) DEFAULT NULL COMMENT '性别',
  `telephone` varchar(32) DEFAULT NULL COMMENT '电话',
  `email` varchar(128) DEFAULT NULL COMMENT '电子邮件',
  `resetpwd` bigint(20) DEFAULT NULL COMMENT '强制重置密码',
  `description` varchar(255) DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) DEFAULT NULL COMMENT '创建者标识',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `account_type` int(11) DEFAULT NULL COMMENT '账号类型1：普通',
  `del_flag` int(11) NOT NULL COMMENT '删除标志(0为未删除，1为已删除，默认为0)',
  `is_active` int(11) DEFAULT NULL,
  `user_type` int(11) DEFAULT NULL COMMENT '用户类型(0未设置，1药师，6护士)',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '21232F297A57A5A743894A0E4A801FC3', 'admin', '1', '18888888888', 'admin@zchl.com', '1', '默认管理员', '1', '2018-04-10 20:11:02', '1', '0', '1', '0');

-- ----------------------------
-- Table structure for sys_user_ref_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_ref_role`;
CREATE TABLE `sys_user_ref_role` (
  `user_role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户角色ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色关系表';

-- ----------------------------
-- Records of sys_user_ref_role
-- ----------------------------
INSERT INTO `sys_user_ref_role` VALUES ('1', '1', '1');
