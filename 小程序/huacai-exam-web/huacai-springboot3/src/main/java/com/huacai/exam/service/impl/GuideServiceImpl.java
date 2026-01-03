package com.huacai.exam.service.impl;

import java.util.List;

import com.huacai.system.general.utils.DateUtils;
import com.huacai.system.general.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.huacai.exam.mapper.GuideMapper;
import com.huacai.exam.domain.Guide;
import com.huacai.exam.service.IGuideService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

/**
 * 学习指南Service业务层处理
 *
 * @author huacai
 * @date 2025-10-02
 */
@Service
public class GuideServiceImpl implements IGuideService {
    @Autowired
    private GuideMapper guideMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询学习指南
     *
     * @param guideId 学习指南主键
     * @return 学习指南
     */
    @Override
    public Guide selectGuideByGuideId(String guideId) {
        return guideMapper.selectGuideByGuideId(guideId);
    }

    /**
     * 查询学习指南列表
     *
     * @param guide 学习指南
     * @return 学习指南
     */
    @Override
    public List<Guide> selectGuideList(Guide guide) {
        return guideMapper.selectGuideList(guide);
    }

    /**
     * 新增学习指南
     *
     * @param guide 学习指南
     * @return 结果
     */
    @Override
    public int insertGuide(Guide guide) {
        guide.setGuideId(IdUtils.fastSimpleUUID());
        guide.setCreateTime(DateUtils.getNowDate());
        return guideMapper.insertGuide(guide);
    }

    /**
     * 批量新增学习指南
     *
     * @param guides 学习指南List
     * @return 结果
     */
    @Override
    public int batchInsertGuide(List<Guide> guides) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(guides)) {
            try {
                for (int i = 0; i < guides.size(); i++) {
                    int row = guideMapper.insertGuide(guides.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i > 0 && i % 100 == 0) || i == guides.size() - 1;
                    if (bool) {
                        sqlSession.commit();
                        sqlSession.clearCache();
                    }
                    count = i + 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 没有提交的数据可以回滚
                sqlSession.rollback();
            } finally {
                sqlSession.close();
                return count;
            }
        }
        return count;
    }

    /**
     * 修改学习指南
     *
     * @param guide 学习指南
     * @return 结果
     */
    @Override
    public int updateGuide(Guide guide) {
        return guideMapper.updateGuide(guide);
    }

    /**
     * 批量删除学习指南
     *
     * @param guideIds 需要删除的学习指南主键
     * @return 结果
     */
    @Override
    public int deleteGuideByGuideIds(String[] guideIds) {
        return guideMapper.deleteGuideByGuideIds(guideIds);
    }

    /**
     * 删除学习指南信息
     *
     * @param guideId 学习指南主键
     * @return 结果
     */
    @Override
    public int deleteGuideByGuideId(String guideId) {
        return guideMapper.deleteGuideByGuideId(guideId);
    }
}
