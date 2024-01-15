import request from '@/utils/request'

// 查询角色列表
export function listRole(query) {
  return request({
    url: '/sys/role',
    method: 'get',
    params: query
  })
}

// 查询角色详细
export function getRole(id) {
  return request({
    url: '/sys/role/' + id,
    method: 'get'
  })
}

// 新增角色
export function addRole(data) {
  return request({
    url: '/sys/role',
    method: 'post',
    data: data
  })
}

// 修改角色
export function updateRole(data) {
  return request({
    url: '/sys/role',
    method: 'put',
    data: data
  })
}

// 删除角色
export function delRole(id) {
  return request({
    url: '/sys/role/' + id,
    method: 'delete'
  })
}

/**
 * 分配权限 获取权限数据
 * @param params
 * @returns {*}
 */
export function getAssignPermissionTree(params) {
  return request({
    url: '/sys/role/assign',
    method: 'get',
    params
  })
}

/**
 * 分配权限 保存权限数据
 * @param data
 * @returns {AxiosPromise}
 */
export function assignRolePermission(data) {
  return request({
    url: '/sys/role/assign',
    method: 'post',
    data
  })
}

/**
 * 分配角色 保存角色数据
 * @param data
 * @returns {*}
 */
export function assignUserRole(data) {
  return request({
    url: '/sys/role/assign/role',
    method: 'post',
    data
  })
}
