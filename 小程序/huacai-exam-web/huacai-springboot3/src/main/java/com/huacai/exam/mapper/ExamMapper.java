package com.huacai.exam.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.huacai.exam.domain.Exam;

/**
 * 考试Mapper接口
 *
 * @author huacai
 * @date 2025-09-27
 */
@Mapper
public interface ExamMapper
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
     * 修改考试
     *
     * @param exam 考试
     * @return 结果
     */
    public int updateExam(Exam exam);

    /**
     * 删除考试
     *
     * @param examId 考试主键
     * @return 结果
     */
    public int deleteExamByExamId(String examId);

    /**
     * 批量删除考试
     *
     * @param examIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExamByExamIds(String[] examIds);
}
