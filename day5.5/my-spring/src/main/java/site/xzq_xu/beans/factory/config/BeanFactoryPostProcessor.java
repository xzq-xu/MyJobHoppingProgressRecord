package site.xzq_xu.beans.factory.config;

import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.ConfigurableListableBeanFactory;

/**
 * 定义允许修改BeanDefinition的接口
 */
public interface BeanFactoryPostProcessor {

    /**
     * 在BeanDefinition加载完成后，但在bean实例化之前，提供修改BeanDefinition属性值的机制
     * @param beanFactory
     * @throws BeansException
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;

}
