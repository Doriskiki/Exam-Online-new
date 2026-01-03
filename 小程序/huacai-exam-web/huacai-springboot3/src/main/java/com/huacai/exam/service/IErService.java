package com.huacai.exam.service;

import java.util.List;
import com.huacai.exam.domain.Er;

/**
 * 考试结果Service接口
 *
 * @author huacai
 * @date 2025-09-30
 */
public interface IErService
{
    /**
     * 查询考试结果
     *
     * @param erId 考试结果主键
     * @return 考试结果
     */
    public Er selectErByErId(String erId);

    /**
     * 查询考试结果列表
     *
     * @param er 考试结果
     * @return 考试结果集合
     */
    public List<Er> selectErList(Er er);

    /**
     * 新增考试结果
     *
     * @param er 考试结果
     * @return 结果
     */
    public int insertEr(Er er);

    /**
     * 批量新增考试结果
     *
     * @param ers 考试结果List
     * @return 结果
     */
    public int batchInsertEr(List<Er> ers);

    /**
     * 修改考试结果
     *
     * @param er 考试结果
     * @return 结果
     */
    public int updateEr(Er er);

    /**
     * 批量删除考试结果
     *
     * @param erIds 需要删除的考试结果主键集合
     * @return 结果
     */
    public int deleteErByErIds(String[] erIds);

    /**
     * 删除考试结果信息
     *
     * @param erId 考试结果主键
     * @return 结果
     */
    public int deleteErByErId(String erId);

    /**
     * 根据考试ID和用户ID查询考试结果列表
     * @param examId
     * @param userId
     * @return
     */
    List<Er> selectErListByExamIdAndUserId(String examId, Long userId);
}
