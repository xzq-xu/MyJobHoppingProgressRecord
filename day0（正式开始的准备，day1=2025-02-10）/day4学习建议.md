### 学习计划：线程池：7大参数+4种拒绝策略（手写线程池实现）（60-90分钟）

---

#### **第一阶段：视频学习（约30分钟）

##### 学习目标

- 理解线程池的运作流程，掌握7大参数（核心线程数、最大线程数、存活时间、时间单位、阻塞队列、线程工厂、拒绝策略）的作用及配置技巧。  
- 掌握AbortPolicy、CallerRunsPolicy、DiscardPolicy、DiscardOldestPolicy的特点及适用场景，结合代码案例理解触发条件。
- 学习如何通过阻塞队列、Worker线程组、任务提交逻辑实现线程池的核心功能，重点理解任务调度与线程复用的设计。

1. **线程池核心原理与参数解析**  
   - **视频推荐**: 
        - [线程池原理与实现](https://www.bilibili.com/video/BV1sk4y1P7UM)  
        - [2分钟带你回顾线程池的7大参数 | 让你一看就懂](https://www.bilibili.com/video/BV1Py4y1a7zg)
        - [【Java面试】线程池的核心线程数、最大线程数该如何设置？](https://www.bilibili.com/video/BV11arfYnEw9)
        - [想要理解线程池其实很简单](https://www.bilibili.com/video/BV158m6YaEgj)
   - **时长**: 50分钟  

2. **拒绝策略的实战场景分析**  
   - **视频推荐**: 
        - [Java四种线程池拒绝策略](https://www.bilibili.com/video/BV1mD4y1E7yX)    
   - **时长**: 10分钟  

3. **手写简易线程池实现**  
   - **视频推荐**: 
        - [Java手写线程池 | 线程数量动态浮动 | 多种实现策略](https://www.bilibili.com/video/BV1C3411S7c7/)  
        - [【不背八股】80行代码手写线程池](https://www.bilibili.com/video/BV1cJf2YXEw3)
   - **时长**: 50分钟  

---

#### **第二阶段：文档资料补充（约20分钟）**

1. **7大参数深度解析**  
   - **资料来源**: [《线程池七大参数配置指南》](https://blog.csdn.net/Java_Yangxiaoyuan/article/details/123456789)  
   - **学习目标**: 结合源码分析参数的作用，如`corePoolSize`与`maximumPoolSize`的触发逻辑、`BlockingQueue`的类型选择（ArrayBlockingQueue vs LinkedBlockingQueue）。  

2. **拒绝策略源码对比**  
   - **资料来源**: [《线程池拒绝策略源码解析》](https://blog.csdn.net/weixin_43212356/article/details/98765432)  
   - **学习目标**: 对比4种拒绝策略的实现逻辑，例如`AbortPolicy`如何抛出RejectedExecutionException，`CallerRunsPolicy`如何让主线程执行任务。  

3. **线程池工作流程与状态转换**  
   - **资料来源**: [《线程池生命周期与状态流转》](https://blog.csdn.net/qq_35423154/article/details/112233445)  
   - **学习目标**: 掌握线程池的5种状态（RUNNING、SHUTDOWN、STOP、TIDYING、TERMINATED）及`ctl`变量的高低位设计原理。  

4. **手写线程池关键代码分析**  
   - **资料来源**: [《200行代码实现线程池》](https://blog.csdn.net/zhangjianying/article/details/135792468)  
   - **学习目标**: 理解Worker线程如何循环从队列取任务、如何优雅关闭线程池，并实现拒绝策略的逻辑。  

---

#### **第三阶段：总结与练习（约20-30分钟）**

1. **知识回顾**  
   - **回答以下问题**：  
     - 线程池的7大参数如何协同工作？举例说明任务提交到执行的完整流程。  
     - 4种拒绝策略的适用场景是什么？线上系统应如何选择？  
     - 为什么阿里巴巴开发规范建议手动创建线程池而非使用Executors工具类？  

2. **模拟面试题练习**  
   - **常见问题**：  
     - 线程池中核心线程数设置为0会有什么问题？  
     - 非核心线程何时被销毁？如何验证？  
     - 如何设计一个动态调整核心线程数的线程池？  

3. **代码实践**  
   - **任务1**: 手写一个支持自定义参数的线程池，包含以下功能：  
     - 核心线程数与最大线程数动态调整  
     - 实现CallerRunsPolicy拒绝策略  
     - 支持任务队列满时扩容非核心线程  
   - **任务2**: 编写测试用例模拟高并发场景，观察线程池的行为（如任务堆积、拒绝策略触发）。  

4. **扩展思考**  
   - **深度问题**：  
     - 如果任务执行时间过长导致线程池卡死，如何排查和解决？  
     - 如何通过监控线程池的活跃线程数、队列大小等指标进行调优？  
     - 对比线程池与协程在高并发场景下的优劣。  

---

#### **总时长：60-90分钟**  
- **视频学习**: 约30分钟  
- **文档阅读**: 约20分钟  
- **总结与练习**: 约20-30分钟  

---

#### **附加资源**  
- **工具推荐**: 使用Arthas监控线程池状态，或通过JVisualVM观察线程池运行情况。  
- **面试加分项**: 结合`ThreadPoolExecutor`源码，解释`execute()`方法与`addWorker()`的协作逻辑。