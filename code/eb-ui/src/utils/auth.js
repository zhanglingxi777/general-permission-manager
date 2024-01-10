import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'
const TimeKey = 'expireTime'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

/**
 * 清空sessionStorage
 */
export function clearSessionStorage() {
  return sessionStorage.clear()
}

/**
 * 设置token的过期时间
 * @param time
 */
export function setTokenTime(time) {
  return sessionStorage.setItem(TimeKey, time)
}

/**
 * 获取token的过期时间
 * @returns {string}
 */
export function getTokenTime() {
  return sessionStorage.getItem(TimeKey)
}

/**
 * 清空token的过期时间
 */
export function removeTokenTime() {
  return sessionStorage.setItem(TimeKey, 0)
}
