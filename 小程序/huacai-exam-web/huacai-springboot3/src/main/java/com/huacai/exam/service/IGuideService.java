package com.huacai.exam.service;

import java.util.List;
import com.huacai.exam.domain.Guide;

/**
 * 学习指南Service接口
 *
 * @author huacai
 * @date 2025-10-02
 */
public interface IGuideService
{
    /**
     * 查询学习指南
     *
     * @param guideId 学习指南主键
     * @return 学习指南
     */
    public Guide selectGuideByGuideId(String guideId);

    /**
     * 查询学习指南列表
     *
     * @param guide 学习指南
     * @return 学习指南集合
     */
    public List<Guide> selectGuideList(Guide guide);

    /**
     * 新增学习指南
     *
     * @param guide 学习指南
     * @return 结果
     */
    public int insertGuide(Guide guide);

    /**
     * 批量新增学习指南
     *
     * @param guides 学习指南List
     * @return 结果
     */
    public int batchInsertGuide(List<Guide> guides);

    /**
     * 修改学习指南
     *
     * @param guide 学习指南
     * @return 结果
     */
    public int updateGuide(Guide guide);

    /**
     * 批量删除学习指南
     *
     * @param guideIds 需要删除的学习指南主键集合
     * @return 结果
     */
    public int deleteGuideByGuideIds(String[] guideIds);

    /**
     * 删除学习指南信息
     *
     * @param guideId 学习指南主键
     * @return 结果
     */
    public int deleteGuideByGuideId(String guideId);
}
