package site.xzq_xu.beans.factory.support;

import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.config.BeanDefinition;

/**
 * Bean的实例化策略
 */
public interface InstantiationStrategy {
    /**
     * 实例化Bean
     * @param beanDefinition Bean的定义
     * @return 实例化的Bean对象
     * @throws BeansException Bean异常
     */
    Object instantiate(BeanDefinition beanDefinition) throws BeansException;
}
