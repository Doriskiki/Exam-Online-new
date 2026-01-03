package com.huacai.exam.service;

import java.util.List;
import com.huacai.exam.domain.Eq;

/**
 * 试题Service接口
 *
 * @author huacai
 * @date 2025-09-27
 */
public interface IEqService
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
     * 批量新增试题
     *
     * @param eqs 试题List
     * @return 结果
     */
    public int batchInsertEq(List<Eq> eqs);

    /**
     * 修改试题
     *
     * @param eq 试题
     * @return 结果
     */
    public int updateEq(Eq eq);

    /**
     * 批量删除试题
     *
     * @param eqIds 需要删除的试题主键集合
     * @return 结果
     */
    public int deleteEqByEqIds(String[] eqIds);

    /**
     * 删除试题信息
     *
     * @param eqId 试题主键
     * @return 结果
     */
    public int deleteEqByEqId(String eqId);

    /**
     * 根据考试ID删除试题
     * @param examId
     * @return
     */
    int deleteEqByExamId(String examId);

    /**
     * 根据考试ID查询对应的试题列表
     * @param examId
     * @return
     */
    List<Eq> selectExamQuestionsListByExamId(String examId);
}
