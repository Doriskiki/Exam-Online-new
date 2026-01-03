package com.huacai.exam.service;

import java.util.List;
import com.huacai.exam.domain.Questions;

/**
 * 题目Service接口
 *
 * @author huacai
 * @date 2025-09-26
 */
public interface IQuestionsService
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
     * 批量新增题目
     *
     * @param questionss 题目List
     * @return 结果
     */
    public int batchInsertQuestions(List<Questions> questionss);

    /**
     * 修改题目
     *
     * @param questions 题目
     * @return 结果
     */
    public int updateQuestions(Questions questions);

    /**
     * 批量删除题目
     *
     * @param questionIds 需要删除的题目主键集合
     * @return 结果
     */
    public int deleteQuestionsByQuestionIds(String[] questionIds);

    /**
     * 删除题目信息
     *
     * @param questionId 题目主键
     * @return 结果
     */
    public int deleteQuestionsByQuestionId(String questionId);
}
