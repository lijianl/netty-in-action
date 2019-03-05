package nia.chapter7;

import java.util.Collections;
import java.util.List;

/**
 * Listing 7.1 Executing tasks in an event loop
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */



public class EventLoopExamples {
    /**
     * Listing 7.1 Executing tasks in an event loop
     *
     *
     *
     *
     * 事件循环模型: readyEvents = 需要处理的任务
     * 事件执行的顺序FIFO
     *
     * */
    public static void executeTaskInEventLoop() {
        boolean terminated = true;
        //...
        while (!terminated) {
            List<Runnable> readyEvents = blockUntilEventsReady();
            for (Runnable ev: readyEvents) {
                ev.run();
            }
        }
    }


    /**
     * 任务列表
     * @return
     */
    private static final List<Runnable> blockUntilEventsReady() {
        return Collections.<Runnable>singletonList(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
