package nia.chapter6;

import io.netty.channel.*;

/**
 * Listing 6.14 Adding a ChannelFutureListener to a ChannelPromise
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class OutboundExceptionHandler extends ChannelOutboundHandlerAdapter {




    //
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {


        // 2. 通过ChannelPromise添加异常处理
        promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture f) {
                if (!f.isSuccess()) {
                    // 输出栈
                    f.cause().printStackTrace();
                    // 关闭channle
                    f.channel().close();
                }
            }
        });
    }
}
