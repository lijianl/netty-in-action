package nia.chapter1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kerr.
 * <p>
 * Listing 1.1 Blocking I/O example
 */
public class BlockingIoExample {

    /**
     * Listing 1.1 Blocking I/O example
     * 只能处理一个链接
     */
    public void serve(int portNumber) throws IOException {
        ServerSocket serverSocket = new ServerSocket(portNumber);
        /**
         * 监听阻塞
         */
        Socket clientSocket = serverSocket.accept();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);
        String request, response;
        /**
         * IO阻塞
         */
        while ((request = in.readLine()) != null) {
            if ("Done".equals(request)) {
                break;
            }
            response = processRequest(request);
            out.println(response);
        }
    }

    /**
     * 业务逻辑
     */
    private String processRequest(String request) {
        return "Processed";
    }


    /**
     * 多线程方案=>把网络IO 和 业务逻辑放到统一个线程里面
     * <p>
     * 1. 线程栈大小1M
     * 2. 大量线程休眠
     * 3. 线程的context switch(上下文切换)
     * <p>
     * Netty方案=>accept 和 网路IO 交给Selector进行网络IO复用
     */

    public static ExecutorService service = Executors.newCachedThreadPool();

    public void serveThread(int portNumber) throws IOException {
        ServerSocket serverSocket = new ServerSocket(portNumber);
        while (true) {
            final Socket clientSocket = serverSocket.accept();

            service.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter out =
                                new PrintWriter(clientSocket.getOutputStream(), true);
                        String request, response;
                        while ((request = in.readLine()) != null) {
                            if ("Done".equals(request)) {
                                break;
                            }
                            response = processRequest(request);
                            out.println(response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
