package site.xzq_xu.beans.factory.support;

import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.config.BeanDefinition;

import java.lang.reflect.InvocationTargetException;

/**
 * 实现了自动装配功能的Bean容器
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstactBeanFactory {


    @Override
    protected Object createBean(String name, BeanDefinition beanDefinition) {
        // 调用doCreateBean方法创建Bean实例
        return doCreateBean(name, beanDefinition);
    }

    protected Object doCreateBean(String name, BeanDefinition beanDefinition) {

        // 获取Bean的Class对象
        Class beanClass = beanDefinition.getBeanClass();
        Object bean = null;
        try {
            // 通过反射创建Bean实例
            bean = beanClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            // 抛出异常
            throw new BeansException("实例化失败", e);
        }

        // 将Bean实例添加到单例池中
        addSingleton(name, bean);
        return bean;
    }


}
