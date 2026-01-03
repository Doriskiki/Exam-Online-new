import request from '@/utils/request'

// 查询考试用户分配列表
export function listEu(query) {
  return request({
    url: '/exam/eu/list',
    method: 'get',
    params: query
  })
}

// 查询考试用户分配详细
export function getEu(euId) {
  return request({
    url: '/exam/eu/' + euId,
    method: 'get'
  })
}

// 新增考试用户分配
export function addEu(data) {
  return request({
    url: '/exam/eu',
    method: 'post',
    data: data
  })
}

// 修改考试用户分配
export function updateEu(data) {
  return request({
    url: '/exam/eu',
    method: 'put',
    data: data
  })
}

// 删除考试用户分配
export function delEu(euId) {
  return request({
    url: '/exam/eu/' + euId,
    method: 'delete'
  })
}
