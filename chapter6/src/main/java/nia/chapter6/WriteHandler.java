package nia.chapter6;


import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Listing 6.9 Caching a ChannelHandlerContext
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */


public class WriteHandler extends ChannelHandlerAdapter {

    // 缓存channel引用,不是线程安全的
    private ChannelHandlerContext ctx;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        // 缓存channel引用
        this.ctx = ctx;
    }
    public void send(String msg) {

        // 使用缓存的引用
        ctx.writeAndFlush(msg);
    }
}
