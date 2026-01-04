package com.huacai.exam.domain;

import java.math.BigDecimal;
import com.huacai.system.general.annotation.Excel;
import com.huacai.system.general.core.domain.BaseEntity;
import lombok.*;

/**
 * 考试对象 exam
 *
 * @author huacai
 * @date 2025-09-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exam extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 考试ID */
    private String examId;

    /** 考试标题 */
    @Excel(name = "考试标题")
    private String title;

    /** 分类ID */
    private String categoryId;

    /** 考试时长(小时) */
    @Excel(name = "考试时长(小时)")
    private BigDecimal time;

    /** 总分 */
    @Excel(name = "总分")
    private Long totalScore;

    /** 及格分 */
    @Excel(name = "及格分")
    private Long passScore;

    //分类名称
    @Excel(name = "分类名称")
    private String categoryName;

    //试题数量
    private Integer eqCount;


}
