package nia.chapter6;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Listing 6.11 Invalid usage of @Sharable
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */

// 可以共享
@Sharable
public class UnsharableHandler extends ChannelInboundHandlerAdapter {


    // 实例属性不能保证线程共享 => 需要通过ThreadLocal实现
    private int count;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        count++;
        System.out.println("inboundBufferUpdated(...) called the "
                + count + " time");
        ctx.fireChannelRead(msg);
    }
}

