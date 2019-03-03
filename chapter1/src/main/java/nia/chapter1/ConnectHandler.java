package nia.chapter1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by kerr.
 * <p>
 * Listing 1.2 ChannelHandler triggered by a callback
 * 链接建立时的回调函数
 * <p>
 * 􏶴
 */
public class ConnectHandler extends ChannelInboundHandlerAdapter {

    /**
     * channel 建立是回调
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {
        System.out.println(
                "Client IP " + ctx.channel().remoteAddress() + " connected");
    }
}