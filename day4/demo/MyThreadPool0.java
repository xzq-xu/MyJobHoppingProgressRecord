package day4.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

import javax.management.RuntimeErrorException;

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
    private final BlockingQueue<Runnable> blockingQueue;

    //3、假设线程池只有一个线程，怎么让这个线程可以复用

    //4、while(true) 十分浪费cpu资源，有没有一种容器可以在没有元素时可以阻塞获取呢？

    //6、需要一个容器存放多个线程，数量不确定，新增一个参数
    private final int corePoolSize;
    //7.2 新开线程需要设置最大的新开数量，这里直接设置最大总线程数
    private final  int maximumPoolSize;
    final List<Thread> coreList =  new ArrayList<>();
    //7.3 新开的线程存放在另一个容器中，表示为辅助线程、临时线程
    final List<Thread> supportList =  new ArrayList<>();
    private final  long timedOut;
    private final  TimeUnit timeUnit;
    private final RejectHandler rejectHandler;


    //5、目前是单个线程的线程池，为了让其他线程也能复用，抽取这个唯一线程的Runnable

    //7.5 非核心线程不应该一直阻塞，需要超时结束


    //将可配置参数设定到构造方法中

    public MyThreadPool0(int corePoolSize,int maximumPoolSize, BlockingQueue<Runnable> blockingQueue,long timedOut,TimeUnit timeUnit,RejectHandler rejectHandler){
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.blockingQueue = blockingQueue;
        this.timeUnit = timeUnit;
        this.timedOut = timedOut;
        this.rejectHandler = rejectHandler;
    }



    public void execute(Runnable task){
        //6.2 在提交任务的时候我们就需要判断线程数量是否达到设定值
        if (coreList.size() < corePoolSize) {
            Thread thread = new CoreThread();
            coreList.add(thread);
            thread.start();
        }

        if (blockingQueue.offer(task)) {
            return;
        }

        //6.3 线程数达到设定值，不需要再创建线程了，直接往阻塞队列放入任务    
        //offer 返回是否添加成功
   
        //7、如果放入阻塞队列失败则表示，队列已满，此时考虑要新开线程
        //7.4 如果线程数还没达到最大值，将创建新线程并放入supportList中
        //注意这一步，需要保证线程安全这里省略
        if (coreList.size() + supportList.size() < maximumPoolSize) {
            Thread thread = new SupportThread();
            supportList.add(thread);
            thread.start();
        }
        //8、 创建完成辅助线程（临时线程）后再次将task放入blockingQueue
        //如果再次出现放入失败，此时需要考虑拒绝任务
        //定义一个拒绝策略接口来实现自定义的拒绝方式
        //添加一个拒绝策略参数，接受拒绝策略
        if (blockingQueue.offer(task)) {
            return;
        };

        rejectHandler.reject(task, this);
        
    }


    //将coreTask 和 supportTask封装为两个类
    class CoreThread extends Thread  {
        
        @Override
        public void run(){
            while (true) {
                try {
                    Runnable command =  blockingQueue.take();
                    command.run();
                } catch (Exception e) {
                   throw new RuntimeException(e);
                }
            }
        }
    }

    class SupportThread extends Thread  {
        
        @Override
        public void run(){
            while (true) {
                try {
                    Runnable command =  blockingQueue.poll(timedOut,timeUnit);
                    if(command == null){
                        //超时未获取到任务，则退出循环
                        break;
                    }
                    command.run();
                } catch (Exception e) {
                   throw new RuntimeException(e);
                }
            }
            System.out.println("supportThread end");
        }
    }


    //添加两个个默认的拒绝策略
    public static class AbortPolicy  implements RejectHandler{

        @Override
        public void reject(Runnable task, MyThreadPool0 threadPool0) {
            throw new RuntimeException("默认拒绝了");
        }
        
    }
    public static class DiscardPolicy  implements RejectHandler{

        @Override
        public void reject(Runnable task, MyThreadPool0 threadPool0) {
            
        }
        
    }

}
