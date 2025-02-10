

### **一、学习大纲（5分钟）**
1. **核心目标**：理解 JVM 内存分区的职责、生命周期和常见问题
2. **重点内容**：
   - 堆（Heap）
   - 栈（Stack）
   - 方法区（Method Area）
   - 本地方法栈（Native Method Stack）
   - 程序计数器（Program Counter Register）
3. **学习路径**：概念 → 作用 → 配置参数 → 常见异常

---

### **二、高效学习资源（直接点击学习）**
#### **1. 视频教程（20分钟）**
- [【【java】jvm内存模型全面解析】](https://www.bilibili.com/video/BV12t411u726/?share_source=copy_web&vd_source=a1dd9eea0d9126db0cb5dbd916f5514e)（中文，动画图解）
- [白话JVM内存结构，死也忘不了](https://www.bilibili.com/video/BV1Q64y1h7PT?spm_id_from=333.788.recommend_more_video.2&vd_source=0e9540a3b4e6ed455c3814157af345a7)

#### **2. 文档资料（20分钟）**
- [Oracle官方文档 - JVM内存结构](https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-2.html#jvms-2.5)（精读第2.5节）
---

### **三、核心知识点整理（15分钟）**
#### **1. 堆（Heap）**
- **作用**：存放对象实例和数组（所有线程共享）
- **生命周期**：随JVM启动创建，垃圾回收主战场
- **关键参数**：
  - `-Xms`：初始堆大小（默认物理内存1/64）
  - `-Xmx`：最大堆大小（默认物理内存1/4）
- **常见异常**：`OutOfMemoryError: Java heap space`

#### **2. 栈（Stack）**
- **作用**：存储方法调用的栈帧（线程私有）
- **生命周期**：与线程同生共死
- **关键参数**：
  - `-Xss`：栈大小（默认1MB，Linux/x64）
- **常见异常**：`StackOverflowError`

#### **3. 方法区（Method Area）**
- **作用**：存储类信息、常量、静态变量（JDK8后由元空间实现）
- **关键参数**：
  - `-XX:MetaspaceSize`：元空间初始大小
  - `-XX:MaxMetaspaceSize`：元空间最大大小
- **常见异常**：`OutOfMemoryError: Metaspace`

#### **4. 本地方法栈（Native Method Stack）**
- **作用**：为Native方法（如C/C++代码）服务
- **特点**：与Java栈类似，但服务于JNI调用
- **常见异常**：`StackOverflowError`

#### **5. 程序计数器（Program Counter Register）**
- **作用**：记录当前线程执行位置（线程私有）
- **特点**：唯一无OOM的区域，纯内存结构

---

### **四、对比表格（5分钟）**
| 区域              | 线程共享？ | 存储内容                  | 异常类型               |
|-------------------|------------|--------------------------|------------------------|
| **堆**            | 是         | 对象实例                 | OOM                   |
| **栈**            | 否         | 栈帧（局部变量表等）      | StackOverflowError    |
| **方法区**        | 是         | 类元数据、常量池          | OOM（Metaspace）      |
| **本地方法栈**    | 否         | Native方法调用信息        | StackOverflowError    |
| **程序计数器**    | 否         | 当前执行指令地址          | 无                    |

---

### **五、实践与调试（10分钟）**
1. **模拟堆溢出**：
   ```java
   // 不断创建大对象
   List<byte[]> list = new ArrayList<>();
   while (true) {
       list.add(new byte[1024 * 1024]); // 1MB
   }
   ```
   - 运行参数：`-Xmx10m -Xms10m -XX:+HeapDumpOnOutOfMemoryError`

2. **模拟栈溢出**：
   ```java
   public static void recursiveCall() {
       recursiveCall(); // 无限递归
   }
   ```
   - 运行参数：`-Xss160k`

---

### **六、总复习建议（5分钟）**
- **思维导图**：[JVM内存模型速查表](https://whimsical.com/jvm-memory-model-FsXf3zEeUzY7bHv3mP5mRZ)
- **常见面试题**：
  1. 堆和栈的区别是什么？
  2. 方法区在JDK8前后的实现有何不同？
  3. 如何定位`StackOverflowError`？

---

**总耗时约60分钟**，建议边看边手写笔记，重点理解内存分区的作用和异常场景！




