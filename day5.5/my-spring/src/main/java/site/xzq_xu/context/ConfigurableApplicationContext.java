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
}
