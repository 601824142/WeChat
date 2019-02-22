package com.wan.Controller;

import com.wan.pojo.WebSocketMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 万星明
 * @Date 2019/2/22
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;


    public void sendMessage(@RequestBody WebSocketMessage webSocketMessage){
        System.out.println("聊天服务需要发送消息到通信服务上："+webSocketMessage);

        //通过RabbitMQ通知通信服务集群服务器(如果传过来的消息中没有设备UUID)
        if (webSocketMessage.getDeviceId() == null){
            //通过登陆的用户的ID,在Redis中查询是否有对应的设备标识的键值对,如果有,取出
            String deviceId = (String) redisTemplate.opsForValue().get(webSocketMessage.getResponseId());
            webSocketMessage.setDeviceId(deviceId);
        }

        rabbitTemplate.convertAndSend("message_exchange","",webSocketMessage);
    }

}
