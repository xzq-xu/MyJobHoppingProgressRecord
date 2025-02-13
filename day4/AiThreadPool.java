package day4;

import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class AiThreadPool implements java.util.concurrent.Executor {
    // region 核心配置参数
    private final BlockingQueue<Runnable> workQueue;      // 任务阻塞队列
    private final HashSet<Worker> workers = new HashSet<>(); // 工作线程集合
    private final ReentrantLock mainLock = new ReentrantLock(); // 控制workers访问的锁
    private volatile int corePoolSize;    // 核心线程数
    private volatile int maximumPoolSize; // 最大线程数
    private volatile long keepAliveTime;  // 非核心线程空闲存活时间(纳秒)
    private volatile RejectedExecutionHandler handler; // 拒绝策略处理器
    private volatile ThreadFactory threadFactory;      // 线程工厂
    // endregion
    
    // region 线程池状态控制
    // 使用AtomicInteger同时保存运行状态(高3位)和有效线程数(低29位)
    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
    private static final int COUNT_BITS = Integer.SIZE - 3; // 29位用于线程计数
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1; // 线程数最大值
    
    // 线程池状态常量（高3位）
    private static final int RUNNING    = -1 << COUNT_BITS; // 111状态：接收新任务并处理队列任务
    private static final int SHUTDOWN   =  0 << COUNT_BITS; // 000状态：不接收新任务，但处理队列任务
    private static final int STOP       =  1 << COUNT_BITS; // 001状态：不接收新任务，不处理队列任务，中断进行中任务
    private static final int TIDYING    =  2 << COUNT_BITS; // 010状态：所有任务终止，workerCount=0
    private static final int TERMINATED =  3 << COUNT_BITS; // 011状态：terminated()方法执行完成
    
    // 状态解析工具方法
    private static int runStateOf(int c)     { return c & ~CAPACITY; } // 解析运行状态
    private static int workerCountOf(int c)  { return c & CAPACITY; }  // 解析工作线程数
    private static int ctlOf(int rs, int wc) { return rs | wc; }       // 组合状态和线程数
    
    /**
     * 线程池构造函数
     * @param corePoolSize 核心线程数（常驻线程）
     * @param maximumPoolSize 最大线程数（临时线程+核心线程）
     * @param keepAliveTime 非核心线程空闲存活时间
     * @param unit 时间单位
     * @param workQueue 任务队列（推荐使用有界队列）
     * @param threadFactory 线程创建工厂
     * @param handler 拒绝策略处理器
     */
    public AiThreadPool(int corePoolSize, int maximumPoolSize,
                       long keepAliveTime, TimeUnit unit,
                       BlockingQueue<Runnable> workQueue,
                       ThreadFactory threadFactory,
                       RejectedExecutionHandler handler) {
        // 参数有效性检查（此处应添加）
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = unit.toNanos(keepAliveTime); // 统一转为纳秒
        this.workQueue = workQueue;
        this.threadFactory = threadFactory;
        this.handler = handler;
    }

    /**
     * Worker类：包装工作线程和任务，继承AQS实现简单不可重入锁
     * 作用：
     * 1. 维护线程执行任务的中断状态
     * 2. 实现简单的锁机制来跟踪任务执行状态
     */
    private final class Worker extends AbstractQueuedSynchronizer implements Runnable {
        final Thread thread;      // 实际执行任务的线程
        Runnable firstTask;       // 初始任务（可能为空）
        
        Worker(Runnable firstTask) {
            setState(-1); // 禁止中断直到runWorker（AQS状态初始化）
            this.firstTask = firstTask;
            this.thread = threadFactory.newThread(this); // 使用线程工厂创建新线程
        }
        
        // 尝试获取锁（实现不可重入锁）
        protected boolean tryAcquire(int acquires) {
            if (compareAndSetState(0, 1)) { // CAS操作设置状态
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }
        
        // 释放锁
        protected boolean tryRelease(int releases) {
            setExclusiveOwnerThread(null);
            setState(0); // 重置锁状态
            return true;
        }
        
        // 加锁方法
        public void lock()        { acquire(1); }
        public boolean tryLock()  { return tryAcquire(1); }
        public void unlock()      { release(1); }
        
        // 线程执行入口
        public void run() {
            runWorker(this); // 调用外部类的任务执行逻辑
        }
    }
    
    // 状态比较方法：当前状态是否 >= 给定状态
    private static boolean runStateAtLeast(int c, int s) {
        return (c & ~CAPACITY) >= s;
    }
    
    private volatile boolean allowCoreThreadTimeOut = false; // 是否允许核心线程超时

    /**
     * 处理工作线程退出
     * @param w 要退出的worker
     * @param completedAbruptly 是否异常结束（true表示任务执行抛出异常）
     */
    private void processWorkerExit(Worker w, boolean completedAbruptly) {
        if (completedAbruptly)
            decrementWorkerCount(); // 异常结束需要主动减少计数

        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock(); // 加锁保证线程安全
        try {
            workers.remove(w); // 从工作集合中移除
        } finally {
            mainLock.unlock();
        }

        tryTerminate(); // 尝试转换到TERMINATED状态

        int c = ctl.get();
        if (runStateLessThan(c, STOP)) { // 如果还在运行或关闭状态
            if (!completedAbruptly) { // 正常结束
                int min = allowCoreThreadTimeOut ? 0 : corePoolSize;
                if (min == 0 && ! workQueue.isEmpty())
                    min = 1; // 队列非空时至少保持一个线程
                if (workerCountOf(c) >= min)
                    return; // 当前线程数足够
            }
            addWorker(null, false); // 创建新worker代替
        }
    }
    
    // 状态比较方法：当前状态是否 < 给定状态
    private static boolean runStateLessThan(int c, int s) {
        return c < s;
    }
    
    // 任务执行前钩子方法（可扩展）
    protected void beforeExecute(Thread t, Runnable r) { }
    
    // 任务执行后钩子方法（可扩展）
    protected void afterExecute(Runnable r, Throwable t) { }
    
    /**
     * 核心工作循环：不断从队列获取任务并执行
     * @param w 当前工作线程包装对象
     */
    private void runWorker(Worker w) {
        Thread wt = Thread.currentThread();
        Runnable task = w.firstTask;
        w.firstTask = null;
        w.unlock(); // 允许中断
        boolean completedAbruptly = true; // 是否异常结束标志
        
        try {
            // 循环获取任务（首次执行firstTask，之后从队列获取）
            while (task != null || (task = getTask()) != null) {
                w.lock(); // 加锁表示开始处理任务
                
                // 如果线程池正在停止，确保线程被中断
                if ((runStateAtLeast(ctl.get(), STOP) ||
                     (Thread.interrupted() && runStateAtLeast(ctl.get(), STOP))) &&
                    !wt.isInterrupted())
                    wt.interrupt();
                
                try {
                    beforeExecute(wt, task); // 执行前处理
                    try {
                        task.run(); // 实际执行任务
                    } finally {
                        afterExecute(task, null); // 执行后处理
                    }
                } finally {
                    task = null; // 清除任务引用
                    w.unlock();  // 解锁表示任务完成
                }
            }
            completedAbruptly = false; // 正常退出循环
        } finally {
            processWorkerExit(w, completedAbruptly); // 处理worker退出
        }
    }
    
    /**
     * 从队列获取任务（核心方法）
     * @return 获取到的任务，null表示需要回收线程
     */
    private Runnable getTask() {
        boolean timedOut = false; // 上次poll是否超时
        
        for (;;) {
            int c = ctl.get();
            int rs = runStateOf(c);

            // 检查线程池状态是否至少是SHUTDOWN且（状态>=STOP或队列为空）
            if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
                decrementWorkerCount(); // 减少工作线程计数
                return null; // 返回null会导致工作线程退出
            }

            int wc = workerCountOf(c); // 当前工作线程数
            boolean timed = wc > corePoolSize || allowCoreThreadTimeOut;

            // 检查是否需要减少工作线程（线程数超过最大值 或 超时且需要回收）
            if ((wc > maximumPoolSize || (timed && timedOut))
                && (wc > 1 || workQueue.isEmpty())) {
                if (compareAndDecrementWorkerCount(c)) // CAS减少线程数
                    return null;
                continue; // 减少失败则重试
            }

            try {
                // 根据是否设置超时选择不同的获取方法
                Runnable r = timed ?
                    workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) : // 超时等待
                    workQueue.take(); // 阻塞等待
                if (r != null)
                    return r;
                timedOut = true; // 标记超时
            } catch (InterruptedException retry) {
                timedOut = false; // 中断后重置超时标记
            }
        }
    }
    
    /**
     * 提交任务到线程池（核心方法）
     * @param command 要执行的任务
     * @throws NullPointerException 任务为null时抛出
     */
    public void execute(Runnable command) {
        if (command == null) throw new NullPointerException();
        
        int c = ctl.get();
        
        // 阶段1：尝试创建核心线程
        if (workerCountOf(c) < corePoolSize) {
            if (addWorker(command, true)) // true表示创建核心线程
                return;
            c = ctl.get(); // 创建失败后重新获取状态
        }
        
        // 阶段2：尝试加入任务队列
        if (isRunning(c) && workQueue.offer(command)) {
            int recheck = ctl.get();
            // 二次检查线程池状态
            if (!isRunning(recheck) && remove(command)) // 如果已关闭则移除任务
                reject(command);
            else if (workerCountOf(recheck) == 0) // 保持至少一个工作线程
                addWorker(null, false); 
        }
        // 阶段3：尝试创建非核心线程
        else if (!addWorker(command, false)) // false表示创建临时线程
            reject(command); // 执行拒绝策略
    }
    
    // CAS增加工作线程计数
    private boolean compareAndIncrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect + 1);
    }
    
    // CAS减少工作线程计数
    private boolean compareAndDecrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect - 1);
    }
    
    /**
     * 添加工作线程（核心内部方法）
     * @param firstTask 新线程首先执行的任务（可能为null）
     * @param core true表示创建核心线程，false表示创建临时线程
     * @return 是否成功创建并启动线程
     */
    private boolean addWorker(Runnable firstTask, boolean core) {
        retry:
        for (;;) { // 外层循环处理状态变化
            int c = ctl.get();
            int rs = runStateOf(c);

            // 检查线程池状态是否允许添加worker
            if (rs >= SHUTDOWN &&
                ! (rs == SHUTDOWN && firstTask == null && ! workQueue.isEmpty()))
                return false;

            for (;;) { // 内层循环处理线程数变更
                int wc = workerCountOf(c);
                if (wc >= CAPACITY || // 超过最大容量
                    wc >= (core ? corePoolSize : maximumPoolSize)) // 超过对应类型限制
                    return false;
                if (compareAndIncrementWorkerCount(c)) // CAS增加线程计数
                    break retry; // 成功则跳出双层循环
                c = ctl.get(); // 重新获取状态
                if (runStateOf(c) != rs) // 状态发生变化则重新外层循环
                    continue retry;
            }
        }

        boolean workerStarted = false; // 线程是否启动
        boolean workerAdded = false;   // worker是否添加到集合
        Worker w = null;
        try {
            w = new Worker(firstTask); // 创建新worker
            final Thread t = w.thread;
            if (t != null) {
                final ReentrantLock mainLock = this.mainLock;
                mainLock.lock(); // 加锁保证线程安全
                try {
                    // 再次检查线程池状态
                    int rs = runStateOf(ctl.get());
                    if (rs < SHUTDOWN || // 运行状态
                        (rs == SHUTDOWN && firstTask == null)) { // 关闭状态但需要处理队列任务
                        if (t.isAlive()) // 线程已经启动则抛出异常
                            throw new IllegalThreadStateException();
                        workers.add(w); // 添加到worker集合
                        workerAdded = true;
                    }
                } finally {
                    mainLock.unlock();
                }
                if (workerAdded) {
                    t.start(); // 启动线程
                    workerStarted = true;
                }
            }
        } finally {
            if (! workerStarted)
                addWorkerFailed(w); // 添加失败后的清理
        }
        return workerStarted;
    }
    
    /**
     * 添加worker失败后的回滚处理
     * @param w 要清理的worker
     */
    private void addWorkerFailed(Worker w) {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock(); // 加锁保证线程安全
        try {
            if (w != null)
                workers.remove(w); // 从集合中移除
            decrementWorkerCount(); // 减少线程计数
            tryTerminate(); // 尝试转换到终止状态
        } finally {
            mainLock.unlock();
        }
    }
    
    // 循环减少工作线程计数直到成功
    private void decrementWorkerCount() {
        do {} while (! compareAndDecrementWorkerCount(ctl.get()));
    }
    
    /**
     * 尝试转换到TERMINATED状态（简化实现）
     */
    final void tryTerminate() {
        advanceRunState(TERMINATED); // 先推进到TIDYING状态
    }
    
    // 推进线程池状态到目标状态
    private void advanceRunState(int targetState) {
        for (;;) { // CAS循环直到状态更新成功
            int c = ctl.get();
            if (runStateAtLeast(c, targetState) || // 已经处于目标状态或更高状态
                ctl.compareAndSet(c, ctlOf(targetState, workerCountOf(c)))) // CAS更新状态
                break;
        }
    }
    
    // region 拒绝策略相关
    /**
     * 自定义拒绝策略接口（与JDK保持一致）
     */
    public interface RejectedExecutionHandler {
        void rejectedExecution(Runnable r, AiThreadPool executor);
    }
    
    /**
     * 默认拒绝策略：直接抛出RejectedExecutionException
     */
    public static class AbortPolicy implements RejectedExecutionHandler {
        public void rejectedExecution(Runnable r, AiThreadPool e) {
            throw new RuntimeException("Task rejected: " + r.toString());
        }
    }
    // endregion
    
    /**
     * 执行拒绝策略
     * @param command 被拒绝的任务
     */
    private void reject(Runnable command) {
        handler.rejectedExecution(command, this); // 委托给策略处理器
    }
    
    /**
     * 判断线程池是否在运行
     * @param c 当前控制状态值
     * @return true表示在RUNNING状态
     */
    private boolean isRunning(int c) {
        return c < SHUTDOWN;
    }

    /**
     * 从队列中移除任务
     * @param task 要移除的任务
     * @return 是否移除成功
     */
    public boolean remove(Runnable task) {
        return workQueue.remove(task);
    }
    
    // 其他辅助方法（可继续扩展）...
}
