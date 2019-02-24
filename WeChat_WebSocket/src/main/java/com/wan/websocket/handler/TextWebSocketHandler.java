package com.wan.websocket.handler;

import com.alibaba.fastjson.JSON;
import com.wan.pojo.WebSocketMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

/**
 * @Author 万星明
 * @Date 2019/2/21
 */

@Component
@ChannelHandler.Sharable
public class TextWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext context, TextWebSocketFrame textWebSocketFrame) throws Exception {
        //获得链接对象
        Channel channel = context.channel();
        //获得当前信息
        String message = textWebSocketFrame.text();
        System.out.println("接收到客户端的消息：" + message);
        //将message转为WebSocketMessage对象
        WebSocketMessage socketMessage = null;
        try {
            socketMessage = JSON.parseObject(message, WebSocketMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //如果接收的消息为空,或者接收的消息类型大于0,则符合前后台约定的数据格式
        if (socketMessage !=null && socketMessage.getType() > 0){
            //消息透传,将消息传递给下一个channelHandler处理
            context.fireChannelRead(socketMessage);
        }else {
            //不符合格式要求
            System.out.println("服务器接收的客户端消息数据格式异常！！");
            channel.close();
        }

    }



}
