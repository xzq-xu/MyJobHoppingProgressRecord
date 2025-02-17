package site.xzq_xu.beans.factory.support;

import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.config.BeanDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认的可以列举的Bean容器，实现了BeanDefinitionRegistry接口和 SingletonBeanRegistry接口
 * 可以注册、获取BeanDefinition 也可以注册和获取SingletonBean
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements  BeanDefinitionRegistry{


    // 定义一个Map，用于存储BeanDefinition
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    // 根据Bean名称获取BeanDefinition
    @Override
    protected BeanDefinition getBeanDefinition(String name) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        if (beanDefinition == null) {
            throw new BeansException("No bean named '" + name + "' is defined");
        }
        return beanDefinition;
    }

    // 注册BeanDefinition
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }
}
