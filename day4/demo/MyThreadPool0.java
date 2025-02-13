package day4.demo;

/**
 * 定义核心组件：任务队列、工作线程、线程池状态管理等。
    实现任务提交方法（execute）。
    创建工作线程并管理它们的生命周期。
    处理任务队列的线程安全。
    实现拒绝策略，当队列满或线程池关闭时处理新任务。
    添加线程池的关闭功能。
 */

public class MyThreadPool0 {
    
    //1.我需要一个线程来执行task任务
    // 这样可以另起一个线程调度这个task了但是，这个线程无法复用
    public void execute(Runnable task){
        new Thread(task).start();
    }
}
