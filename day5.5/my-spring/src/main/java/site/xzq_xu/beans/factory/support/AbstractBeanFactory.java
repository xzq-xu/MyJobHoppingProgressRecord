package site.xzq_xu.beans.factory.support;

import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.FactoryBean;
import site.xzq_xu.beans.factory.config.BeanDefinition;
import site.xzq_xu.beans.factory.config.BeanPostProcessor;
import site.xzq_xu.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一个实现单例bean注册和获取的Bean容器
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {


    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final Map<String,Object> factoryBeanObjectCache = new HashMap<>();

    @Override
    public Object getBean(String name) throws BeansException {
        // 从单例池中获取bean
        Object sharedInstance = getSingleton(name);
        // 如果单例池中有该bean，则直接返回
        if (sharedInstance != null) {
            //处理FactoryBean的情况
            return getObjectForBeanInstance(sharedInstance,name);
        }
        // 如果单例池中没有该bean，则从bean定义中获取
        BeanDefinition beanDefinition = getBeanDefinition(name);
        // 创建该bean
        Object bean = createBean(name, beanDefinition);
        return getObjectForBeanInstance(bean,name);

    }

    /**
     * 处理FactoryBean的情况， 如果是FactoryBean，则调用其getObject方法获取bean
     * @param beanInstance
     * @param beanName
     * @return
     */
    protected Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        // 定义一个对象，初始值为beanInstance
        Object object = beanInstance;
        // 判断对象是否为FactoryBean的实例
        if (object instanceof FactoryBean<?> factoryBean){
            try {
                // 如果是单例，则从缓存中获取对象
                if (factoryBean.isSingleton()){
                    object = this.factoryBeanObjectCache.get(beanName);
                    // 如果缓存中没有，则调用FactoryBean的getObject方法获取对象，并将其放入缓存中
                    if (object == null){
                        object = factoryBean.getObject();
                        this.factoryBeanObjectCache.put(beanName,object);
                    }
                // 如果不是单例，则直接调用FactoryBean的getObject方法获取对象
                }else {
                    object = factoryBean.getObject();
                }
            }catch (Exception ex){
                // 如果获取对象时发生异常，则抛出BeansException
                throw new BeansException("FactoryBean thew exception on object["+ beanName+ "] creation",ex);
            }
        }

        // 返回获取到的对象
        return object;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T)getBean(name);
    }

    // 获取bean定义
    protected abstract BeanDefinition getBeanDefinition(String name);

    // 创建bean
    protected abstract Object createBean(String name, BeanDefinition beanDefinition);

    @Override
    // 重写addBeanPostProcessor方法
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        // 从beanPostProcessors中移除beanPostProcessor
        this.beanPostProcessors.remove(beanPostProcessor);
        // 将beanPostProcessor添加到beanPostProcessors中
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }
}
