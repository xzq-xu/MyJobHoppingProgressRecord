package site.xzq_xu.beans.factory.support;

import site.xzq_xu.beans.factory.BeanFactory;
import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.config.BeanDefinition;

/**
 * 一个实现单例bean注册和获取的Bean容器
 */
public abstract class AbstactBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {
    @Override
    public Object getBean(String name) throws BeansException {
        // 从单例池中获取bean
        Object bean = getSingleton(name);
        // 如果单例池中有该bean，则直接返回
        if (bean != null) {
            return bean;
        }
        // 如果单例池中没有该bean，则从bean定义中获取
        BeanDefinition beanDefinition = getBeanDefinition(name);
        // 创建该bean
        return createBean(name, beanDefinition);

    }

    // 获取bean定义
    protected abstract BeanDefinition getBeanDefinition(String name);

    // 创建bean
    protected abstract Object createBean(String name, BeanDefinition beanDefinition);
}
