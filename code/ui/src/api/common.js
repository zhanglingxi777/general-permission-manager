import request from '@/utils/request'

/**
 * 上传文件
 * @param profile 文件路径前缀
 * @param data 文件数据
 * @returns {*}
 */
export function uploadFile(profile, data) {
  return request({
    url: '/common/uploadFile/' + profile,
    method: 'post',
    data
  })
}

/**
 * 上传文件（指定文件名称）
 * @param profile 文件路径前缀
 * @param fileName 文件名称
 * @param data 文件数据
 * @returns {*}
 */
export function uploadFileAppointName(profile, fileName, data) {
  return request({
    url: '/common/uploadFile/' + profile + '/' + fileName,
    method: 'post',
    data
  })
}

/**
 * 获取头像数据
 * @param params
 * @returns {*}
 */
export function getImage(params) {
  return request({
    url: '/common/getImage',
    method: 'get',
    params
  })
}
