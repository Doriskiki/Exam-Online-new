package com.huacai.exam.domain.vo;

import lombok.Data;

/**
 * 个人考试统计VO (考试次数和通过次数)
 */
@Data
public class ExamStatusVO {
    //考试次数
    private Integer examCount;

    //通过次数
    private Integer passCount;
}
