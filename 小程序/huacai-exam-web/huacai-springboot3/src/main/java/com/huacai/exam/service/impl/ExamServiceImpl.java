package com.huacai.exam.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.huacai.exam.domain.Eq;
import com.huacai.exam.domain.Questions;
import com.huacai.exam.service.IEqService;
import com.huacai.exam.service.IQuestionsService;
import com.huacai.system.general.utils.DateUtils;
import com.huacai.system.general.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.huacai.exam.mapper.ExamMapper;
import com.huacai.exam.domain.Exam;
import com.huacai.exam.service.IExamService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

/**
 * 考试Service业务层处理
 *
 * @author huacai
 * @date 2025-09-27
 */
@Service
public class ExamServiceImpl implements IExamService {
    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private IEqService eqService;

    @Autowired
    private IQuestionsService questionsService;

    /**
     * 查询考试
     *
     * @param examId 考试主键
     * @return 考试
     */
    @Override
    public Exam selectExamByExamId(String examId) {
        return examMapper.selectExamByExamId(examId);
    }

    /**
     * 查询考试列表
     *
     * @param exam 考试
     * @return 考试
     */
    @Override
    public List<Exam> selectExamList(Exam exam) {
        return examMapper.selectExamList(exam);
    }

    /**
     * 新增考试
     *
     * @param exam 考试
     * @return 结果
     */
    @Override
    public int insertExam(Exam exam) {
        exam.setCreateTime(DateUtils.getNowDate());
        exam.setExamId(IdUtils.fastSimpleUUID());
        return examMapper.insertExam(exam);
    }

