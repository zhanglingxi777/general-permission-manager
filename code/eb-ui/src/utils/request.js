import axios from 'axios'
import {MessageBox, Message} from 'element-ui'
import store from '@/store'
import {getToken, setToken, clearSessionStorage, getTokenTime, setTokenTime, removeTokenTime} from '@/utils/auth'
import {refreshToken} from "@/api/system/user";

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 15000 // request timeout
})

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
let isRefreshToken = false
// 请求拦截器
service.interceptors.request.use(
  config => {
    // 获取当前时间
    let currentTime = Date.now()
    // 获取token过期时间
    let expireTime = getTokenTime() || Date.now()
    if (config.url !== '/doLogin' && expireTime && expireTime > 0) {
      let min = (expireTime - currentTime) / 1000 / 60
      if (min < 10) {
        console.log('刷新token')
        if (!isRefreshToken) {
          isRefreshToken = true
          refreshToken().then(response => {
            console.log('tokenVO', response)
            if (response.code === 200) {
              setToken(response.data.token)
              setTokenTime(response.data.expireTime)
              config.headers['Authorization'] = 'Bearer ' + getToken()
              return config
            }
          }).catch(() => {
          }).finally(() => {
            isRefreshToken = false
          })
        }
      }
    }
    // do something before request is sent

    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['Authorization'] = 'Bearer ' + getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    clearSessionStorage()
    removeTokenTime()
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
   */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data
    // if the custom code is not 20000, it is judged as an error.
    if (res.code !== 200) {
      Message({
        message: res.msg || 'Error',
        type: 'error',
        duration: 5 * 1000
      })

      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (res.code === 401) { // || res.code === 50012 || res.code === 50014) {
        // to re-login
        MessageBox.confirm('用户登录信息过期，请重新登录！', '系统提示', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            clearSessionStorage()
            removeTokenTime()
            location.reload()
          })
        })
      }
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    clearSessionStorage()
    removeTokenTime()
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
