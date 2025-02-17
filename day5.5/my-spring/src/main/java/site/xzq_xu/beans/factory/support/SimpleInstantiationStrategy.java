package site.xzq_xu.beans.factory.support;

import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Bean实例化策略 - 使用Bean的构造方法来实例化Bean
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy {
    /**
     * 简单的bean实例化策略，根据bean的无参构造函数实例化对象
     * @param beanDefinition Bean的定义
     * @return
     * @throws BeansException
     */
    @Override
    public Object instantiate(BeanDefinition beanDefinition) throws BeansException {
        // 获取Bean的Class对象
        Class beanClass = beanDefinition.getBeanClass();
        try {
            // 获取Bean的无参构造函数
            Constructor constructor = beanClass.getDeclaredConstructor();
            // 实例化Bean
            return constructor.newInstance();
        } catch (Exception e) {
            // 抛出异常
            throw new BeansException("Failed to instantiate [" + beanClass.getName() + "]", e);
        }
    }
}
