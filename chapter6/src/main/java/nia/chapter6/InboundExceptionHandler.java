package nia.chapter6;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Listing 6.12 Basic inbound exception handling
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class InboundExceptionHandler extends ChannelInboundHandlerAdapter {



    //处理入站消息异常，
    //查看源码默认实现=>ctx.fireExceptionCaught(cause); 继续传递
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
        Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
