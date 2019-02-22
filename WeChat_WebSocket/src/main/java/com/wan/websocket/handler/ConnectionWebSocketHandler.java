package com.wan.websocket.handler;

import com.wan.pojo.WebSocketMessage;
import com.wan.websocket.group.ChannelGroupUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 链接握手的处理器
 *
 * @Author 万星明
 * @Date 2019/2/22
 */
@Component
@ChannelHandler.Sharable  //使得处理器类能共用(在不存在线程安全的问题时,使用)
public class ConnectionWebSocketHandler extends SimpleChannelInboundHandler<WebSocketMessage> {


    @Autowired
    private ChannelGroupUtil channelGroupUtil;


    @Override
    protected void channelRead0(ChannelHandlerContext channelContext, WebSocketMessage webSocketMessage) throws Exception {

        if (webSocketMessage.getType() == 1){
            //握手请求,获得当前的管道对象channel,并且和设备对象进行统一管理
            Channel channel = channelContext.channel();
            //获得设备的UUID
            String deviceId = webSocketMessage.getDeviceId();
            //将设备UUID和通信管道绑定存放到Map中,统一管理
            channelGroupUtil.put(deviceId,channel);

            System.out.println("链接握手成功！"+webSocketMessage+";当前的通信链路数量为："+channelGroupUtil.size());
        }else {
            //如果不是握手请求,则向下一个处理器传递
            channelContext.fireChannelRead(webSocketMessage);
        }

    }


    /**
     * 异常处理
     * @param channelContext
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext channelContext, Throwable cause) throws Exception {
        //如果有异常,删除存放的键值对删除
        channelGroupUtil.removeByChannel(channelContext.channel());
    }

    /**
     * 上线处理
     * @param channelContext
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext channelContext) throws Exception {
        System.out.println("有一个客户端连接了！");
    }


    /**
     * 下线处理
     * @param channelContext
     * @throws Exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext channelContext) throws Exception {
        System.out.println("有一个客户端下线了！");
        //如果有链接下线,则将存放的键值对删除
        channelGroupUtil.removeByChannel(channelContext.channel());
    }


}
