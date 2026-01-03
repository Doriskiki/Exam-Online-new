package com.huacai.exam.service.impl;

import java.util.List;

import com.huacai.system.general.utils.DateUtils;
import com.huacai.system.general.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.huacai.exam.mapper.CategoryMapper;
import com.huacai.exam.domain.Category;
import com.huacai.exam.service.ICategoryService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

/**
 * 题目分类Service业务层处理
 *
 * @author huacai
 * @date 2025-09-25
 */
@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询题目分类
     *
     * @param categoryId 题目分类主键
     * @return 题目分类
     */
    @Override
    public Category selectCategoryByCategoryId(String categoryId) {
        return categoryMapper.selectCategoryByCategoryId(categoryId);
    }

    /**
     * 查询题目分类列表
     *
     * @param category 题目分类
     * @return 题目分类
     */
    @Override
    public List<Category> selectCategoryList(Category category) {
        return categoryMapper.selectCategoryList(category);
    }

    /**
     * 新增题目分类
     *
     * @param category 题目分类
     * @return 结果
     */
    @Override
    public int insertCategory(Category category) {
        category.setCreateTime(DateUtils.getNowDate());
        category.setCategoryId(IdUtils.fastSimpleUUID());
        return categoryMapper.insertCategory(category);
    }

    /**
     * 批量新增题目分类
     *
     * @param categorys 题目分类List
     * @return 结果
     */
    @Override
    public int batchInsertCategory(List<Category> categorys) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(categorys)) {
            try {
                for (int i = 0; i < categorys.size(); i++) {
                    for (Category category : categorys) {
                        category.setCategoryId(IdUtils.fastSimpleUUID());
                    }
                    int row = categoryMapper.insertCategory(categorys.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i > 0 && i % 100 == 0) || i == categorys.size() - 1;
                    if (bool) {
                        sqlSession.commit();
                        sqlSession.clearCache();
                    }
                    count = i + 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 没有提交的数据可以回滚
                sqlSession.rollback();
            } finally {
                sqlSession.close();
                return count;
            }
        }
        return count;
    }

    /**
     * 修改题目分类
     *
     * @param category 题目分类
     * @return 结果
     */
    @Override
    public int updateCategory(Category category) {
        return categoryMapper.updateCategory(category);
    }

    /**
     * 批量删除题目分类
     *
     * @param categoryIds 需要删除的题目分类主键
     * @return 结果
     */
    @Override
    public int deleteCategoryByCategoryIds(String[] categoryIds) {
        return categoryMapper.deleteCategoryByCategoryIds(categoryIds);
    }

    /**
     * 删除题目分类信息
     *
     * @param categoryId 题目分类主键
     * @return 结果
     */
    @Override
    public int deleteCategoryByCategoryId(String categoryId) {
        return categoryMapper.deleteCategoryByCategoryId(categoryId);
    }

    /**
     * 获取题目类型分布数据
     * @return
     */
    @Override
    public List<Category> getQuestionTypeStats() {
        return categoryMapper.getQuestionTypeStats();
    }
}
