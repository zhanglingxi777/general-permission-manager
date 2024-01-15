import request from '@/utils/request'

// 查询登录日志列表
export function listLoginLog(query) {
  return request({
    url: '/system/loginLog/list',
    method: 'get',
    params: query
  })
}

// 查询登录日志详细
export function getLoginLog(id) {
  return request({
    url: '/system/loginLog/' + id,
    method: 'get'
  })
}

// 新增登录日志
export function addLoginLog(data) {
  return request({
    url: '/system/loginLog',
    method: 'post',
    data: data
  })
}

// 修改登录日志
export function updateLoginLog(data) {
  return request({
    url: '/system/loginLog',
    method: 'put',
    data: data
  })
}

// 删除登录日志
export function delLoginLog(id) {
  return request({
    url: '/system/loginLog/' + id,
    method: 'delete'
  })
}