    /**
     * 批量新增考试
     *
     * @param exams 考试List
     * @return 结果
     */
    @Override
    public int batchInsertExam(List<Exam> exams) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(exams)) {
            try {
                for (int i = 0; i < exams.size(); i++) {
                    int row = examMapper.insertExam(exams.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i > 0 && i % 100 == 0) || i == exams.size() - 1;
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
     * 修改考试
     *
     * @param exam 考试
     * @return 结果
     */
    @Override
    public int updateExam(Exam exam) {
        return examMapper.updateExam(exam);
    }

    /**
     * 批量删除考试
     *
     * @param examIds 需要删除的考试主键
     * @return 结果
     */
    @Override
    public int deleteExamByExamIds(String[] examIds) {
        return examMapper.deleteExamByExamIds(examIds);
    }

    /**
     * 删除考试信息
     *
     * @param examId 考试主键
     * @return 结果
     */
    @Override
    public int deleteExamByExamId(String examId) {
        return examMapper.deleteExamByExamId(examId);
    }

    /**
     * 自动组卷
     *
     * @param examId 考试ID
     * @return 返回实际生成的试卷总分
     */
    @Override
    public int autoCompose(String examId) {
        //第一步: 获取考试信息
        Exam exam = examMapper.selectExamByExamId(examId);
        if (exam == null) {
            throw new RuntimeException("考试信息不存在, 请检查考试ID是否正确");
        }

        //第二步: 检查考试总分是否有效
        if (exam.getTotalScore() == null || exam.getTotalScore() <= 0) {
            throw new RuntimeException("考试总分必须大于0, 请设置合理的总分值");
        }

        //第三步: 检查考试分类是否设置
        if (exam.getCategoryId() == null || exam.getCategoryId().isEmpty()) {
            throw new RuntimeException("考试分类不能为空, 请指定考试所属的分类");
        }

        //第四步: 删除原有的试题关联
        eqService.deleteEqByExamId(examId);

        //第五步: 获取该分类下的所有题目
        Questions questionParam = new Questions();
        questionParam.setCategoryId(exam.getCategoryId());
        List<Questions> allQuestions = questionsService.selectQuestionsList(questionParam);

        //第六步: 检查该分类下是否有题目
        if (allQuestions.isEmpty()) {
            throw new RuntimeException("该分类下没有题目, 请先添加题目到题库");
        }

        //第七步: 按题型分组
        Map<String, List<Questions>> questionsByType = allQuestions.stream()
                .collect(Collectors.groupingBy(Questions::getType));

        //第八步: 检查是否包含所有必要的题型
        if (!questionsByType.containsKey("单选题")
                || !questionsByType.containsKey("多选题")
                || !questionsByType.containsKey("判断题")) {
            throw new RuntimeException("该分类下缺少必要的题型, 请确保题库中包含单选题和多选题以及判断题");
        }

        //第九步: 准备生成试卷题目列表
        Long totalScore = exam.getTotalScore();
        List<Eq> examQuestions = new ArrayList<>();

        //第十步: 为考试自动分配题目
        examQuestions = allocateQuestions(questionsByType, examId, totalScore, allQuestions);

        //第十一步: 检查是否成功分配到题目
        if (examQuestions.isEmpty()) {
            throw new RuntimeException("无法根据现有题目生成符合要求的试卷, 可能是因为题目数量不足");
        }

        //第十二步: 保存试题到数据库
        for (Eq eq : examQuestions) {
            eqService.insertEq(eq);
        }

        //第十三步: 计算实际生成试卷的总分并且返回
        return (int) examQuestions.stream().mapToLong(Eq::getScore).sum();
    }

    /**
     * 根据策略分配题目
     * @param questionsByType 按题型分类的题目
     * @param examId 考试ID
     * @param totalScore 总分
     * @param allQuestions 所有题目列表
     * @return 分配好的题目列表
     */
    private List<Eq> allocateQuestions(Map<String, List<Questions>> questionsByType,
                                       String examId, long totalScore, List<Questions> allQuestions) {
        //准备一个空列表来存放最终选中的题目
        List<Eq> result = new ArrayList<>();

        //定义每种题型的最低分值要求
        // - 单选题至少5分一道
        // - 判断题至少5分一道
        // - 多选题至少10分一道
        HashMap<String, Integer> minScoreMap = new HashMap<>();
        minScoreMap.put("单选题", 5);
        minScoreMap.put("判断题", 5);
        minScoreMap.put("多选题", 10);

        //定义题型优先级顺序
        //先考虑单选题, 然后多选题, 最后判断题
        List<String> questionTypes = Arrays.asList("单选题", "多选题", "判断题");

        //先为每种题型分配一道题, 确保试卷包含所有题型
        long currentScore = 0; //当前已分配的分数
        HashMap<String, Questions> selectedQuestions = new HashMap<>(); //临时存放选中的题目

        //为每种题型选择第一道题目
        for (String type : questionTypes) {
            List<Questions> questions = questionsByType.get(type);
            System.out.println(questions);
            //随机打乱题目顺序, 这样每次组卷都会得到不同的题目
            Collections.shuffle(questions);

            //选择第一道题目
            Questions question = questions.get(0);
            selectedQuestions.put(type, question);

            //加上这道题的最低分数
            int minScore = minScoreMap.get(type);
            currentScore += minScore;
        }

        //创建初始题目并且添加到结果列表中
        for (String type : questionTypes) {
            Questions question = selectedQuestions.get(type);
            System.out.println(question);
            Eq eq = new Eq();
            eq.setEqId(IdUtils.fastSimpleUUID());
            eq.setExamId(examId);
            eq.setQuestionId(question.getQuestionId());
            eq.setScore(Long.valueOf(minScoreMap.get(type)));
            eq.setCreateTime(new Date());
            result.add(eq);
        }

        //计算分配完基础题目后的剩余分数
        long remainingScore = totalScore - currentScore;

        //如果还有剩余分数, 继续分配题目直到分数用完或者题目不够
        while (remainingScore > 0) {
            //标记本轮是否成功添加了题目
            boolean added = false;
            //按优先级顺序为每种题型分配题目
            for (String type : questionTypes) {
                //如果剩余分数已经用完, 就停止添加
                if (remainingScore <= 0) {
                    break;
                }

                //获取该题型的所有题目, 并排除已经选中的题目
                List<Questions> questionsOfType = questionsByType.get(type);
                List<Questions> availableQuestions = questionsOfType.stream()
                        .filter(q -> result.stream().noneMatch(eq -> eq.getQuestionId().equals(q.getQuestionId())))
                        .collect(Collectors.toList());

                //如果该题型没有可选题目了, 就尝试下一种题型
                if (availableQuestions.isEmpty()) {
                    continue;
                }

                //随机选取一道题目
                Collections.shuffle(availableQuestions);
                Questions question = availableQuestions.get(0);

                //分配分数: 最多分配剩余分数, 但是至少分配该题型最低分
                Integer minScore = minScoreMap.get(type);
                long allocateScore = Math.min(remainingScore, minScore);

                //创建题目关联对象
                Eq eq = new Eq();
                eq.setEqId(IdUtils.fastSimpleUUID());
                eq.setExamId(examId);
                eq.setQuestionId(question.getQuestionId());
                eq.setScore(allocateScore);
                eq.setCreateTime(new Date());
                result.add(eq);

                //更新剩余分数
                remainingScore -= allocateScore;
                //标记本轮成功添加了题目
                added = true;
                //添加一道题后就跳出循环, 重新开始下一轮
                break;
            }

            //如果一轮下来没有添加任何题目 (可能所有题型的题目都用完了)
            //就跳出循环, 避免无限循环
            if (!added) {
                break;
            }
        }

        //如果还有分数没分配完, 就处理剩余分数
        if (remainingScore > 0) {
            //定义分数调整的优先级顺序: 先调整多选题 然后单选题 最后判断题
            List<String> adjustOrder = Arrays.asList("多选题", "单选题", "判断题");
            for (String type : adjustOrder) {
                //如果剩余分数已经分配完, 就停止调整
                if (remainingScore <= 0) break;

                //找到该类型的第一个题目进行分数调整
                Optional<Eq> eqOptional = result.stream()
                        .filter(eq -> {
                            //通过题目ID找到对应的题目对象
                            String questionId = eq.getQuestionId();
                            Questions q = allQuestions.stream()
                                    .filter(question -> question.getQuestionId().equals(questionId))
                                    .findFirst().orElse(null);
                            //检查题目类型是否匹配
                            return q != null && q.getType().equals(type);
                        })
                        .findFirst();

                //如果找到该类型的题目, 就把剩余分数添加到这个题目上
                if (eqOptional.isPresent()) {
                    Eq eq = eqOptional.get();
                    eq.setScore(eq.getScore() + remainingScore);
                    //剩余分数全部分配
                    remainingScore = 0;
                }
            }
        }

        //返回最终的题目分配列表结果
        return result;
    }

}
