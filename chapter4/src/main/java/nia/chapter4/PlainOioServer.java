package nia.chapter4;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Listing 4.1 Blocking networking without Netty
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class PlainOioServer {


    /**
     * 使用JDK库实现的阻塞IO
     * 1.增加了多线程
     * 2.还是阻塞的
     */
    public void serve(int port) throws IOException {
        // serverScoket
        final ServerSocket socket = new ServerSocket(port);

        try {
            for (; ; ) {

                // 监听阻塞
                final Socket clientSocket = socket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                // 新线程处理链接
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        OutputStream out;
                        try {
                            // IO阻塞
                            out = clientSocket.getOutputStream();
                            out.write("Hi!\r\n".getBytes(Charset.forName("UTF-8")));
                            out.flush();
                            clientSocket.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                // 关系网络IO
                                clientSocket.close();
                            } catch (IOException ex) {
                                // ignore on close
                            }
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
