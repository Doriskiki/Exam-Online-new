package com.huacai.exam.domain;

import com.huacai.system.general.annotation.Excel;
import com.huacai.system.general.core.domain.BaseEntity;
import lombok.*;

/**
 * 考试结果对象 er
 *
 * @author huacai
 * @date 2025-09-30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Er extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 考试结果ID */
    private String erId;

    /** 考试ID */
    @Excel(name = "考试ID")
    private String examId;

    /** 题目ID */
    @Excel(name = "题目ID")
    private String questionId;

    /** 用户答案 */
    @Excel(name = "用户答案")
    private String userAnswer;

    /** 正确答案 */
    @Excel(name = "正确答案")
    private String correctAnswer;

    /** 是否正确 */
    @Excel(name = "是否正确")
    private String isCorrect;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    //考试名称
    private String examTitle;

    //题目名称
    private String title;

    //用户名
    private String userName;

    //题目解析
    private String analysis;

    //选项
    private String options;

    //题型
    private String type;

}
