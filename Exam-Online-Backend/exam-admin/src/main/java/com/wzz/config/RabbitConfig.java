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

@Configuration
public class RabbitConfig {

    @Value("${mq.exam.exchange:exam.submit.exchange}")
    private String exchangeName;

    @Value("${mq.exam.queue:exam.submit.queue}")
    private String queueName;

    @Value("${mq.exam.routing-key:exam.submit.key}")
    private String routingKey;

    @Bean
    public DirectExchange examSubmitExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Queue examSubmitQueue() {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Binding examSubmitBinding(Queue examSubmitQueue, DirectExchange examSubmitExchange) {
        return BindingBuilder.bind(examSubmitQueue).to(examSubmitExchange).with(routingKey);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
