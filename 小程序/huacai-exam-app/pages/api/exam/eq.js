import request from '@/utils/request'

// 查询试题列表
export function listEq(query) {
  return request({
    url: '/exam/eq/list',
    method: 'get',
    params: query
  })
}

// 查询试题详细
export function getEq(eqId) {
  return request({
    url: '/exam/eq/' + eqId,
    method: 'get'
  })
}

// 新增试题
export function addEq(data) {
  return request({
    url: '/exam/eq',
    method: 'post',
    data: data
  })
}

// 修改试题
export function updateEq(data) {
  return request({
    url: '/exam/eq',
    method: 'put',
    data: data
  })
}

// 删除试题
export function delEq(eqId) {
  return request({
    url: '/exam/eq/' + eqId,
    method: 'delete'
  })
}

//根据考试ID查询对应的试题列表
export function selectExamQuestionsListByExamId(examId) {
  return request({
    url: '/exam/eq/selectExamQuestionsListByExamId/' + examId,
    method: 'get'
  })
}
