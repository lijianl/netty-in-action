package nia.chapter1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Created by kerr.
 * <p>
 * Listing 1.3 Asynchronous connect
 * <p>
 * Listing 1.4 Callback in action
 */


/**
 * ChannleFuture + 回调的基本实现
 */
public class ConnectExample {


    /**
     * Listing 1.3 Asynchronous connect
     * <p>
     * Listing 1.4 Callback in action
     */

    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    public static void connect() {

        Channel channel = CHANNEL_FROM_SOMEWHERE; //reference form somewhere

        /**
         * 客户端链接服务器，此处不阻塞(ChannelFuture)
         */
        ChannelFuture future = channel.connect(new InetSocketAddress("192.168.0.1", 25));

        /**
         * ChannelFuture 增加监听回调函数
         */
        future.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) {

                /**
                 * 链接成功:回调
                 */
                if (future.isSuccess()) {

                    ByteBuf buffer = Unpooled.copiedBuffer("Hello", Charset.defaultCharset());

                    /***
                     * 写IO操作也不会阻塞,此处依然可以增加Listner
                     */
                    ChannelFuture wf = future.channel().writeAndFlush(buffer);
                } else {


                    /**
                     * 链接异常处理
                     */
                    Throwable cause = future.cause();
                    cause.printStackTrace();
                }
            }
        });

    }
}