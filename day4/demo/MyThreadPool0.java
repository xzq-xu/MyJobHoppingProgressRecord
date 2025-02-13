package day4.demo;

import java.util.ArrayList;
import java.util.List;

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
    private List<Runnable> taskList = new ArrayList<>();

    //3、假设线程池只有一个线程，怎么让这个线程可以复用
    

    public void execute(Runnable task){
        taskList.add(task);
        taskList.forEach(e -> new Thread(e).start());
        // new Thread(task).start();
    }
}
