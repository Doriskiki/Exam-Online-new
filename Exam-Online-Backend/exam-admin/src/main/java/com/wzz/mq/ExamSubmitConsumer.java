package com.wzz.mq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzz.Util.SaltEncryption;
import com.wzz.entity.Answer;
import com.wzz.entity.ExamQuestion;
import com.wzz.entity.ExamRecord;
import com.wzz.entity.User;
import com.wzz.service.impl.AnswerServiceImpl;
import com.wzz.service.impl.ExamQuestionServiceImpl;
import com.wzz.service.impl.ExamRecordServiceImpl;
import com.wzz.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * ============================================================
 * 技术亮点2: RabbitMQ 异步消费者 - 考试提交处理
 * ============================================================
 * 功能说明：
 * 1. 监听RabbitMQ队列，接收考试提交消息
 * 2. 异步处理考试记录：保存数据、计算分数、统计错题
 * 3. 实现削峰填谷，避免高峰期数据库压力过大
 * 
 * 业务流程：
 * 1. 接收消息：从队列中获取ExamSubmitMessage
 * 2. 查询用户：根据username查询用户ID
 * 3. 生成记录ID：保持与原同步逻辑一致
 * 4. 计算分数：对比考生答案与标准答案，累加得分
 * 5. 统计错题：记录答错的题目ID列表
 * 6. 保存记录：将完整的考试记录写入数据库
 * 
 * 性能优势：
 * - 前端响应时间：从50-200ms（同步写库）降至5-20ms（MQ投递）
 * - 削峰能力：队列可缓冲高峰流量，消费端按能力处理
 * - 可扩展性：可水平扩展多个消费者实例，提升吞吐量
 * 
 * @Slf4j Lombok注解，自动生成日志对象log
 * @Component 标记为Spring组件，自动注册为Bean
 * @RequiredArgsConstructor Lombok注解，自动生成final字段的构造函数
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExamSubmitConsumer {

    // ========== 依赖注入的Service层（通过构造函数注入） ==========
    
    /** 用户服务：查询用户信息 */
    private final UserServiceImpl userService;
    
    /** 答案服务：查询题目标准答案 */
    private final AnswerServiceImpl answerService;
    
    /** 考试题目服务：查询试卷题目和分值配置 */
    private final ExamQuestionServiceImpl examQuestionService;
    
    /** 考试记录服务：保存考试记录 */
    private final ExamRecordServiceImpl examRecordService;

    /**
     * ============================================================
     * 核心方法：处理考试提交消息
     * ============================================================
     * @RabbitListener注解说明：
     * - queues: 监听的队列名称（从配置文件读取）
     * - 自动从队列中拉取消息并调用此方法
     * - 支持自动ACK（消息确认）机制
     * 
     * 处理流程：
     * 1. 消息校验：检查消息和考试记录是否为空
     * 2. 用户查询：根据username获取用户ID
     * 3. 记录ID生成：如果没有ID则自动生成
     * 4. 分数计算：对比答案，累加得分，记录错题
     * 5. 数据保存：将完整记录写入数据库
     * 
     * @param message 考试提交消息对象
     */
    @RabbitListener(queues = "${mq.exam.queue:exam.submit.queue}")
    public void handleExamSubmit(ExamSubmitMessage message) {
        // ========== 步骤1：消息校验 ==========
        if (message == null || message.getExamRecord() == null) {
            log.warn("[exam-submit] message empty, skip");
            return;
        }
        
        ExamRecord examRecord = message.getExamRecord();
        String username = message.getUsername();
        log.info("[exam-submit] received submit from user={}, examId={}, creditImgUrl={}", 
                username, examRecord.getExamId(), examRecord.getCreditImgUrl());

        // ========== 步骤2：查询用户信息 ==========
        // 根据用户名查询用户对象，获取用户ID
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            log.error("[exam-submit] user not found, username={}", username);
            return;
        }
        // 设置考试记录的用户ID
        examRecord.setUserId(user.getId());

        // ========== 步骤3：生成记录ID（与原同步逻辑保持一致） ==========
        // 如果消息中没有recordId，则自动生成
        // 注意：这里使用简单的递增ID，生产环境建议使用数据库自增或雪花算法
        if (examRecord.getRecordId() == null) {
            List<ExamRecord> examRecords = examRecordService.list(new QueryWrapper<>());
            int id = 1;
            if (!examRecords.isEmpty()) {
                // 获取最后一条记录的ID，加1作为新ID
                id = examRecords.get(examRecords.size() - 1).getRecordId() + 1;
            }
            examRecord.setRecordId(id);
        }

        // ========== 步骤4：计算逻辑分数与统计错题 ==========
        try {
            // 4.1 查询所有题目的标准答案
            // 根据试卷中的题目ID列表，批量查询答案
            List<Answer> answers = answerService.list(
                new QueryWrapper<Answer>().in("question_id", Arrays.asList(examRecord.getQuestionIds().split(",")))
            );
            
            // 4.2 构建题目分值映射表（题目ID -> 分值）
            HashMap<String, String> scoreMap = new HashMap<>();
            ExamQuestion examQuestion = examQuestionService.getOne(
                new QueryWrapper<ExamQuestion>().eq("exam_id", examRecord.getExamId())
            );
            if (examQuestion != null) {
                // 解析题目ID列表和分值列表
                String[] ids = examQuestion.getQuestionIds().split(",");
                String[] scores = examQuestion.getScores().split(",");
                // 建立映射关系：题目ID -> 分值
                for (int i = 0; i < ids.length; i++) {
                    scoreMap.put(ids[i], scores[i]);
                }
            }
            
            // 4.3 逐题对比答案，计算得分
            int logicScore = 0;  // 总得分
            StringBuilder errorIds = new StringBuilder();  // 错题ID列表
            
            // 解析考生答案（格式：A-B-C-D，用"-"分隔）
            String[] userAnswers = examRecord.getUserAnswers().split("-");
            // 解析题目ID列表（格式：1,2,3,4，用","分隔）
            String[] recordQuestionIds = examRecord.getQuestionIds().split(",");
            
            // 遍历每道题目
            for (int i = 0; i < recordQuestionIds.length; i++) {
                // 在答案列表中查找当前题目的索引
                int index = SaltEncryption.getIndex(answers, Integer.parseInt(recordQuestionIds[i]));
                if (index != -1) {
                    // 对比考生答案与标准答案
                    if (Objects.equals(userAnswers[i], answers.get(index).getTrueOption())) {
                        // 答对：累加该题分值
                        logicScore += Integer.parseInt(scoreMap.getOrDefault(recordQuestionIds[i], "0"));
                    } else {
                        // 答错：记录错题ID
                        errorIds.append(recordQuestionIds[i]).append(",");
                    }
                }
            }
            
            // 4.4 设置计算结果
            examRecord.setLogicScore(logicScore);  // 设置总得分
            if (errorIds.length() > 0) {
                // 去掉最后一个逗号，设置错题ID列表
                examRecord.setErrorQuestionIds(errorIds.substring(0, errorIds.length() - 1));
            }
        } catch (Exception e) {
            // 分数计算失败时记录日志，但继续保存已有数据
            log.error("[exam-submit] score calc failed, recordId={}, examId={}, err= {}", 
                    examRecord.getRecordId(), examRecord.getExamId(), e.getMessage(), e);
        }

        // ========== 步骤5：保存考试记录到数据库 ==========
        examRecord.setExamTime(new Date());  // 设置考试时间为当前时间
        examRecordService.save(examRecord);  // 持久化到数据库
        
        log.info("[exam-submit] saved recordId={} for userId={}, creditImgUrl={}", 
                examRecord.getRecordId(), examRecord.getUserId(), examRecord.getCreditImgUrl());
    }
}
