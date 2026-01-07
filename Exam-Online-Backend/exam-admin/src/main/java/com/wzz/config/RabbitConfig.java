package com.wzz.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ============================================================
 * 技术亮点2: RabbitMQ 异步提交配置
 * ============================================================
 * 功能说明：
 * 1. 配置RabbitMQ的交换机、队列、路由键，实现消息的异步传递
 * 2. 使用Direct Exchange（直连交换机）模式，精确路由消息
 * 3. 配置持久化队列，防止消息丢失
 * 4. 使用Jackson JSON转换器，自动序列化/反序列化消息对象
 * 
 * 业务场景：
 * - 考生提交试卷时，前端立即返回"已提交"
 * - 后台通过MQ异步处理：保存记录、计算分数、统计错题
 * - 削峰填谷：高峰期（千人同考）时，队列缓冲请求，避免数据库压力
 * 
 * 架构优势：
 * - 解耦：提交接口不依赖评分逻辑，各自独立演进
 * - 削峰：队列平滑吸收流量高峰，消费端按能力处理
 * - 可靠：消息持久化，服务重启不丢失
 * 
 * @Configuration 标记为Spring配置类
 */
@Configuration
public class RabbitConfig {

    // ========== 从配置文件读取MQ参数（支持多环境配置） ==========
    
    /** 交换机名称（默认：exam.submit.exchange） */
    @Value("${mq.exam.exchange:exam.submit.exchange}")
    private String exchangeName;

    /** 队列名称（默认：exam.submit.queue） */
    @Value("${mq.exam.queue:exam.submit.queue}")
    private String queueName;

    /** 路由键（默认：exam.submit.key），用于绑定交换机和队列 */
    @Value("${mq.exam.routing-key:exam.submit.key}")
    private String routingKey;

    /**
     * ============================================================
     * 创建Direct Exchange（直连交换机）
     * ============================================================
     * Direct Exchange特点：
     * - 根据routing key精确匹配队列
     * - 消息只会发送到routing key完全匹配的队列
     * - 适合点对点的消息传递场景
     * 
     * 参数说明：
     * - name: 交换机名称
     * - durable=true: 持久化，服务器重启后交换机不会丢失
     * - autoDelete=false: 不自动删除，即使没有队列绑定也保留
     * 
     * @return DirectExchange实例
     */
    @Bean
    public DirectExchange examSubmitExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    /**
     * ============================================================
     * 创建持久化队列
     * ============================================================
     * 队列用于存储消息，消费者从队列中取出消息处理
     * 
     * QueueBuilder.durable()特点：
     * - 持久化队列：服务器重启后队列不会丢失
     * - 队列中的消息也会持久化到磁盘
     * - 保证消息可靠性，防止宕机丢失
     * 
     * @return Queue实例
     */
    @Bean
    public Queue examSubmitQueue() {
        return QueueBuilder.durable(queueName).build();
    }

    /**
     * ============================================================
     * 绑定队列到交换机
     * ============================================================
     * Binding定义了消息的路由规则：
     * - 将队列绑定到交换机
     * - 指定routing key
     * - 只有routing key匹配的消息才会进入该队列
     * 
     * 工作流程：
     * 1. 生产者发送消息到交换机，指定routing key
     * 2. 交换机根据routing key查找绑定关系
     * 3. 将消息路由到匹配的队列
     * 4. 消费者从队列中消费消息
     * 
     * @param examSubmitQueue 要绑定的队列
     * @param examSubmitExchange 要绑定的交换机
     * @return Binding实例
     */
    @Bean
    public Binding examSubmitBinding(Queue examSubmitQueue, DirectExchange examSubmitExchange) {
        return BindingBuilder.bind(examSubmitQueue).to(examSubmitExchange).with(routingKey);
    }

    /**
     * ============================================================
     * 配置消息转换器（JSON序列化）
     * ============================================================
     * Jackson2JsonMessageConverter作用：
     * - 发送消息时：自动将Java对象序列化为JSON字符串
     * - 接收消息时：自动将JSON字符串反序列化为Java对象
     * 
     * 优势：
     * - 可读性好：JSON格式便于调试和监控
     * - 跨语言：其他语言的消费者也能解析JSON
     * - 避免Java序列化的版本兼容问题
     * 
     * 示例：
     * ExamSubmitMessage对象 -> {"username":"zhangsan","examRecord":{...}}
     * 
     * @return MessageConverter实例
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * ============================================================
     * 配置RabbitTemplate（消息发送模板）
     * ============================================================
     * RabbitTemplate是Spring AMQP提供的消息发送工具：
     * - 封装了消息发送的底层细节
     * - 支持同步/异步发送
     * - 支持消息确认机制
     * 
     * 配置要点：
     * - 注入ConnectionFactory：管理与RabbitMQ的连接
     * - 设置MessageConverter：使用JSON转换器
     * 
     * 使用示例：
     * rabbitTemplate.convertAndSend(exchange, routingKey, message);
     * 
     * @param connectionFactory RabbitMQ连接工厂
     * @param messageConverter 消息转换器
     * @return RabbitTemplate实例
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 设置JSON转换器，自动序列化消息对象
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
