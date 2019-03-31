package com.len.netty.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;


public class Server {

    private Integer port = 8001;

    public void start() {

        // 单例，所有的客户端共享一个
        // final EchoServerHandler serverHandler = new EchoServerHandler();
        // 线程池
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 引导启动
            ServerBootstrap b = new ServerBootstrap();
            // 配置线程池,channel类型,ChannelHandler,监听端口
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(
                            // Channel创建初始化
                            // 当新的链接建立时用于创建新的Channel,并加入同一个serverHandler
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                public void initChannel(SocketChannel ch) throws Exception {
                                    ch.pipeline()
                                            .addLast(new LoggingHandler(LogLevel.INFO))
                                            .addLast(new EchoServerHandler());
                                }
                            });


            /**
             * 绑定阻塞:.sync()阻塞当前线程(main主线程,也就是服务端主线程),直到绑定完成
             * 默认是Future.sync()；类似Thread.join()
             */
            ChannelFuture f = b.bind().sync();

            System.out.println(Server.class.getName() + " started and listening for connections on " + f.channel().localAddress());

            /**
             *  关闭阻塞:阻塞当前线程直到CloseFuture完成
             *  获取了Channel的CloseFuture(),并阻塞当前线程(server主线程),等待它被调用完成
             *  等价与Thread.join()函数
             */
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅关闭线程池
            //group.shutdownGracefully().sync();
            group.shutdownGracefully();
        }
    }


    public static void main(String[] args) {
        new Server().start();
    }

}
