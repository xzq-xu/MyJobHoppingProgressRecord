package site.xzq_xu.context;

import site.xzq_xu.beans.BeansException;

/**
 * 可配置的应用程序上下文
 */
public interface ConfigurableApplicationContext extends ApplicationContext{
    /**
     * 刷新容器
     */
    void refresh() throws BeansException;

    /**
     * 关闭容器
     */
    void close();

    /**
     * 向虚拟机注册一个钩子方法方法，在虚拟机关闭之前调用 执行关闭容器等操作
     */
    void registerShutdownHook();



}
