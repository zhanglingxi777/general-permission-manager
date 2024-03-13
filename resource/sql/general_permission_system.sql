-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: rm-bp1j8u5tpt83g853v6o.rwlb.rds.aliyuncs.com    Database: general_permission_system
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
--
-- Table structure for table `persistent_logins`
--

DROP TABLE IF EXISTS `persistent_logins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `persistent_logins` (
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `series` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '序列化号',
  `token` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '凭证',
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近使用时间',
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='记住我持久化表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_login_log`
--

DROP TABLE IF EXISTS `tb_login_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_login_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  `request_data` json DEFAULT NULL COMMENT '请求数据',
  `login_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='登录日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_permission`
--

DROP TABLE IF EXISTS `tb_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '权限名称',
  `parent_id` bigint DEFAULT NULL COMMENT '父权限ID',
  `parent_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '父权限名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '授权标识符',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '路由地址',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '路由名称',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '授权路径',
  `type` tinyint DEFAULT NULL COMMENT '权限类型（0目录1菜单2按钮）',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图标',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `order_num` int DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=261 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_permission`
--

LOCK TABLES `tb_permission` WRITE;
/*!40000 ALTER TABLE `tb_permission` DISABLE KEYS */;
INSERT INTO `tb_permission` VALUES (1,'系统管理',0,'顶级菜单','system:manager','/system','system','',0,'el-icon-s-tools','2023-12-22 22:10:26','2023-12-22 22:10:28',NULL,1),(101,'角色管理',1,'系统管理','system:role','/system/role','role','/system/role/index',1,'el-icon-s-custom','2023-12-22 16:00:00','2023-12-24 08:46:43',NULL,2),(102,'用户管理',1,'系统管理','system:user','/system/user','user','/system/user/index',1,'el-icon-user','2023-12-22 22:10:26','2023-12-22 22:10:26',NULL,3),(103,'菜单管理',1,'系统管理','system:menu','/system/menu','menu','/system/menu/index',1,'el-icon-menu','2023-12-22 22:10:26','2023-12-22 22:10:26',NULL,4),(204,'新增角色',101,'角色管理','system:role:add',NULL,NULL,NULL,2,NULL,'2023-12-22 22:10:26','2023-12-22 22:10:26',NULL,1),(205,'修改角色',101,'角色管理','system:role:update',NULL,NULL,NULL,2,NULL,'2023-12-22 22:10:26','2023-12-22 22:10:26',NULL,2),(206,'删除角色',101,'角色管理','system:role:delete',NULL,NULL,NULL,2,NULL,'2023-12-22 22:10:26','2023-12-22 22:10:26',NULL,3),(207,'新增用户',102,'用户管理','system:user:add',NULL,NULL,NULL,2,NULL,'2023-12-22 22:10:26','2023-12-22 22:10:26',NULL,1),(208,'修改用户',102,'用户管理','system:user:update',NULL,NULL,NULL,2,NULL,'2023-12-22 22:10:26','2023-12-22 22:10:26',NULL,2),(209,'删除用户',102,'用户管理','system:user:delete',NULL,NULL,NULL,2,NULL,'2023-12-22 22:10:26','2023-12-22 22:10:26',NULL,3),(210,'新增菜单',103,'菜单管理','system:menu:add','',NULL,NULL,2,NULL,'2023-12-22 22:10:26','2023-12-22 22:10:26',NULL,1),(211,'修改菜单',103,'菜单管理','system:menu:update',NULL,NULL,NULL,2,NULL,'2023-12-22 22:10:26','2023-12-22 22:10:26',NULL,2),(212,'删除菜单',103,'菜单管理','system:menu:delete',NULL,NULL,NULL,2,NULL,'2023-12-22 22:10:26','2023-12-22 22:10:26',NULL,3),(251,' 登录日志',1,'系统管理','system:login:log','/system/login/log','systemLoginLog','/system/loginLog/index',1,'el-icon-edit-outline','2024-01-04 16:41:13',NULL,NULL,5),(252,'新增登录日志',251,' 登录日志','system:loginLog:add',NULL,NULL,NULL,2,NULL,'2024-01-04 00:00:00','2024-01-04 16:48:41',NULL,2),(253,'查询登录日志',251,' 登录日志','system:loginLog:query',NULL,NULL,NULL,2,NULL,'2024-01-04 00:00:00','2024-01-04 16:48:44',NULL,1),(254,'修改登录日志',251,' 登录日志','system:loginLog:update',NULL,NULL,NULL,2,NULL,'2024-01-04 16:48:31',NULL,NULL,3),(255,'删除登录日志',251,' 登录日志','system:loginLog:delete',NULL,NULL,NULL,2,NULL,'2024-01-04 16:49:15',NULL,NULL,4),(256,'登录用户',1,'系统管理','system:loginUser','/system/login/user','loginUser','/login/loginUser/index',1,'el-icon-star-off','2024-01-04 00:00:00','2024-01-04 22:58:16',NULL,6),(257,'注销登录用户',256,'登录用户','system:loginUser:logout',NULL,NULL,NULL,2,NULL,'2024-01-04 22:53:12',NULL,NULL,1),(258,'查询角色',101,'角色管理','system:role:query',NULL,NULL,NULL,2,NULL,'2024-01-11 11:17:37',NULL,NULL,4),(259,'查询用户',102,'用户管理','system:user:query',NULL,NULL,NULL,2,NULL,'2024-01-11 11:18:00',NULL,NULL,4),(260,'查询菜单',103,'菜单管理','system:menu:query',NULL,NULL,NULL,2,NULL,'2024-01-11 00:00:00','2024-01-11 11:18:33',NULL,4);
/*!40000 ALTER TABLE `tb_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_role`
--

DROP TABLE IF EXISTS `tb_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '编码',
  `role_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '名称',
  `create_user` bigint DEFAULT '0' COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role`
--

LOCK TABLES `tb_role` WRITE;
/*!40000 ALTER TABLE `tb_role` DISABLE KEYS */;
INSERT INTO `tb_role` VALUES (1,'ROLE_ADMIN','超级管理员',1,'2023-12-14 00:00:00','2024-01-12 13:16:49','请不要删除超级管理员！');
/*!40000 ALTER TABLE `tb_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_role_permission`
--

DROP TABLE IF EXISTS `tb_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_role_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `permission_id` bigint NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=371 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role_permission`
--

LOCK TABLES `tb_role_permission` WRITE;
/*!40000 ALTER TABLE `tb_role_permission` DISABLE KEYS */;
INSERT INTO `tb_role_permission` VALUES (350,1,1),(351,1,102),(352,1,103),(353,1,101),(354,1,204),(355,1,205),(356,1,206),(357,1,258),(358,1,207),(359,1,208),(360,1,209),(361,1,210),(362,1,211),(363,1,212),(364,1,251),(365,1,252),(366,1,253),(367,1,254),(368,1,255),(369,1,256),(370,1,257);
/*!40000 ALTER TABLE `tb_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user`
--

DROP TABLE IF EXISTS `tb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户Id',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像（路径）',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '真实姓名',
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '性别',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `login_error_num` int DEFAULT NULL COMMENT '登录错误次数',
  `login_limit_time` datetime DEFAULT NULL COMMENT '登录限制时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后一次登录时间',
  `is_admin` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否是管理员',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user`
--

LOCK TABLES `tb_user` WRITE;
/*!40000 ALTER TABLE `tb_user` DISABLE KEYS */;
INSERT INTO `tb_user` VALUES (1,'admin','vN9dRiKTQ9N+lPFrjZ4InQ==','12345678901','admin@qq.com','/avatar/admin.jpg','管理员','男','2024-01-10 05:00:00','2024-01-13 00:07:21',0,'2024-01-13 14:09:06','2024-01-14 19:56:43','1');
/*!40000 ALTER TABLE `tb_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_role`
--

DROP TABLE IF EXISTS `tb_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_role`
--

LOCK TABLES `tb_user_role` WRITE;
/*!40000 ALTER TABLE `tb_user_role` DISABLE KEYS */;
INSERT INTO `tb_user_role` VALUES (1,1,1);
/*!40000 ALTER TABLE `tb_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'general_permission_system'
--
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-15 22:30:53
