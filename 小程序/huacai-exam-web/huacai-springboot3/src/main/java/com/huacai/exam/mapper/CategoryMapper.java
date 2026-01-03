package com.huacai.exam.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.huacai.exam.domain.Category;

/**
 * 题目分类Mapper接口
 *
 * @author huacai
 * @date 2025-09-25
 */
@Mapper
public interface CategoryMapper
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
     * 修改题目分类
     *
     * @param category 题目分类
     * @return 结果
     */
    public int updateCategory(Category category);

    /**
     * 删除题目分类
     *
     * @param categoryId 题目分类主键
     * @return 结果
     */
    public int deleteCategoryByCategoryId(String categoryId);

    /**
     * 批量删除题目分类
     *
     * @param categoryIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCategoryByCategoryIds(String[] categoryIds);

    /**
     * 获取题目类型分布数据
     * @return
     */
    List<Category> getQuestionTypeStats();
}
