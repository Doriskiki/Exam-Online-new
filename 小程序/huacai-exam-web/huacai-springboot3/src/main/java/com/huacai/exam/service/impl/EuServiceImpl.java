package com.huacai.exam.service.impl;

import java.util.List;

import com.huacai.exam.domain.vo.ExamStatusVO;
import com.huacai.exam.mapper.EqMapper;
import com.huacai.system.general.utils.DateUtils;
import com.huacai.system.general.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.huacai.exam.mapper.EuMapper;
import com.huacai.exam.domain.Eu;
import com.huacai.exam.service.IEuService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

/**
 * 考试用户分配Service业务层处理
 *
 * @author huacai
 * @date 2025-09-29
 */
@Service
public class EuServiceImpl implements IEuService {
    @Autowired
    private EuMapper euMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private EqMapper eqMapper;

    /**
     * 查询考试用户分配
     *
     * @param euId 考试用户分配主键
     * @return 考试用户分配
     */
    @Override
    public Eu selectEuByEuId(String euId) {
        return euMapper.selectEuByEuId(euId);
    }

    /**
     * 查询考试用户分配列表
     *
     * @param eu 考试用户分配
     * @return 考试用户分配
     */
    @Override
    public List<Eu> selectEuList(Eu eu) {
        return euMapper.selectEuList(eu);
    }

    /**
     * 新增考试用户分配
     *
     * @param eu 考试用户分配
     * @return 结果
     */
    @Override
    public int insertEu(Eu eu) {
        eu.setCreateTime(DateUtils.getNowDate());
        eu.setEuId(IdUtils.fastSimpleUUID());
        //根据考试ID查询该考试的题目总数
        Long questionCount = eqMapper.selectQuestionCountByExamId(eu.getExamId());
        eu.setQuestionCount(questionCount);
        return euMapper.insertEu(eu);
    }

    /**
     * 批量新增考试用户分配
     *
     * @param eus 考试用户分配List
     * @return 结果
     */
    @Override
    public int batchInsertEu(List<Eu> eus) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(eus)) {
            try {
                for (int i = 0; i < eus.size(); i++) {
                    int row = euMapper.insertEu(eus.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i > 0 && i % 100 == 0) || i == eus.size() - 1;
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
     * 修改考试用户分配
     *
     * @param eu 考试用户分配
     * @return 结果
     */
    @Override
    public int updateEu(Eu eu) {
        return euMapper.updateEu(eu);
    }

    /**
     * 批量删除考试用户分配
     *
     * @param euIds 需要删除的考试用户分配主键
     * @return 结果
     */
    @Override
    public int deleteEuByEuIds(String[] euIds) {
        return euMapper.deleteEuByEuIds(euIds);
    }

    /**
     * 删除考试用户分配信息
     *
     * @param euId 考试用户分配主键
     * @return 结果
     */
    @Override
    public int deleteEuByEuId(String euId) {
        return euMapper.deleteEuByEuId(euId);
    }

    /**
     * 根据考试ID和用户ID查询考试用户分配ID
     * @param examId 考试ID
     * @param userId 用户ID
     * @return 考试用户分配ID
     */
    @Override
    public String selectEuIdByExamIdAndUserId(String examId, Long userId) {
        return euMapper.selectEuIdByExamIdAndUserId(examId, userId);
    }

    /**
     * 查询个人已考完的考试列表
     * @param userId
     * @return
     */
    @Override
    public List<Eu> selectMyExamEndRecord(Long userId) {
        return euMapper.selectMyExamEndRecord(userId);
    }

    /**
     * 查询个人的考试次数和通过次数
     * @return
     */
    @Override
    public ExamStatusVO selectMyExamCountAndPassCount(Long userId) {
        return euMapper.selectMyExamCountAndPassCount(userId);
    }
}
