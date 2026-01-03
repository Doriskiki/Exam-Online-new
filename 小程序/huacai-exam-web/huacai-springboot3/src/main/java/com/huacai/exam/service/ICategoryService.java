package com.huacai.exam.service;

import java.util.List;
import com.huacai.exam.domain.Category;

/**
 * 题目分类Service接口
 *
 * @author huacai
 * @date 2025-09-25
 */
public interface ICategoryService
{
    /**
     * 查询题目分类
     *
     * @param categoryId 题目分类主键
     * @return 题目分类
     */
    public Category selectCategoryByCategoryId(String categoryId);

    /**
     * 查询题目分类列表
     *
     * @param category 题目分类
     * @return 题目分类集合
     */
    public List<Category> selectCategoryList(Category category);

    /**
     * 新增题目分类
     *
     * @param category 题目分类
     * @return 结果
     */
    public int insertCategory(Category category);

    /**
     * 批量新增题目分类
     *
     * @param categorys 题目分类List
     * @return 结果
     */
    public int batchInsertCategory(List<Category> categorys);

    /**
     * 修改题目分类
     *
     * @param category 题目分类
     * @return 结果
     */
    public int updateCategory(Category category);

    /**
     * 批量删除题目分类
     *
     * @param categoryIds 需要删除的题目分类主键集合
     * @return 结果
     */
    public int deleteCategoryByCategoryIds(String[] categoryIds);

    /**
     * 删除题目分类信息
     *
     * @param categoryId 题目分类主键
     * @return 结果
     */
    public int deleteCategoryByCategoryId(String categoryId);

    /**
     * 获取题目类型分布数据
     * @return
     */
    List<Category> getQuestionTypeStats();
}
