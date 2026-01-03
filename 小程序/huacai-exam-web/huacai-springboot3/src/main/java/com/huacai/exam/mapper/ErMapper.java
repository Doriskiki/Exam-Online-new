package com.huacai.exam.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.huacai.exam.domain.Er;

/**
 * 考试结果Mapper接口
 *
 * @author huacai
 * @date 2025-09-30
 */
@Mapper
public interface ErMapper
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
     * 修改考试结果
     *
     * @param er 考试结果
     * @return 结果
     */
    public int updateEr(Er er);

    /**
     * 删除考试结果
     *
     * @param erId 考试结果主键
     * @return 结果
     */
    public int deleteErByErId(String erId);

    /**
     * 批量删除考试结果
     *
     * @param erIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteErByErIds(String[] erIds);

    /**
     * 根据考试ID和用户ID查询考试结果列表
     * @param examId
     * @param userId
     * @return
     */
    List<Er> selectErListByExamIdAndUserId(String examId, Long userId);
}
