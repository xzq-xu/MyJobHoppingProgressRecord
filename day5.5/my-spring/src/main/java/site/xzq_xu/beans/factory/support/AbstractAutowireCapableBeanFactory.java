package site.xzq_xu.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import lombok.Getter;
import lombok.Setter;
import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.PropertyValue;
import site.xzq_xu.beans.factory.config.AutowireCapableBeanFactory;
import site.xzq_xu.beans.factory.config.BeanDefinition;
import site.xzq_xu.beans.factory.config.BeanPostProcessor;
import site.xzq_xu.beans.factory.config.BeanReference;

/**
 * 实现了自动装配功能的Bean容器
 */
@Setter
@Getter
public abstract class AbstractAutowireCapableBeanFactory extends AbstactBeanFactory
                                        implements AutowireCapableBeanFactory {


    /**
     * 实例化策略添加实例化策略
     */
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();


    @Override
    protected Object createBean(String name, BeanDefinition beanDefinition) {
        // 调用doCreateBean方法创建Bean实例
        return doCreateBean(name, beanDefinition);
    }

    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition) {

        Object bean = null;
        try {
            // 根据实例化策略实例化Bean
            bean = createBeanInstance(beanDefinition);
            //为 Bean 填充属性
            applyPropertyValues(beanName, bean,beanDefinition);
            // 初始化Bean  执行bean的初始化方法和BeanPostProcessor的前置和后置处理方法
            initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            // 抛出异常
            throw new BeansException("实例化失败", e);
        }

        // 将Bean实例添加到单例池中
        addSingleton(beanName, bean);
        return bean;
    }




    /**
     * 为Bean填充属性
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    protected void applyPropertyValues(String beanName, Object bean,BeanDefinition beanDefinition) {
        try {
            //遍历beanDefinition中的属性值
            for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValues()) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                if (value instanceof BeanReference beanReference) {
                    //如果属性值为BeanReference，则先去实例化BeanReference所引用的Bean
                    value = getBean(beanReference.getBeanName());
                }
                //通过反射设置属性值
                BeanUtil.setProperty(bean, name, value);
            }
        } catch (Exception e) {
            //抛出异常
            throw new BeansException("属性填充失败,Bean:"+beanName, e);
        }
    }

    /**
     * 从BeanDefinition使用InstantiationStrategy创建Bean实例
     * @param beanDefinition
     * @return
     */
    private  Object createBeanInstance(BeanDefinition beanDefinition)  {
        return getInstantiationStrategy().instantiate(beanDefinition);
    }


    @Override
    // 执行BeanPostProcessor的 postProcessBeforeInitialization方法
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        // 将传入的existingBean赋值给result
        Object result = existingBean;
        // 遍历所有的Bean后处理器
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            // 调用postProcessBeforeInitialization方法，对result进行处理
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            // 如果current为null，则返回result
            if (current == null) {
                return result;
            }
            // 否则将current赋值给result
            result = current;
        }
        // 返回最终的result
        return result;
    }

    //执行BeanPostProcessor的 postProcessAfterInitialization方法
    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        //将existingBean赋值给result
        Object result = existingBean;
        //遍历所有的BeanPostProcessor
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            //调用postProcessAfterInitialization方法，将result和beanName作为参数传入
            Object current = processor.postProcessAfterInitialization(result, beanName);
            //如果current为null，则返回result
            if (current == null){
                return result;
            }
            //将current赋值给result
            result = current;
        }
        //返回result
        return  result;
    }

    /**
     * 初始化bean
     * @param beanName
     * @param bean
     * @param beanDefinition
     * @return
     */
    protected Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        //执行BeanPostProcessor的前置处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        //TODO 后面会在此处执行bean的初始化方法
        invokeInitMethods(beanName, wrappedBean, beanDefinition);

        //执行BeanPostProcessor的后置处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        return wrappedBean;
    }


    /**
     * 执行bean的初始化方法
     *
     * @param beanName
     * @param bean
     * @param beanDefinition
     * @throws Throwable
     */
    protected void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) {
        //TODO 后面会实现
        System.out.println("执行bean[" + beanName + "]的初始化方法");
    }

}
