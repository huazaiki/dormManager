package com.dormManager.backend.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RabbitConfiguration {

    @Bean("emailQueue")
    public Queue emailQueue() {
        return QueueBuilder.
                durable("mail")
                .build();
    }

    /**
     * 创建并配置用于RabbitMQ消息转换的SimpleMessageConverter。
     * 该转换器允许指定允许转换的包名模式列表，以提高安全性。
     *
     * @return SimpleMessageConverter对象，表示消息转换器
     */
    @Bean
    public SimpleMessageConverter converter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        converter.setAllowedListPatterns(List.of("com.dormManager.backend.*", "java.util.*"));
        return converter;
    }
}
