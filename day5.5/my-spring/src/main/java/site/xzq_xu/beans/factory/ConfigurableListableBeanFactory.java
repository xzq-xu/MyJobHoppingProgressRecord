package site.xzq_xu.beans.factory;

import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.config.AutowireCapableBeanFactory;
import site.xzq_xu.beans.factory.config.BeanDefinition;
import site.xzq_xu.beans.factory.config.ConfigurableBeanFactory;

/**
 * 可配置、可获取所有Bean的BeanFactory
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

    // 定义一个接口，继承自ListableBeanFactory、AutowireCapableBeanFactory和ConfigurableBeanFactory

    /**
     *  获取指定名称的BeanDefinition
     * @param beanName Bean名称
     * @return BeanDefinition
     * @throws BeansException
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;
}
