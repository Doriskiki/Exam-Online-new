import request from '@/utils/request'

// 查询考试列表
export function listExam(query) {
  return request({
    url: '/exam/exam/list',
    method: 'get',
    params: query
  })
}

// 查询考试详细
export function getExam(examId) {
  return request({
    url: '/exam/exam/' + examId,
    method: 'get'
  })
}

// 新增考试
export function addExam(data) {
  return request({
    url: '/exam/exam',
    method: 'post',
    data: data
  })
}

// 修改考试
export function updateExam(data) {
  return request({
    url: '/exam/exam',
    method: 'put',
    data: data
  })
}

// 删除考试
export function delExam(examId) {
  return request({
    url: '/exam/exam/' + examId,
    method: 'delete'
  })
}
