package nia.chapter2.echoserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Listing 2.2 EchoServer class
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class EchoServer {
    private final int port;


    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    public void start() throws Exception {

        // 单例，所有的客户端共享一个
        final EchoServerHandler serverHandler = new EchoServerHandler();

        // 线程池
        EventLoopGroup group = new NioEventLoopGroup();

        try {

            // 引导启动
            ServerBootstrap b = new ServerBootstrap();


            // 配置线程池,channel类型,ChannelHandler,监听端口
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(
                            // Channel创建初始化
                            // 当新的链接建立时用于创建新的Channel,并加入同一个serverHandler
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                public void initChannel(SocketChannel ch) throws Exception {
                                    ch.pipeline()
                                            .addLast(serverHandler);
                        }
                    });


            /**
             * 绑定阻塞:.sync()阻塞当前线程(main主线程,也就是服务端主线程),直到绑定完成
             * 默认是Future.sync()；类似Thread.join()
             */
            ChannelFuture f = b.bind().sync();

            System.out.println(EchoServer.class.getName() + " started and listening for connections on " + f.channel().localAddress());

            /**
             *  关闭阻塞:阻塞当前线程直到CloseFuture完成
             *  获取了Channel的CloseFuture(),并阻塞当前线程(server主线程),等待它被调用完成
             *  等价与Thread.join()函数
             */
            f.channel().closeFuture().sync();
        } finally {
            // 优雅关闭线程池
            group.shutdownGracefully().sync();
        }
    }
}
