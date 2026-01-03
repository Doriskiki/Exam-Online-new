import request from '@/utils/request'

// 查询题目分类列表
export function listCategory(query) {
  return request({
    url: '/exam/category/list',
    method: 'get',
    params: query
  })
}

// 查询题目分类详细
export function getCategory(categoryId) {
  return request({
    url: '/exam/category/' + categoryId,
    method: 'get'
  })
}

// 新增题目分类
export function addCategory(data) {
  return request({
    url: '/exam/category',
    method: 'post',
    data: data
  })
}

// 修改题目分类
export function updateCategory(data) {
  return request({
    url: '/exam/category',
    method: 'put',
    data: data
  })
}

// 删除题目分类
export function delCategory(categoryId) {
  return request({
    url: '/exam/category/' + categoryId,
    method: 'delete'
  })
}

//查询所有分类列表数据
export function selectAllCategoryList() {
  return request({
    url: '/exam/category/selectAllCategoryList',
    method: 'get'
  })
}

//获取题目类型分布数据
export function getQuestionTypeStats() {
  return request({
    url: '/exam/category/getQuestionTypeStats',
    method: 'get'
  })
}
