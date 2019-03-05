package nia.chapter6;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Listing 6.1 Releasing message resources
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */


// 释放消息的Handler
@Sharable
public class DiscardHandler extends ChannelInboundHandlerAdapter {


    /**
     * 释放消息,需要自己完成空间释放
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        /**
         *  1.释放当前Handler的引用  => 就可以释放空间
         *  2.msg是需要释放的空间
         */
        ReferenceCountUtil.release(msg);
    }

}

