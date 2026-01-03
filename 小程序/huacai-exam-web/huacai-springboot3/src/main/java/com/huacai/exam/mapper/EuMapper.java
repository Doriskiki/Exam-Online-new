package com.huacai.exam.mapper;

import java.util.List;

import com.huacai.exam.domain.vo.ExamStatusVO;
import org.apache.ibatis.annotations.Mapper;
import com.huacai.exam.domain.Eu;

/**
 * 考试用户分配Mapper接口
 *
 * @author huacai
 * @date 2025-09-29
 */
@Mapper
public interface EuMapper
{
    /**
     * 查询考试用户分配
     *
     * @param euId 考试用户分配主键
     * @return 考试用户分配
     */
    public Eu selectEuByEuId(String euId);

    /**
     * 查询考试用户分配列表
     *
     * @param eu 考试用户分配
     * @return 考试用户分配集合
     */
    public List<Eu> selectEuList(Eu eu);

    /**
     * 新增考试用户分配
     *
     * @param eu 考试用户分配
     * @return 结果
     */
    public int insertEu(Eu eu);

    /**
     * 修改考试用户分配
     *
     * @param eu 考试用户分配
     * @return 结果
     */
    public int updateEu(Eu eu);

    /**
     * 删除考试用户分配
     *
     * @param euId 考试用户分配主键
     * @return 结果
     */
    public int deleteEuByEuId(String euId);

    /**
     * 批量删除考试用户分配
     *
     * @param euIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEuByEuIds(String[] euIds);

    /**
     * 根据考试ID和用户ID查询考试用户分配ID
     * @param examId
     * @param userId
     * @return
     */
    String selectEuIdByExamIdAndUserId(String examId, Long userId);

    /**
     * 查询个人已考完的考试列表
     * @param userId
     * @return
     */
    List<Eu> selectMyExamEndRecord(Long userId);

    /**
     * 查询个人的考试次数和通过次数
     * @return
     */
    ExamStatusVO selectMyExamCountAndPassCount(Long userId);
}
