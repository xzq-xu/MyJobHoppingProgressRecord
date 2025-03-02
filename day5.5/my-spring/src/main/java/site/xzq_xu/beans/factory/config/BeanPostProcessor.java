package site.xzq_xu.beans.factory.config;

import site.xzq_xu.beans.BeansException;

/**
 * 定义用于修改实例化后bean的扩展点
 */
public interface BeanPostProcessor {

    /**
     * 在bean初始化之前调用
     * @param bean
     * @param beanName
     * @return
     */
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    /**
     * 在bean初始化之后调用
     * @param bean
     * @param beanName
     * @return
     */
    Object postProcessAfterInitialization(Object bean, String beanName) throws  BeansException;
}
