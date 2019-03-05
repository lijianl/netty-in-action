package nia.chapter4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import java.util.Iterator;
import java.util.Set;


import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Listing 4.2 Asynchronous networking without Netty
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class PlainNioServer {


    /**
     * JDK库实现的非阻塞IO,
     * 1.selector监听
     * 2.非阻塞
     */
    public void serve(int port) throws IOException {


        /**
         * serverChannel,配置非阻塞
         */
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        // 绑定监听地址
        ServerSocket ss = serverChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        ss.bind(address);

        /**
         * Selector网络IO复用, 注册->监听事件
         */
        Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 缓存
        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());


        for (; ; ) {
            try {
                /**
                 * 阻塞直到selector上注册的某一个事件就绪
                 */
                selector.select();

            } catch (IOException ex) {
                ex.printStackTrace();
                //handle exception
                break;
            }


            // 返回所有的就绪事件
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();

            //  遍历处理就绪事件
            while (iterator.hasNext()) {

                SelectionKey key = iterator.next();
                iterator.remove();

                try {

                    /**
                     * 监听事件处理
                     */
                    if (key.isAcceptable()) {

                        // ServerSocketChannel
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();

                        // 监听到新的客户端链接，并注册到selector,并写入数据
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate());
                        System.out.println("Accepted connection from " + client);
                    }

                    /**
                     * 监听到写事件
                     */
                    if (key.isWritable()) {

                        // SocketChannel
                        SocketChannel client = (SocketChannel) key.channel();
                        // 使用缓存
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        // 写数据
                        while (buffer.hasRemaining()) {
                            if (client.write(buffer) == 0) {
                                break;
                            }
                        }
                        client.close();
                    }

                } catch (IOException ex) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException cex) {
                        // ignore on close
                    }
                }
            }
        }
    }
}

