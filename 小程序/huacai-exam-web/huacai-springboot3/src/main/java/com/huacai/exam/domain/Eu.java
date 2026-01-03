package com.huacai.exam.domain;

import com.huacai.system.general.annotation.Excel;
import com.huacai.system.general.core.domain.BaseEntity;
import lombok.*;

/**
 * 考试用户分配对象 eu
 *
 * @author huacai
 * @date 2025-09-29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Eu extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 考试用户分配ID */
    private String euId;

    /** 考试ID */
    @Excel(name = "考试ID")
    private String examId;

    /** 题目总数 */
    @Excel(name = "题目总数")
    private Long questionCount;

    /** 正确题数 */
    @Excel(name = "正确题数")
    private Long correctCount;

    /** 得分 */
    @Excel(name = "得分")
    private Long score;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 考试标题 */
    private String title;

    /** 考试时长 */
    private String time;

    /** 及格分 */
    private String passScore;

    /** 总分 */
    private String totalScore;

    /** 用户名 */
    private String userName;
}
