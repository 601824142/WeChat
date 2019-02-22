package com.wan.websocket;

import com.wan.websocket.handler.ConnectionWebSocketHandler;
import com.wan.websocket.handler.HeartWebSocketHandler;
import com.wan.websocket.handler.TextWebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author 万星明
 * @Date 2019/2/21
 */
@Component
public class WebSocketServer implements CommandLineRunner {

    //新建主从事件循环池
    private static final EventLoopGroup masterGroup = new NioEventLoopGroup();
    private static final EventLoopGroup slaveGroup = new NioEventLoopGroup();

    @Value("${ws.ip}")
    private String ip;

    @Value("${ws.port}")
    private int port;

    @Value("${zk.host}")
    private String zkHost;

    //服务器的Channel对象
    private Channel channel;

    @Autowired
    private TextWebSocketHandler textWebSocketHandler;

    @Autowired
    private ConnectionWebSocketHandler connectionWebSocketHandler;

    @Autowired
    private HeartWebSocketHandler heartWebSocketHandler;

    /**
     * 初始化WebSocket服务器
     */
    private ChannelFuture init(){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                //配置引导类为主从模式
                .group(masterGroup, slaveGroup)
                //配置通信管道类
                .channel(NioServerSocketChannel.class)
                //配置处理器类
                .childHandler(new ChannelInitializer() {

                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        //新建处理链
                        ChannelPipeline pipeline = channel.pipeline();

                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpObjectAggregator(512 * 1024));
                        //处理Http的升级握手，并且处理所有的控制帧（Ping、Pong、Close）
                        pipeline.addLast(new WebSocketServerProtocolHandler("/wechat"));
                        //添加一个读超时的处理器, 在10秒钟之内，如果当前这个客户端（channel），没有读取到任何内容，则自动关闭
                        pipeline.addLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS));
                        //自定义WebSocket的文本帧处理器(验证消息格式)
                        pipeline.addLast(textWebSocketHandler);
                        //自定义链接握手的处理器(处理链接握手)
                        pipeline.addLast(connectionWebSocketHandler);
                        //自定义保持心跳处理器(验证客户端是否异常掉线)
                        pipeline.addLast(heartWebSocketHandler);
                    }
                });

        //给管道绑定端口
        ChannelFuture future = serverBootstrap.bind(port);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(channelFuture.isSuccess()){
                    //绑定11000端口成功
                    System.out.println("WebSocket服务已经启动，端口为：" + port);
                    channel = future.channel();

                    //------注册Zookeeper------
                    connZookeeper();
                } else {
                    //绑定9000端口失败
                    //优雅关闭Netty服务
                    destory();
                    //失败的原因打印到控制台
                    Throwable e = channelFuture.cause();
                    e.printStackTrace();
                }
            }
        });

        return future;
    }



    /**
     * 注销Netty服务
     */
    private void destory(){
        if(channel != null && channel.isActive()){
            channel.close();
        }
        //优雅关闭主从线程组
        masterGroup.shutdownGracefully();
        slaveGroup.shutdownGracefully();
    }


    /**
     * 链接Zookeeper
     */
    private void connZookeeper(){
        try {
            //连接Zookeeper
            ZooKeeper zooKeeper = new ZooKeeper(zkHost, 60, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {

                }
            });

            //在根路径下创建一个Netty节点
            Stat stat = zooKeeper.exists("/netty", null);
            if(stat == null){
                zooKeeper.create(
                        "/netty",
                        null,
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            }

            //创建当前服务器所对应的临时节点
            //节点的值 ： ip:port
            String value = ip + ":" + port;
            String str = zooKeeper.create("/netty/server", value.getBytes("utf-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println("服务器在zookeeper上创建了一个临时节点：" + str);

        } catch (Exception e) {
            e.printStackTrace();
            destory();
        }
    }


    /**
     * 主要运行方法
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        //运行通信管道初始化
        ChannelFuture channelFuture = init();

        //设置一个运行时的销毁回调
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                destory();
            }
        });

        //同步阻塞
        channelFuture.channel().closeFuture().syncUninterruptibly();
    }



}
