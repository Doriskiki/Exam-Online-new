package com.huacai.exam.service.impl;

import java.util.List;

import com.huacai.system.general.utils.DateUtils;
import com.huacai.system.general.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.huacai.exam.mapper.EqMapper;
import com.huacai.exam.domain.Eq;
import com.huacai.exam.service.IEqService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

/**
 * 试题Service业务层处理
 *
 * @author huacai
 * @date 2025-09-27
 */
@Service
public class EqServiceImpl implements IEqService {
    @Autowired
    private EqMapper eqMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询试题
     *
     * @param eqId 试题主键
     * @return 试题
     */
    @Override
    public Eq selectEqByEqId(String eqId) {
        return eqMapper.selectEqByEqId(eqId);
    }

    /**
     * 查询试题列表
     *
     * @param eq 试题
     * @return 试题
     */
    @Override
    public List<Eq> selectEqList(Eq eq) {
        return eqMapper.selectEqList(eq);
    }

    /**
     * 新增试题
     *
     * @param eq 试题
     * @return 结果
     */
    @Override
    public int insertEq(Eq eq) {
        eq.setCreateTime(DateUtils.getNowDate());
        eq.setEqId(IdUtils.fastSimpleUUID());
        return eqMapper.insertEq(eq);
    }

    /**
     * 批量新增试题
     *
     * @param eqs 试题List
     * @return 结果
     */
    @Override
    public int batchInsertEq(List<Eq> eqs) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(eqs)) {
            try {
                for (int i = 0; i < eqs.size(); i++) {
                    int row = eqMapper.insertEq(eqs.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i > 0 && i % 100 == 0) || i == eqs.size() - 1;
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
     * 修改试题
     *
     * @param eq 试题
     * @return 结果
     */
    @Override
    public int updateEq(Eq eq) {
        return eqMapper.updateEq(eq);
    }

    /**
     * 批量删除试题
     *
     * @param eqIds 需要删除的试题主键
     * @return 结果
     */
    @Override
    public int deleteEqByEqIds(String[] eqIds) {
        return eqMapper.deleteEqByEqIds(eqIds);
    }

    /**
     * 删除试题信息
     *
     * @param eqId 试题主键
     * @return 结果
     */
    @Override
    public int deleteEqByEqId(String eqId) {
        return eqMapper.deleteEqByEqId(eqId);
    }

    /**
     * 根据考试ID删除试题
     * @param examId
     * @return
     */
    @Override
    public int deleteEqByExamId(String examId) {
        return eqMapper.deleteEqByExamId(examId);
    }

    /**
     * 根据考试ID查询对应的试题列表
     * @param examId
     * @return
     */
    @Override
    public List<Eq> selectExamQuestionsListByExamId(String examId) {
        return eqMapper.selectExamQuestionsListByExamId(examId);
    }
}
