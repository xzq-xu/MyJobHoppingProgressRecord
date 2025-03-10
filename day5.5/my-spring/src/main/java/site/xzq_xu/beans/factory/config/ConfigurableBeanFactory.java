package site.xzq_xu.beans.factory.config;

import site.xzq_xu.beans.factory.HierarchicalBeanFactory;

/**
 * 可配置的Bean工厂
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory,SingletonBeanRegistry {
    // 可配置的Bean工厂接口，继承自HierarchicalBeanFactory和SingletonBeanRegistry接口

    /**
     * 添加 BeanPostProcessor
     * @param beanPostProcessor
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /**
     * 销毁所有单例bean
     */
    void destroySingletons();


}
