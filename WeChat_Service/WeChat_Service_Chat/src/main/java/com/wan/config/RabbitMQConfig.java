package com.wan.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 万星明
 * @Date 2019/2/23
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public Exchange getFanoutExchange(){
        return new FanoutExchange("message_exchange");
    }

}
