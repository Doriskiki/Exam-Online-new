package com.wzz.mq;

import com.wzz.entity.ExamRecord;
import lombok.Data;

import java.io.Serializable;

/**
 * ============================================================
 * 技术亮点2: 考试提交消息实体
 * ============================================================
 * 功能说明：
 * 封装考试提交的消息数据，用于在RabbitMQ中传递
 * 
 * 消息内容：
 * - username: 考生用户名（用于查询用户ID）
 * - examRecord: 考试记录对象（包含试卷ID、答案、分数等）
 * 
 * 序列化：
 * - 实现Serializable接口，支持对象序列化
 * - 配合Jackson2JsonMessageConverter，自动转换为JSON
 * 
 * 使用场景：
 * 1. 生产者：TeacherController.addExamRecord() 创建消息并发送到MQ
 * 2. 消费者：ExamSubmitConsumer.handleExamSubmit() 接收消息并处理
 * 
 * @Data Lombok注解，自动生成getter/setter/toString等方法
 */
@Data
public class ExamSubmitMessage implements Serializable {

    /** 考生用户名（用于查询用户信息） */
    private String username;

    /** 考试记录对象（包含答题详情） */
    private ExamRecord examRecord;
}
