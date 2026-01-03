package com.huacai.exam.service.impl;

import java.util.List;

import com.huacai.system.general.utils.DateUtils;
import com.huacai.system.general.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.huacai.exam.mapper.ErMapper;
import com.huacai.exam.domain.Er;
import com.huacai.exam.service.IErService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

import static com.huacai.system.general.utils.SecurityUtils.getUserId;

/**
 * 考试结果Service业务层处理
 *
 * @author huacai
 * @date 2025-09-30
 */
@Service
public class ErServiceImpl implements IErService {
    @Autowired
    private ErMapper erMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询考试结果
     *
     * @param erId 考试结果主键
     * @return 考试结果
     */
    @Override
    public Er selectErByErId(String erId) {
        return erMapper.selectErByErId(erId);
    }

    /**
     * 查询考试结果列表
     *
     * @param er 考试结果
     * @return 考试结果
     */
    @Override
    public List<Er> selectErList(Er er) {
        return erMapper.selectErList(er);
    }

    /**
     * 新增考试结果
     *
     * @param er 考试结果
     * @return 结果
     */
    @Override
    public int insertEr(Er er) {
        er.setCreateTime(DateUtils.getNowDate());
        er.setErId(IdUtils.fastSimpleUUID());
        er.setUserId(getUserId());
        return erMapper.insertEr(er);
    }

    /**
     * 批量新增考试结果
     *
     * @param ers 考试结果List
     * @return 结果
     */
    @Override
    public int batchInsertEr(List<Er> ers) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(ers)) {
            try {
                for (int i = 0; i < ers.size(); i++) {
                    int row = erMapper.insertEr(ers.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i > 0 && i % 100 == 0) || i == ers.size() - 1;
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
     * 修改考试结果
     *
     * @param er 考试结果
     * @return 结果
     */
    @Override
    public int updateEr(Er er) {
        return erMapper.updateEr(er);
    }

    /**
     * 批量删除考试结果
     *
     * @param erIds 需要删除的考试结果主键
     * @return 结果
     */
    @Override
    public int deleteErByErIds(String[] erIds) {
        return erMapper.deleteErByErIds(erIds);
    }

    /**
     * 删除考试结果信息
     *
     * @param erId 考试结果主键
     * @return 结果
     */
    @Override
    public int deleteErByErId(String erId) {
        return erMapper.deleteErByErId(erId);
    }

    /**
     * 根据考试ID和用户ID查询考试结果列表
     * @param examId
     * @param userId
     * @return
     */
    @Override
    public List<Er> selectErListByExamIdAndUserId(String examId, Long userId) {
        return erMapper.selectErListByExamIdAndUserId(examId, userId);
    }
}
