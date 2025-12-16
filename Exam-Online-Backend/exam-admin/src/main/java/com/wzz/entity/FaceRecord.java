package com.wzz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 人脸识别记录实体类
 */
@Data
@TableName("face_record")
public class FaceRecord {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    /**
     * 用户ID
     */
    private Integer userId;
    
    /**
     * 考试ID
     */
    private Integer examId;
    
    /**
     * 识别类型：register-注册, verify-验证
     */
    private String recordType;
    
    /**
     * 是否匹配成功
     */
    private Boolean matched;
    
    /**
     * 相似度分数
     */
    private Double similarity;
    
    /**
     * 人脸特征向量（512维，JSON格式存储）
     */
    private String embedding;
    
    /**
     * 识别时间
     */
    private LocalDateTime recordTime;
    
    /**
     * 备注信息
     */
    private String remark;
}
