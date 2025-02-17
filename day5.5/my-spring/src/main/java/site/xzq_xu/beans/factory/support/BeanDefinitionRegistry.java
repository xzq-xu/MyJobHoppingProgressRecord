package site.xzq_xu.beans.factory.support;

import site.xzq_xu.beans.factory.config.BeanDefinition;

/**
 * BeanDefinition注册表
 */
public interface BeanDefinitionRegistry {
    /**
     *  注册BeanDefinition
     * @param beanName
     * @param beanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
