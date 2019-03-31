package com.len.netty.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

public class Client {

    private String host = "127.0.0.1";
    private Integer port = 8001;

    public void start() {

        // 客户端channel的线程池
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            // Channel的类型需要与线程池的类型一致
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(
                            //用户初始化创建Channel
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                public void initChannel(SocketChannel ch) throws Exception {
                                    ch.pipeline()
                                            .addLast(new LoggingHandler(LogLevel.INFO))
                                            .addLast(new EchoClientHandler());
                                }
                            });


            // 阻塞主线程(客户端线程)，等待connect()调用完成
            ChannelFuture f = b.connect().sync();
            // 阻塞主线程(客户端线程)，等待CloseFuture()调用完成
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放线程池
            //group.shutdownGracefully().sync();
            group.shutdownGracefully();
        }
    }


    public static void main(String[] args) {

    }
}
