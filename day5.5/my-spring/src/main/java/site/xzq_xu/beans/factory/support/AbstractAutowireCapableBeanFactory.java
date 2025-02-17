package site.xzq_xu.beans.factory.support;

import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.config.BeanDefinition;

import java.lang.reflect.InvocationTargetException;

/**
 * 实现了自动装配功能的Bean容器
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstactBeanFactory {


    /**
     * 实例化策略添加实例化策略
     */
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();


    @Override
    protected Object createBean(String name, BeanDefinition beanDefinition) {
        // 调用doCreateBean方法创建Bean实例
        return doCreateBean(name, beanDefinition);
    }

    protected Object doCreateBean(String name, BeanDefinition beanDefinition) {

        Object bean = null;
        try {
            // 根据实例化策略实例化Bean
            bean = createBeanInstance(beanDefinition);
        } catch (Exception e) {
            // 抛出异常
            throw new BeansException("实例化失败", e);
        }

        // 将Bean实例添加到单例池中
        addSingleton(name, bean);
        return bean;
    }

    /**
     * 从BeanDefinition使用InstantiationStrategy创建Bean实例
     * @param beanDefinition
     * @return
     */
    private  Object createBeanInstance(BeanDefinition beanDefinition)  {
        return instantiationStrategy.instantiate(beanDefinition);
    }


    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }



}
