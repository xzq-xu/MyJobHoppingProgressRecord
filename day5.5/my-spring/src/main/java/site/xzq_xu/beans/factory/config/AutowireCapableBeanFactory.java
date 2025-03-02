package site.xzq_xu.beans.factory.config;

import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.BeanFactory;

/**
 * 可自动装配的Bean工厂
 */
public interface AutowireCapableBeanFactory extends BeanFactory {
    // 定义一个接口，继承自BeanFactory，用于自动装配bean

    /**
     * 执行BeanPostProcessor的 postProcessBeforeInitialization方法
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object bean, String beanName)
            throws BeansException;


    /**
     * 执行BeanPostProcessor的 postProcessAfterInitialization方法
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object applyBeanPostProcessorsAfterInitialization(Object bean, String beanName)
            throws BeansException;

}
