import axios from '@/libs/api.request'

export const getTableData = () => {
  return axios.request({
    url: 'http://47.94.252.83:8082/jeecg-boot-module-system-2.0.1/lib/library/list',
    method: 'post'
  })
}

export const getWordTableData = () => {
  return axios.request({
    url: 'http://47.94.252.83:8082/jeecg-boot-module-system-2.0.1/api/word',
    method: 'post'
  })
}

export const getDragList = () => {
  return axios.request({
    url: 'get_drag_list',
    method: 'get'
  })
}

export const errorReq = () => {
  return axios.request({
    url: 'error_url',
    method: 'post'
  })
}

export const saveErrorLogger = info => {
  return axios.request({
    url: 'save_error_logger',
    data: info,
    method: 'post'
  })
}

export const uploadImg = formData => {
  return axios.request({
    url: 'image/upload',
    data: formData
  })
}

export const getOrgData = () => {
  return axios.request({
    url: 'get_org_data',
    method: 'get'
  })
}

export const getTreeSelectData = () => {
  return axios.request({
    url: 'get_tree_select_data',
    method: 'get'
  })
}
