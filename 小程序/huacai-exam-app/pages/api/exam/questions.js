import request from '@/utils/request'

// 查询题目列表
export function listQuestions(query) {
  return request({
    url: '/exam/questions/list',
    method: 'get',
    params: query
  })
}

// 查询题目详细
export function getQuestions(questionId) {
  return request({
    url: '/exam/questions/' + questionId,
    method: 'get'
  })
}

// 新增题目
export function addQuestions(data) {
  return request({
    url: '/exam/questions',
    method: 'post',
    data: data
  })
}

// 修改题目
export function updateQuestions(data) {
  return request({
    url: '/exam/questions',
    method: 'put',
    data: data
  })
}

// 删除题目
export function delQuestions(questionId) {
  return request({
    url: '/exam/questions/' + questionId,
    method: 'delete'
  })
}

//查询对应分类的所有题目
export function selectAllListByCategoryId(categoryId) {
  return request({
    url: '/exam/questions/selectAllListByCategoryId/' + categoryId,
    method: 'get'
  })
}
