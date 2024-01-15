import request from '@/utils/request'

/**
 * 查询部门列表
 * @param params
 * @returns {*}
 */
export function getDepartmentList(params) {
  return request({
    url: '/sys/department',
    method: 'get',
    params: params
  })
}

/**
 * 查询上级部门列表
 * @returns {AxiosPromise}
 */
export function getDepartmentParentList() {
  return request({
    url: '/sys/department/parent',
    method: 'get'
  })
}

/**
 * 新增部门
 * @param data
 * @returns {AxiosPromise}
 */
export function addDepartment(data) {
  return request({
    url: '/sys/department',
    method: 'post',
    data: data
  })
}

/**
 * 删除部门
 * @param id
 * @returns {AxiosPromise}
 */
export function deleteDepartment(id) {
  return request({
    url: '/sys/department/' + id,
    method: 'delete'
  })
}

/**
 * 获取部门详情
 * @param id
 * @returns {AxiosPromise}
 */
export function getDepartment(id) {
  return request({
    url: '/sys/department/' + id,
    method: 'get'
  })
}


export function  updateDepartment(data) {
  return request({
    url: '/sys/department',
    method: 'put',
    data
  })
}
