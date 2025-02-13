package day4;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
   public static void main2(String[] args) {
        try (ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 
            4, 10,TimeUnit.SECONDS ,
            new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.AbortPolicy())) {
        }
   }

   /**
    * 自定义线程池测试代码
    */
   public static void main(String[] args) {
        AiThreadPool myThreadPool = new AiThreadPool(
            2, // corePoolSize
            4, // maximumPoolSize
            10, // keepAliveTime
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10), // workQueue
            java.util.concurrent.Executors.defaultThreadFactory(),
            new AiThreadPool.AbortPolicy()
        );

        myThreadPool.execute(() -> {
            try{
                Thread.sleep(1000);
            }catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        });

        System.out.println("主线程结束");

   }

}
