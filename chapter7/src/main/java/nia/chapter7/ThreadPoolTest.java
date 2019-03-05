package nia.chapter7;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

public class ThreadPoolTest {


    public static void main(String[] args) {


    }

    /**
     * 手动创建线程池 => 推荐
     * 1. 可以自动配置参数
     * 2. 避免耗尽系统的资源
     * <p>
     * // 默认创建线程池存在的风险,创建线程池需要注意的问题
     *
     * 查看底层的封装
     */
    public static void testDefaultError() {

        /**
         * 1. 默认创建的阻塞队列的大小 = 源码(Integer.MAX_VALUE); 会耗尽系统的资源； 已经发生过一次
         */
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        // 同时设置的最大值/coolSize
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

        /**
         * 2. 默认创建的最大线程数 = 源码(Integer.MAX_VALUE); 耗尽系统资源
         */
        // cool = 0, max = Integer.MAX_VALUE,空闲等待60L
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        // 支持 => 定时线程,延迟线程
        // cool = size, max = Integer.MAX_VALUE
        ExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);

    }


    // 手动创建线程池的方式
    public static void testManuallyCreate() {

        // 还是最大线程池 = Integer.MAX_VALUE
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("my-schedule-pool-%d").daemon(true).build());

        //  => 推荐
        // 线程池创建的基本方法
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("my-thread-pool-%d").build();


        /**
         * Creates a new {@code ThreadPoolExecutor} with the given initial
         * parameters.
         *
         * @param corePoolSize(线程池保留的大小) the number of threads to keep in the pool, even
         *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
         * @param maximumPoolSize(线程池最大值) the maximum number of threads to allow in the
         *        pool
         * @param keepAliveTime(线程池超过默认大小,空闲线程等待任务的时间)when the number of threads is greater than
         *        the core, this is the maximum time that excess idle threads
         *        will wait for new tasks before terminating.
         * @param unit the time unit for the {@code keepAliveTime} argument
         * @param workQueue (缓存submited的任务) the queue to use for holding tasks before they are
         *        executed.  This queue will hold only the {@code Runnable}
         *        tasks submitted by the {@code execute} method.
         * @param threadFactory(创建线程池的工具) the factory to use when the executor
         *        creates a new thread
         * @param handler (线程池满,队列满后的处理策略) the handler to use when execution is blocked
         *        because the thread bounds and queue capacities are reached
         * @throws IllegalArgumentException if one of the following holds:<br>
         *         {@code corePoolSize < 0}<br>
         *         {@code keepAliveTime < 0}<br>
         *         {@code maximumPoolSize <= 0}<br>
         *         {@code maximumPoolSize < corePoolSize}
         * @throws NullPointerException if {@code workQueue}
         *         or {@code threadFactory} or {@code handler} is null
         */

        // 最原始的创建线程池的方法
        ExecutorService executorService = new ThreadPoolExecutor(
                5,
                10,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024),
                threadFactory, new ThreadPoolExecutor.AbortPolicy());


        // 并发线程池 => 应用场景
        Executors.newWorkStealingPool();


        executorService.execute(()->{});
        executorService.submit(() ->{ });
    }



}
