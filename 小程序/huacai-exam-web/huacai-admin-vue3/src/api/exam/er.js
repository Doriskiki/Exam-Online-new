import request from '@/utils/request'

// 查询考试结果列表
export function listEr(query) {
  return request({
    url: '/exam/er/list',
    method: 'get',
    params: query
  })
}

// 查询考试结果详细
export function getEr(erId) {
  return request({
    url: '/exam/er/' + erId,
    method: 'get'
  })
}

// 新增考试结果
export function addEr(data) {
  return request({
    url: '/exam/er',
    method: 'post',
    data: data
  })
}

// 修改考试结果
export function updateEr(data) {
  return request({
    url: '/exam/er',
    method: 'put',
    data: data
  })
}

// 删除考试结果
export function delEr(erId) {
  return request({
    url: '/exam/er/' + erId,
    method: 'delete'
  })
}
