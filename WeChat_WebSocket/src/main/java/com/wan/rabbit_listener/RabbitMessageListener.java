package com.wan.rabbit_listener;

import com.alibaba.fastjson.JSON;
import com.wan.pojo.WebSocketMessage;
import com.wan.websocket.group.ChannelGroupUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author 万星明
 * @Date 2019/2/23
 */
@Component
@RabbitListener(queues = "message_queue_${ws.ip}:${ws.port}")
public class RabbitMessageListener {

    @Autowired
    private ChannelGroupUtil channelGroupUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitHandler
    public void rabbitHandler(WebSocketMessage webSocketMessage){
        System.out.println("网络通信链接服务器从RabbitMQ上监听的信息："+webSocketMessage);

        //获得设备号
        String deviceId = webSocketMessage.getDeviceId();

        if (deviceId == null){
            //通过接收请求的用户ID去Redis中查询设备号
            deviceId = (String) redisTemplate.opsForValue().get(webSocketMessage.getResponseId());
        }

        //通过设备号查询到和设备绑定的通信管道
        Channel channel = channelGroupUtil.get(deviceId);
        System.out.println("查询到之前登陆账号设备的channel通信管道："+channel);
        //判断,如果管道不为空,就发送指定的信息
        if (channel !=null){
            System.out.println("准备发送消息通知之前的设备下线！！");
            //将监听到的消息通过JSON转为JSON对象,转发到之前登陆的设备中去
            channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(webSocketMessage)));
        }

    }


}
