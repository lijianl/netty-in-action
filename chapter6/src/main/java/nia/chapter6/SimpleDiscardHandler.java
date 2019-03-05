package nia.chapter6;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Listing 6.2 Using SimpleChannelInboundHandler
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */





// 不需要自己释放空间
@Sharable
public class SimpleDiscardHandler extends SimpleChannelInboundHandler<Object> {


    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
        // No need to do anything special
        // 不需要自己实现空间,channelRead()已经释放
    }
}
