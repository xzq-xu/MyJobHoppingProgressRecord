package site.xzq_xu.beans.factory.support;

import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.ConfigurableListableBeanFactory;
import site.xzq_xu.beans.factory.config.BeanDefinition;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 默认的可以列举的Bean容器，实现了BeanDefinitionRegistry接口和 SingletonBeanRegistry接口
 * 可以注册、获取BeanDefinition 也可以注册和获取SingletonBean
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
        implements ConfigurableListableBeanFactory,BeanDefinitionRegistry{

    // 定义一个Map，用于存储BeanDefinition
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    // 根据Bean名称获取BeanDefinition
    @Override
    public BeanDefinition getBeanDefinition(String name) {
        // 从beanDefinitionMap中获取指定名称的BeanDefinition
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        // 如果获取不到，则抛出异常
        if (beanDefinition == null) {
            throw new BeansException("No bean named '" + name + "' is defined");
        }
        // 返回获取到的BeanDefinition
        return beanDefinition;
    }



    // 注册BeanDefinition
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        // 将beanName和beanDefinition放入beanDefinitionMap中
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    // 重写父类方法，判断beanDefinitionMap中是否包含指定名称的bean定义
    public boolean containsBeanDefinition(String beanName) {
        // 返回beanDefinitionMap中是否包含指定名称的bean定义
        return beanDefinitionMap.containsKey(beanName);
    }


    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        // 创建一个Map，用于存储符合条件的bean
        Map<String, T> result = new HashMap<>();
        // 遍历beanDefinitionMap，获取每个bean的名称和定义
        beanDefinitionMap.forEach((beanName,beanDefinition)->{
            // 获取bean的Class对象
            Class beanClass = beanDefinition.getBeanClass();
            // 判断bean的Class对象是否是type的子类或实现类
            if (type.isAssignableFrom(beanClass)) {
                // 如果是，则通过getBean方法获取bean实例
                T bean = (T) getBean(beanName);
                // 将bean实例放入result中
                result.put(beanName,bean);
            }
        });
        // 返回符合条件的bean
        return result;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        Set<String> beanNames = beanDefinitionMap.keySet();
        return beanNames.toArray(new String[0]);
    }

    @Override
    public void preInstantiateSingletons() throws BeansException {
        beanDefinitionMap.keySet().forEach(this::getBean);
    }
}
