package com.huacai.exam.domain;

import com.huacai.system.general.annotation.Excel;
import com.huacai.system.general.core.domain.BaseEntity;
import lombok.*;

/**
 * 学习指南对象 guide
 *
 * @author huacai
 * @date 2025-10-02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Guide extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 学习指南ID */
    private String guideId;

    /** 类型 */
    @Excel(name = "类型")
    private String type;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 要点总结 */
    @Excel(name = "要点总结")
    private String summary;

    /** 实用技巧 */
    @Excel(name = "实用技巧")
    private String tips;


}
