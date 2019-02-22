package com.wan.websocket.handler;

import com.wan.pojo.WebSocketMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

/**
 * @Author 万星明
 * @Date 2019/2/22
 */
@Component
@ChannelHandler.Sharable
public class HeartWebSocketHandler extends SimpleChannelInboundHandler<WebSocketMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelContext, WebSocketMessage webSocketMessage) throws Exception {
        if (webSocketMessage.getType() == 2){
            //当前是一个心跳消息
            int i=0;
            System.out.println("心跳处理器正在心跳："+(i++));
        }else {
            //如果不是心跳消息,透传下去
            channelContext.fireChannelRead(webSocketMessage);
        }
    }
}
