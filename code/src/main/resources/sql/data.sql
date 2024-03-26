-- tb_permission 权限表数据

INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(1, '系统管理', 0, '顶级菜单', 'system:manager', '/system', 'system', '', 0, 'el-icon-s-tools', '2023-12-22 22:10:26', '2023-12-22 22:10:28', NULL, 1);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(101, '角色管理', 1, '系统管理', 'system:role', '/system/role', 'role', '/system/role/index', 1, 'el-icon-s-custom', '2023-12-22 16:00:00', '2023-12-24 08:46:43', NULL, 2);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(102, '用户管理', 1, '系统管理', 'system:user', '/system/user', 'user', '/system/user/index', 1, 'el-icon-user', '2023-12-22 22:10:26', '2023-12-22 22:10:26', NULL, 3);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(103, '菜单管理', 1, '系统管理', 'system:menu', '/system/menu', 'menu', '/system/menu/index', 1, 'el-icon-menu', '2023-12-22 22:10:26', '2023-12-22 22:10:26', NULL, 4);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(204, '新增角色', 101, '角色管理', 'system:role:add', NULL, NULL, NULL, 2, NULL, '2023-12-22 22:10:26', '2023-12-22 22:10:26', NULL, 1);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(205, '修改角色', 101, '角色管理', 'system:role:update', NULL, NULL, NULL, 2, NULL, '2023-12-22 22:10:26', '2023-12-22 22:10:26', NULL, 2);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(206, '删除角色', 101, '角色管理', 'system:role:delete', NULL, NULL, NULL, 2, NULL, '2023-12-22 22:10:26', '2023-12-22 22:10:26', NULL, 3);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(207, '新增用户', 102, '用户管理', 'system:user:add', NULL, NULL, NULL, 2, NULL, '2023-12-22 22:10:26', '2023-12-22 22:10:26', NULL, 1);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(208, '修改用户', 102, '用户管理', 'system:user:update', NULL, NULL, NULL, 2, NULL, '2023-12-22 22:10:26', '2023-12-22 22:10:26', NULL, 2);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(209, '删除用户', 102, '用户管理', 'system:user:delete', NULL, NULL, NULL, 2, NULL, '2023-12-22 22:10:26', '2023-12-22 22:10:26', NULL, 3);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(210, '新增菜单', 103, '菜单管理', 'system:menu:add', '', NULL, NULL, 2, NULL, '2023-12-22 22:10:26', '2023-12-22 22:10:26', NULL, 1);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(211, '修改菜单', 103, '菜单管理', 'system:menu:update', NULL, NULL, NULL, 2, NULL, '2023-12-22 22:10:26', '2023-12-22 22:10:26', NULL, 2);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(212, '删除菜单', 103, '菜单管理', 'system:menu:delete', NULL, NULL, NULL, 2, NULL, '2023-12-22 22:10:26', '2023-12-22 22:10:26', NULL, 3);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(251, ' 登录日志', 1, '系统管理', 'system:login:log', '/system/login/log', 'systemLoginLog', '/system/loginLog/index', 1, 'el-icon-edit-outline', '2024-01-04 16:41:13', NULL, NULL, 5);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(252, '新增登录日志', 251, ' 登录日志', 'system:loginLog:add', NULL, NULL, NULL, 2, NULL, '2024-01-04 00:00:00', '2024-01-04 16:48:41', NULL, 2);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(253, '查询登录日志', 251, ' 登录日志', 'system:loginLog:query', NULL, NULL, NULL, 2, NULL, '2024-01-04 00:00:00', '2024-01-04 16:48:44', NULL, 1);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(254, '修改登录日志', 251, ' 登录日志', 'system:loginLog:update', NULL, NULL, NULL, 2, NULL, '2024-01-04 16:48:31', NULL, NULL, 3);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(255, '删除登录日志', 251, ' 登录日志', 'system:loginLog:delete', NULL, NULL, NULL, 2, NULL, '2024-01-04 16:49:15', NULL, NULL, 4);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(256, '登录用户', 1, '系统管理', 'system:loginUser', '/system/login/user', 'loginUser', '/login/loginUser/index', 1, 'el-icon-star-off', '2024-01-04 00:00:00', '2024-01-04 22:58:16', NULL, 6);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(257, '注销登录用户', 256, '登录用户', 'system:loginUser:logout', NULL, NULL, NULL, 2, NULL, '2024-01-04 22:53:12', NULL, NULL, 1);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(258, '查询角色', 101, '角色管理', 'system:role:query', NULL, NULL, NULL, 2, NULL, '2024-01-11 11:17:37', NULL, NULL, 4);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(259, '查询用户', 102, '用户管理', 'system:user:query', NULL, NULL, NULL, 2, NULL, '2024-01-11 11:18:00', NULL, NULL, 4);
INSERT INTO tb_permission (id, label, parent_id, parent_name, code, `path`, name, url, `type`, icon, create_time, update_time, remark, order_num) VALUES(260, '查询菜单', 103, '菜单管理', 'system:menu:query', NULL, NULL, NULL, 2, NULL, '2024-01-11 00:00:00', '2024-01-11 11:18:33', NULL, 4);

-- tb_user
INSERT INTO tb_user (id, username, password, phone, email, avatar, real_name, gender, create_time, update_time, login_error_num, login_limit_time, last_login_time, is_admin) VALUES(1, 'admin', '$2a$10$eyVkFt110CHnf8xhvDpKvO.95Yfz5Ra5qvvRfr4Tj6S9vbJtx7k/S', '12345678901', 'admin@qq.com', '/avatar/admin.jpg', '管理员', '男', '2024-01-10 05:00:00', '2024-03-26 21:07:26', 1, NULL, '2024-03-26 21:07:26', '1');

-- tb_role
INSERT INTO tb_role (id, role_code, role_name, create_user, create_time, update_time, remark) VALUES(1, 'ROLE_ADMIN', '超级管理员', 1, '2023-12-14 00:00:00', '2024-01-12 13:16:49', '请不要删除超级管理员！');

-- tb_role_permission
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(371, 1, 1);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(372, 1, 101);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(373, 1, 204);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(374, 1, 205);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(375, 1, 206);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(376, 1, 258);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(377, 1, 102);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(378, 1, 207);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(379, 1, 208);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(380, 1, 209);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(381, 1, 259);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(382, 1, 103);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(383, 1, 210);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(384, 1, 211);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(385, 1, 212);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(386, 1, 260);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(387, 1, 251);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(388, 1, 252);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(389, 1, 253);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(390, 1, 254);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(391, 1, 255);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(392, 1, 256);
INSERT INTO tb_role_permission (id, role_id, permission_id) VALUES(393, 1, 257);

-- tb_user_role
INSERT INTO tb_user_role (id, user_id, role_id) VALUES(1, 1, 1);
