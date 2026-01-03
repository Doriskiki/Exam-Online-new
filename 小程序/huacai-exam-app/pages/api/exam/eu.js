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

//查询个人的所有考试
export function selectMyExam() {
  return request({
    url: '/exam/eu/selectMyExam',
    method: 'get'
  })
}

// 查询个人已考完的考试列表
export function selectMyExamEndRecord() {
  return request({
    url: '/exam/eu/selectMyExamEndRecord',
    method: 'get'
  })
}

// 查询个人的考试次数和通过次数
export function selectMyExamCountAndPassCount() {
  return request({
    url: '/exam/eu/selectMyExamCountAndPassCount',
    method: 'get'
  })
}
