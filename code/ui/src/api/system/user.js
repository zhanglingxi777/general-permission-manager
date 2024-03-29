import request from '@/utils/request'

/**
 * 获取登录用户信息
 */
export function getInfo() {
  return request({
    url: '/sys/user/getInfo',
    method: 'get'
  });
}

/**
 * 获取菜单数据
 */
export function getMenuList() {
  return request({
    url: '/sys/user/getMenuList',
    method: 'get'
  })
}

/**
 * 获取分页用户信息
 * @param params
 * @returns {*}
 */
export function listUser(params) {
  return request({
    url: '/sys/user',
    method: 'get',
    params
  })
}

/**
 * 获取用户详细信息
 * @param id
 * @returns {*}
 */
export function getUserInfo(id) {
  return request({
    url: '/sys/user/' + id,
    method: 'get'
  })
}

/**
 * 获取用户下拉框列表
 * @returns {*}
 */
export function getUserSelectVO() {
  return request({
    url: '/sys/user/list/select',
    method: 'get'
  })
}

/**
 * 新增用户信息
 * @param data
 * @returns {*}
 */
export function addUser(data) {
  return request({
    url: '/sys/user',
    method: 'post',
    data
  })
}

/**
 * 更新用户喜信息
 * @param data
 * @returns {*}
 */
export function updateUser(data) {
  return request({
    url: '/sys/user',
    method: 'put',
    data
  })
}

/**
 * 删除用户信息
 * @param id
 * @returns {*}
 */
export function delUser(id) {
  return request({
    url: '/sys/user/' + id,
    method: 'delete'
  })
}

/**
 * 查询用户可分配的角色列表
 * @param params
 * @returns {*}
 */
export function getAssignRoleList(params) {
  return request({
    url: '/sys/role',
    method: 'get',
    params
  })
}

/**
 * 查询用户拥有的角色列表
 * @param params
 * @returns {*}
 */
export function getRoleIdByUserId(params) {
  return request({
    url: '/sys/role/assign/hasRole',
    method: 'get',
    params
  })
}

/**
 * 修改密码
 * @param data
 * @returns {*}
 */
export function resetPwd(data) {
  return request({
    url: '/sys/user/pwd',
    method: 'put',
    data
  })
}
