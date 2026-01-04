package com.huacai.exam.domain.dto;

import lombok.Data;

/**
 * 完成考试数据
 */
@Data
public class CompleteExamDTO {
    //考试ID
    private String examId;

    //题目ID
    private String questionId;

    //用户答题的答案
    private String userAnswer;
}
