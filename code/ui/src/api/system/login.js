import request from '@/utils/request'

/**
 * 获取验证码
 */
export function getVcImage() {
  return request({
    url: '/login/vcImage',
    method: 'get'
  });
}

/**
 * 登录
 */
export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

/**
 * 注销
 */
export function logout() {
  return request({
    url: '/logout',
    method: 'get'
  })
}

/**
 * 刷新token
 * @returns {AxiosPromise}
 */
export function refreshToken() {
  return request({
    url: '/login/refreshToken',
    method: 'get'
  })
}

/**
 * 获取登录用户信息
 * @returns {*}
 */
export function listLoginUser() {
  return request({
    url: '/login/user',
    method: 'get'
  })
}

/**
 * 注销用户
 * @param username
 * @returns {*}
 */
export function forceLogout(username) {
  return request({
    url: '/login/force/' + username,
    method: 'get'
  })
}
