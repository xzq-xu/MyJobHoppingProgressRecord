package site.xzq_xu.beans.factory.config;

/**
 * 单例Bean注册表
 */
public interface SingletonBeanRegistry {
    /**
     * 获取单例bean
     * @param beanName
     * @return
     */
    Object getSingleton(String beanName);

    /**
     * 添加到单例池
     * @param beanName
     * @param singletonObject
     */
    void addSingleton(String beanName, Object singletonObject);

}
