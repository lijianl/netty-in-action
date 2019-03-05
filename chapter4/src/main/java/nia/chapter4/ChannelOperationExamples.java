package nia.chapter4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Listing 4.5 Writing to a Channel
 * <p>
 * Listing 4.6 Using a Channel from many threads
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */


public class ChannelOperationExamples {

    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    /**
     * channel 写数据
     */
    public static void writingToChannel() {
        // Get the channel reference from somewhere
        Channel channel = CHANNEL_FROM_SOMEWHERE;

        ByteBuf buf = Unpooled.copiedBuffer("your data", CharsetUtil.UTF_8);
        ChannelFuture cf = channel.writeAndFlush(buf);

        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                if (future.isSuccess()) {
                    System.out.println("Write successful");
                } else {
                    System.err.println("Write error");
                    future.cause().printStackTrace();
                }
            }
        });
    }

    /**
     * Channel使用多线程的模式,是线程安全的,消息有序
     */
    public static void writingToChannelFromManyThreads() {

        // Get the channel reference from somewhere
        final Channel channel = CHANNEL_FROM_SOMEWHERE;
        final ByteBuf buf = Unpooled.copiedBuffer("your data", CharsetUtil.UTF_8);

        Runnable writer = new Runnable() {
            /**
             * 线程内使用channel的引用
             */
            @Override
            public void run() {
                channel.write(buf.duplicate());
            }
        };
        Executor executor = Executors.newCachedThreadPool();
        // write in one thread
        executor.execute(writer);
        // write in another thread
        executor.execute(writer);
        //...
    }
}
