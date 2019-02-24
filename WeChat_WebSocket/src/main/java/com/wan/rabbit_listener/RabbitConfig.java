package com.wan.rabbit_listener;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 万星明
 * @Date 2019/2/23
 */
@Configuration
public class RabbitConfig {

    @Value("${ws.ip}")
    private String ip;
    @Value("${ws.port}")
    private int port;


    /**
     * 创建一个队列
     * @return
     */
    @Bean
    public Queue getQueue(){
        return new Queue("message_queue_" + ip + ":" + port);
    }


    /**
     * 创建路由
     * @return
     */
    @Bean
    public FanoutExchange getFanoutExchange(){
        return new FanoutExchange("message_exchange");
    }



    /**
     * 绑定路由和队列
     * @return
     */
    @Bean
    public Binding binding(FanoutExchange getFanoutExchange, Queue getQueue){
        return BindingBuilder.bind(getQueue).to(getFanoutExchange);
    }


}
