import { asyncRoutes, constantRoutes } from '@/router'
import {getMenuList} from "@/api/system/user"
import Layout from '@/layout'

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []
  routes.forEach(route => {
    const tmp = { ...route }
    // 判断是否拥有相应的权限
    if (hasPermission(roles, tmp)) {
      let component = tmp.component;
      // 判断是否有对应的组件
      if (route.component) {
        // 判断是否有根组件
        if (component === 'Layout') {
          tmp.component = Layout
        } else {
          tmp.component = (resolve) => require([`@/views${component}`], resolve)
        }
      }
      // 判断是否有子菜单
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  generateRoutes({ commit }, roles) {
    return new Promise((resolve, reject) => {
      // 获取菜单数据
      getMenuList().then(response => {
        let accessedRoutes
        if (response.code === 200) {
          accessedRoutes = filterAsyncRoutes(response.data, roles)
        }
        commit('SET_ROUTES', accessedRoutes)
        resolve(accessedRoutes)
      }).catch(error => {
        reject(error)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
