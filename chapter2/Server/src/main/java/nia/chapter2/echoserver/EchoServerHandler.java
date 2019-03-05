package nia.chapter2.echoserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Listing 2.1 EchoServerHandler
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */


/**
 * 多个线程共享单个实例
 * <p>
 * <p>
 * !!!!!!服务端使用这个!!!!!!!!
 */


/**
 * 注意
 * 1.ChannelInboundHandlerAdapter的继承关系
 * 2.@Sharable:实现安全共享(应该是单例)? 实现的原理
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {


    /***
     * ChannelHandlerContext: 所有ChannelHandler共享的上下文环境
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));

        /**
         * NIO的 => 不阻塞,
         */
        ctx.write(in);
        /**
         * 哪个性能好
         * ctx.writeAndFlush(in);
         */
    }


    /**
     * channelRead最后一次调用：把数据刷新的remote, 并 close Channel
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        /**
         * 读完成并释放空间
         * 1. 刷出到Client,并释放缓存
         * 2. 关闭Channel
         */
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }


    /**
     * 读取异常, 关闭channel
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        // 关闭Channel,也可以重试建立链接(不同的异常处理方式)
        ctx.close();
    }
}
