package day4.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MyThreadPoolTest {
    public static void main(String[] args) {
        MyThreadPool0 myThreadPool  = new MyThreadPool0(1,2,new ArrayBlockingQueue<>(1),1,TimeUnit.SECONDS,new MyThreadPool0.AbortPolicy());
        for (int i = 0; i < 5; i++) {
            myThreadPool.execute(() -> {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName());
            });
        }
        System.out.println("main thread end");

    }
}

