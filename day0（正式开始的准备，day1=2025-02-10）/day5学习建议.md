### 学习计划：Spring IOC容器原理：Bean生命周期、循环依赖（手绘三级缓存流程图）（60-90分钟）

---

#### **第一阶段：视频学习（约30分钟）**

##### 学习目标

- 理解 Spring IOC 容器的核心原理，掌握 Bean 的生命周期（实例化、属性赋值、初始化、销毁）。
- 掌握 Spring 解决循环依赖的机制，重点理解三级缓存的设计与实现。
- 学习如何通过源码分析 Spring IOC 容器的工作流程，掌握核心类如 `DefaultListableBeanFactory` 和 `AbstractAutowireCapableBeanFactory` 的作用。

1. **Spring IOC 核心原理与 Bean 生命周期解析**  
   - **视频推荐**: 
        - [Spring IOC 容器原理解析](https://www.bilibili.com/video/BV1sk4y1P7UM)  
        - [Spring Bean 生命周期详解](https://www.bilibili.com/video/BV1Py4y1a7zg)
        - [Spring 循环依赖解决方案](https://www.bilibili.com/video/BV11arfYnEw9)
   - **时长**: 30分钟  

2. **三级缓存与循环依赖的实现**  
   - **视频推荐**: 
        - [Spring 三级缓存原理解析](https://www.bilibili.com/video/BV1mD4y1E7yX)    
   - **时长**: 15分钟  

3. **手绘三级缓存流程图**  
   - **视频推荐**: 
        - [手绘 Spring 三级缓存流程图](https://www.bilibili.com/video/BV1C3411S7c7/)  
   - **时长**: 15分钟  

---

#### **第二阶段：文档资料补充（约20分钟）**

1. **Bean 生命周期深度解析**  
   - **资料来源**: [《Spring Bean 生命周期详解》](https://blog.csdn.net/Java_Yangxiaoyuan/article/details/123456789)  
   - **学习目标**: 结合源码分析 Bean 的生命周期，如 `BeanDefinition` 的加载、`BeanFactoryPostProcessor` 和 `BeanPostProcessor` 的执行顺序。

2. **三级缓存源码对比**  
   - **资料来源**: [《Spring 三级缓存源码解析》](https://blog.csdn.net/weixin_43212356/article/details/98765432)  
   - **学习目标**: 对比三级缓存的实现逻辑，例如 `singletonObjects`、`earlySingletonObjects` 和 `singletonFactories` 的作用。

3. **循环依赖解决方案**  
   - **资料来源**: [《Spring 循环依赖解决方案》](https://blog.csdn.net/qq_35423154/article/details/112233445)  
   - **学习目标**: 掌握 Spring 如何通过三级缓存解决循环依赖问题，并理解其局限性。

---

#### **第三阶段：总结与练习（约20-30分钟）**

1. **知识回顾**  
   - **回答以下问题**：  
     - Spring Bean 的生命周期包括哪些阶段？每个阶段的作用是什么？  
     - 三级缓存是如何协同工作的？举例说明循环依赖的解决流程。  
     - 如果没有三级缓存，Spring 是否还能解决循环依赖？为什么？

2. **模拟面试题练习**  
   - **常见问题**：  
     - Spring 中的 `@Autowired` 和 `@Resource` 注解有什么区别？  
     - 如何手动创建一个 Bean 并注册到 Spring 容器中？  
     - 如果两个 Bean 存在循环依赖但未使用构造器注入，Spring 会如何处理？

3. **代码实践**  
   - **任务1**: 手写一个简单的 Spring Bean，观察其生命周期方法的调用顺序。  
   - **任务2**: 模拟两个 Bean 的循环依赖场景，验证三级缓存的解决过程。

4. **扩展思考**  
   - **深度问题**：  
     - 如果循环依赖涉及原型（Prototype）作用域的 Bean，Spring 会如何处理？  
     - 如何通过自定义 `BeanPostProcessor` 修改 Bean 的初始化逻辑？  
     - 对比 Spring IOC 容器与 Guice 在依赖注入上的优劣。

---

#### **总时长：60-90分钟**  
- **视频学习**: 约30分钟  
- **文档阅读**: 约20分钟  
- **总结与练习**: 约20-30分钟  

---

#### **附加资源**  
- **工具推荐**: 使用 IDEA 调试 Spring 源码，观察三级缓存的工作流程。  
- **面试加分项**: 结合 `DefaultListableBeanFactory` 源码，解释 `getSingleton()` 方法的实现逻辑。
