import request from "@/utils/request";

/**
 * 查询菜单列表
 * @param params
 * @returns
 */
export function getMenuList(params) {
  return request({
    url: '/sys/permission',
    method: 'get',
    params
  })
}

/**
 * 获取上级菜单列表
 * @returns {*}
 */
export function getMenuParentList() {
  return request({
    url: '/sys/permission/parent',
    method: 'get'
  })
}

/**
 * 添加菜单
 */
export function addMenu(data) {
  return request({
    url: '/sys/permission',
    method: 'post',
    data
  })
}

/**
 * 获取菜单详情
 * @param id
 * @returns menu
 */

export function getMenu(id) {
  return request({
    url: '/sys/permission/' + id,
    method: 'get',
  })
}

/**
 * 编辑菜单
 */
export function updateMenu(data) {
  return request({
    url: '/sys/permission/',
    method: 'put',
    data
  })
}

/**
 * 删除菜单
 */
export function deleteMenu(id) {
  return request({
    url: '/sys/permission/'+id,
    method:'delete',
  })
}

