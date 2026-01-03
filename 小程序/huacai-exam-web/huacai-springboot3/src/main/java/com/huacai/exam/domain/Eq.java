package com.huacai.exam.domain;

import com.huacai.system.general.annotation.Excel;
import com.huacai.system.general.core.domain.BaseEntity;
import lombok.*;

/**
 * 试题对象 eq
 *
 * @author huacai
 * @date 2025-09-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Eq extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 试题ID */
    private String eqId;

    /** 考试ID */
    @Excel(name = "考试ID")
    private String examId;

    /** 题目ID */
    @Excel(name = "题目ID")
    private String questionId;

    /** 题目分数 */
    @Excel(name = "题目分数")
    private Long score;

    //考试标题
    private String examTitle;

    //题型
    private String type;

    //题目
    private String title;

    //选项
    private String options;

    //正确答案
    private String answer;

}
