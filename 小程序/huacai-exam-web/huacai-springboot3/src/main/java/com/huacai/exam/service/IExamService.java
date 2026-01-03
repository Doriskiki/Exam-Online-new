package com.huacai.exam.service;

import java.util.List;
import com.huacai.exam.domain.Exam;

/**
 * 考试Service接口
 *
 * @author huacai
 * @date 2025-09-27
 */
public interface IExamService
{
    /**
     * 查询考试
     *
     * @param examId 考试主键
     * @return 考试
     */
    public Exam selectExamByExamId(String examId);

    /**
     * 查询考试列表
     *
     * @param exam 考试
     * @return 考试集合
     */
    public List<Exam> selectExamList(Exam exam);

    /**
     * 新增考试
     *
     * @param exam 考试
     * @return 结果
     */
    public int insertExam(Exam exam);

    /**
     * 批量新增考试
     *
     * @param exams 考试List
     * @return 结果
     */
    public int batchInsertExam(List<Exam> exams);

    /**
     * 修改考试
     *
     * @param exam 考试
     * @return 结果
     */
    public int updateExam(Exam exam);

    /**
     * 批量删除考试
     *
     * @param examIds 需要删除的考试主键集合
     * @return 结果
     */
    public int deleteExamByExamIds(String[] examIds);

    /**
     * 删除考试信息
     *
     * @param examId 考试主键
     * @return 结果
     */
    public int deleteExamByExamId(String examId);

    /**
     * 自动组卷
     * @param examId
     * @return
     */
    int autoCompose(String examId);
}
