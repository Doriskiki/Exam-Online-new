import request from '@/utils/request'

// 查询学习指南列表
export function listGuide(query) {
  return request({
    url: '/exam/guide/list',
    method: 'get',
    params: query
  })
}

// 查询学习指南详细
export function getGuide(guideId) {
  return request({
    url: '/exam/guide/' + guideId,
    method: 'get'
  })
}

// 新增学习指南
export function addGuide(data) {
  return request({
    url: '/exam/guide',
    method: 'post',
    data: data
  })
}

// 修改学习指南
export function updateGuide(data) {
  return request({
    url: '/exam/guide',
    method: 'put',
    data: data
  })
}

// 删除学习指南
export function delGuide(guideId) {
  return request({
    url: '/exam/guide/' + guideId,
    method: 'delete'
  })
}

// 查询所有学习指南
export function selectAllGuide() {
  return request({
    url: '/exam/guide/selectAllGuide',
    method: 'get'
  })
}
