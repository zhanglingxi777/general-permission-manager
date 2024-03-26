-- `lingxi-basic`.persistent_logins definition

CREATE TABLE `persistent_logins` (
                                     `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
                                     `series` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '序列化号',
                                     `token` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '凭证',
                                     `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近使用时间',
                                     PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='记住我持久化表';


-- `lingxi-basic`.tb_login_log definition

CREATE TABLE `tb_login_log` (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Id',
                                `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
                                `login_time` datetime NOT NULL COMMENT '登录时间',
                                `request_data` json DEFAULT NULL COMMENT '请求数据',
                                `login_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录状态',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='登录日志表';


-- `lingxi-basic`.tb_permission definition

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


-- `lingxi-basic`.tb_role definition

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


-- `lingxi-basic`.tb_role_permission definition

CREATE TABLE `tb_role_permission` (
                                      `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                      `role_id` bigint NOT NULL COMMENT '角色ID',
                                      `permission_id` bigint NOT NULL COMMENT '权限ID',
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=394 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色菜单表';


-- `lingxi-basic`.tb_user definition

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


-- `lingxi-basic`.tb_user_role definition

CREATE TABLE `tb_user_role` (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                `user_id` bigint NOT NULL COMMENT '用户ID',
                                `role_id` bigint NOT NULL COMMENT '角色ID',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色用户表';