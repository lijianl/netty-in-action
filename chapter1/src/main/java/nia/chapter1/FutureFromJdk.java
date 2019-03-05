package nia.chapter1;

import java.util.concurrent.*;


/**
 * jdk的Future实现
 */
public class FutureFromJdk {

    public static void main(String[] args) {

        ExecutorService service = Executors.newCachedThreadPool();
        try {

            Future<Integer> fInt = service.submit(new GetInt());

            while (true) {
                if (fInt.isDone()) {
                    System.out.printf("callBack = %d\n", fInt.get());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class GetInt implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            System.out.printf("tid = %s\n", Thread.currentThread().getId());
            Thread.sleep(1000 * 5);
            return Integer.valueOf(100);
        }
    }
}
