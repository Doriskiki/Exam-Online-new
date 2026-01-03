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

@Slf4j
@Component
@RequiredArgsConstructor
public class ExamSubmitConsumer {

    private final UserServiceImpl userService;
    private final AnswerServiceImpl answerService;
    private final ExamQuestionServiceImpl examQuestionService;
    private final ExamRecordServiceImpl examRecordService;

    @RabbitListener(queues = "${mq.exam.queue:exam.submit.queue}")
    public void handleExamSubmit(ExamSubmitMessage message) {
        if (message == null || message.getExamRecord() == null) {
            log.warn("[exam-submit] message empty, skip");
            return;
        }
        ExamRecord examRecord = message.getExamRecord();
        String username = message.getUsername();
        log.info("[exam-submit] received submit from user={}, examId={}, creditImgUrl={}", username, examRecord.getExamId(), examRecord.getCreditImgUrl());

        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            log.error("[exam-submit] user not found, username={}", username);
            return;
        }
        examRecord.setUserId(user.getId());

        // 生成 recordId（与原同步逻辑保持一致）
        if (examRecord.getRecordId() == null) {
            List<ExamRecord> examRecords = examRecordService.list(new QueryWrapper<>());
            int id = 1;
            if (!examRecords.isEmpty()) {
                id = examRecords.get(examRecords.size() - 1).getRecordId() + 1;
            }
            examRecord.setRecordId(id);
        }

        // 计算逻辑分数与错题
        try {
            List<Answer> answers = answerService.list(new QueryWrapper<Answer>().in("question_id", Arrays.asList(examRecord.getQuestionIds().split(","))));
            HashMap<String, String> scoreMap = new HashMap<>();
            ExamQuestion examQuestion = examQuestionService.getOne(new QueryWrapper<ExamQuestion>().eq("exam_id", examRecord.getExamId()));
            if (examQuestion != null) {
                String[] ids = examQuestion.getQuestionIds().split(",");
                String[] scores = examQuestion.getScores().split(",");
                for (int i = 0; i < ids.length; i++) {
                    scoreMap.put(ids[i], scores[i]);
                }
            }
            int logicScore = 0;
            StringBuilder errorIds = new StringBuilder();
            String[] userAnswers = examRecord.getUserAnswers().split("-");
            String[] recordQuestionIds = examRecord.getQuestionIds().split(",");
            for (int i = 0; i < recordQuestionIds.length; i++) {
                int index = SaltEncryption.getIndex(answers, Integer.parseInt(recordQuestionIds[i]));
                if (index != -1) {
                    if (Objects.equals(userAnswers[i], answers.get(index).getTrueOption())) {
                        logicScore += Integer.parseInt(scoreMap.getOrDefault(recordQuestionIds[i], "0"));
                    } else {
                        errorIds.append(recordQuestionIds[i]).append(",");
                    }
                }
            }
            examRecord.setLogicScore(logicScore);
            if (errorIds.length() > 0) {
                examRecord.setErrorQuestionIds(errorIds.substring(0, errorIds.length() - 1));
            }
        } catch (Exception e) {
            log.error("[exam-submit] score calc failed, recordId={}, examId={}, err= {}", examRecord.getRecordId(), examRecord.getExamId(), e.getMessage(), e);
        }

        examRecord.setExamTime(new Date());
        examRecordService.save(examRecord);
        log.info("[exam-submit] saved recordId={} for userId={}, creditImgUrl={}", examRecord.getRecordId(), examRecord.getUserId(), examRecord.getCreditImgUrl());
    }
}
