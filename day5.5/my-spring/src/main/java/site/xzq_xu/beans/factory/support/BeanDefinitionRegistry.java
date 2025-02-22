package site.xzq_xu.beans.factory.support;

import site.xzq_xu.beans.BeansException;
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

    /**
     *  根据beanName获取BeanDefinition
     * @param beanName bean名称
     * @return BeanDefinition
     * @throws BeansException
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;


    /**
     *  判断容器中是否包含指定名称的Bean定义
     * @param beanName 要判断的Bean名称
     * @return 如果容器中包含指定名称的Bean定义，则返回true，否则返回false
     */
    boolean containsBeanDefinition(String beanName);

    /**
     *  获取bean定义名称数组
     * @return bean定义名称数组
     */
    String[] getBeanDefinitionNames();


}
