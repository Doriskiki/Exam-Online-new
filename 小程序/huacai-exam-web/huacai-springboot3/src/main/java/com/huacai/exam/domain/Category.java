package com.huacai.exam.domain;

import com.huacai.system.general.annotation.Excel;
import com.huacai.system.general.core.domain.BaseEntity;
import lombok.*;

/**
 * 题目分类对象 category
 *
 * @author huacai
 * @date 2025-09-25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分类ID */
    private String categoryId;

    /** 分类名称 */
    @Excel(name = "分类名称")
    private String name;

    /** 排序 */
    @Excel(name = "排序")
    private Long sortOrder;

    /** 分类名称 */
    private String categoryName;

    /** 分类题目数量 */private String questionCount;



}
