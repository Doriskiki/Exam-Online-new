package com.huacai.exam.domain;

import com.huacai.system.general.annotation.Excel;
import com.huacai.system.general.core.domain.BaseEntity;
import lombok.*;

/**
 * 题目对象 questions
 *
 * @author huacai
 * @date 2025-09-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Questions extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 题目ID */
    private String questionId;

    /** 题型 */
    @Excel(name = "题型")
    private String type;

    /** 分类ID */
    @Excel(name = "分类ID")
    private String categoryId;

    /** 题目标题 */
    @Excel(name = "题目标题")
    private String title;

    /** 选项 */
    @Excel(name = "选项")
    private String options;

    /** 正确答案 */
    @Excel(name = "正确答案")
    private String answer;

    /** 题目解析 */
    @Excel(name = "题目解析")
    private String analysis;

    //分类名称
    private String categoryName;


}
