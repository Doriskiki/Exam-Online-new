package com.huacai.exam.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.huacai.exam.domain.Questions;

/**
 * 题目Mapper接口
 *
 * @author huacai
 * @date 2025-09-26
 */
@Mapper
public interface QuestionsMapper
{
    /**
     * 查询题目
     *
     * @param questionId 题目主键
     * @return 题目
     */
    public Questions selectQuestionsByQuestionId(String questionId);

    /**
     * 查询题目列表
     *
     * @param questions 题目
     * @return 题目集合
     */
    public List<Questions> selectQuestionsList(Questions questions);

    /**
     * 新增题目
     *
     * @param questions 题目
     * @return 结果
     */
    public int insertQuestions(Questions questions);

    /**
     * 修改题目
     *
     * @param questions 题目
     * @return 结果
     */
    public int updateQuestions(Questions questions);

    /**
     * 删除题目
     *
     * @param questionId 题目主键
     * @return 结果
     */
    public int deleteQuestionsByQuestionId(String questionId);

    /**
     * 批量删除题目
     *
     * @param questionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuestionsByQuestionIds(String[] questionIds);
}
