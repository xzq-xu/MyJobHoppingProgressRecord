package day4.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 定义核心组件：任务队列、工作线程、线程池状态管理等。
    实现任务提交方法（execute）。
    创建工作线程并管理它们的生命周期。
    处理任务队列的线程安全。
    实现拒绝策略，当队列满或线程池关闭时处理新任务。
    添加线程池的关闭功能。
 */

public class MyThreadPool0 {
    
    //1、我需要一个线程来执行task任务
    // 这样可以另起一个线程调度这个task了但是，这个线程无法复用

    //2、我需要一个容器来存放这个task，以便于调用
    private BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(1024);

    //3、假设线程池只有一个线程，怎么让这个线程可以复用
    private Thread thread = new Thread(() -> {
        //4、while(true) 十分浪费cpu资源，有没有一种容器可以在没有元素时可以阻塞获取呢？
        //使用BlockingQueue
        while (true) {
            try {
                Runnable command =  blockingQueue.take();
                command.run();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
        }
    },"唯一线程");
    
    {
        thread.start();
    }


    public void execute(Runnable task){
        //offer 表示是否添加成功
        boolean offer = blockingQueue.offer(task);
        if (!offer) {
            
        }
    }
}
