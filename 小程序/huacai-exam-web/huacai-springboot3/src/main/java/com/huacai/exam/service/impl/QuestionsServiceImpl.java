package com.huacai.exam.service.impl;

import java.util.List;

import com.huacai.system.general.utils.DateUtils;
import com.huacai.system.general.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.huacai.exam.mapper.QuestionsMapper;
import com.huacai.exam.domain.Questions;
import com.huacai.exam.service.IQuestionsService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

/**
 * 题目Service业务层处理
 *
 * @author huacai
 * @date 2025-09-26
 */
@Service
public class QuestionsServiceImpl implements IQuestionsService {
    @Autowired
    private QuestionsMapper questionsMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询题目
     *
     * @param questionId 题目主键
     * @return 题目
     */
    @Override
    public Questions selectQuestionsByQuestionId(String questionId) {
        return questionsMapper.selectQuestionsByQuestionId(questionId);
    }

    /**
     * 查询题目列表
     *
     * @param questions 题目
     * @return 题目
     */
    @Override
    public List<Questions> selectQuestionsList(Questions questions) {
        return questionsMapper.selectQuestionsList(questions);
    }

    /**
     * 新增题目
     *
     * @param questions 题目
     * @return 结果
     */
    @Override
    public int insertQuestions(Questions questions) {
        questions.setCreateTime(DateUtils.getNowDate());
        questions.setQuestionId(IdUtils.fastSimpleUUID());
        return questionsMapper.insertQuestions(questions);
    }

    /**
     * 批量新增题目
     *
     * @param questionss 题目List
     * @return 结果
     */
    @Override
    public int batchInsertQuestions(List<Questions> questionss) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(questionss)) {
            try {
                for (int i = 0; i < questionss.size(); i++) {
                    int row = questionsMapper.insertQuestions(questionss.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i > 0 && i % 100 == 0) || i == questionss.size() - 1;
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
     * 修改题目
     *
     * @param questions 题目
     * @return 结果
     */
    @Override
    public int updateQuestions(Questions questions) {
        return questionsMapper.updateQuestions(questions);
    }

    /**
     * 批量删除题目
     *
     * @param questionIds 需要删除的题目主键
     * @return 结果
     */
    @Override
    public int deleteQuestionsByQuestionIds(String[] questionIds) {
        return questionsMapper.deleteQuestionsByQuestionIds(questionIds);
    }

    /**
     * 删除题目信息
     *
     * @param questionId 题目主键
     * @return 结果
     */
    @Override
    public int deleteQuestionsByQuestionId(String questionId) {
        return questionsMapper.deleteQuestionsByQuestionId(questionId);
    }
}
