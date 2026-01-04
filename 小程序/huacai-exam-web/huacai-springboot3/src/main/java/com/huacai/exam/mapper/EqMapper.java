package com.huacai.exam.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.huacai.exam.domain.Eq;

/**
 * 试题Mapper接口
 *
 * @author huacai
 * @date 2025-09-27
 */
@Mapper
public interface EqMapper
{
    /**
     * 查询试题
     *
     * @param eqId 试题主键
     * @return 试题
     */
    public Eq selectEqByEqId(String eqId);

    /**
     * 查询试题列表
     *
     * @param eq 试题
     * @return 试题集合
     */
    public List<Eq> selectEqList(Eq eq);

    /**
     * 新增试题
     *
     * @param eq 试题
     * @return 结果
     */
    public int insertEq(Eq eq);

    /**
     * 修改试题
     *
     * @param eq 试题
     * @return 结果
     */
    public int updateEq(Eq eq);

    /**
     * 删除试题
     *
     * @param eqId 试题主键
     * @return 结果
     */
    public int deleteEqByEqId(String eqId);

    /**
     * 批量删除试题
     *
     * @param eqIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEqByEqIds(String[] eqIds);

    /**
     * 根据考试ID删除试题
     * @param examId
     * @return
     */
    int deleteEqByExamId(String examId);

    /**
     * 根据考试ID查询该考试的题目总数
     */
    Long selectQuestionCountByExamId(String examId);

    /**
     * 根据考试ID查询对应的试题列表
     * @param examId
     * @return
     */
    List<Eq> selectExamQuestionsListByExamId(String examId);
}
