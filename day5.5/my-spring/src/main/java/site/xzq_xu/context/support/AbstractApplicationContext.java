package site.xzq_xu.context.support;

import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.ConfigurableListableBeanFactory;
import site.xzq_xu.beans.factory.config.BeanFactoryPostProcessor;
import site.xzq_xu.beans.factory.config.BeanPostProcessor;
import site.xzq_xu.context.ConfigurableApplicationContext;
import site.xzq_xu.core.io.DefaultResourceLoader;
import site.xzq_xu.core.io.Resource;

import java.util.Map;


/**
 * 抽象应用程序上下文
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {
    /**
     *重写父类的方法 // 刷新容器
      */
    @Override
    public void refresh() throws BeansException {
        //1. 创建BeanFactory，加载Bean定义信息
        refreshBeanFactory();
        // 获取beanFactory
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        //2. 在Bean实例化之前执行BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);

        //3. 注册BeanPostProcessor
        registerBeanPostProcessors(beanFactory);

        //提前实例化单例bean
        beanFactory.preInstantiateSingletons();

    }

    /**
     * 注册BeanPostProcessor
     * @param beanFactory
     */
    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        // 获取所有实现了BeanPostProcessor接口的bean
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        // 遍历所有实现了BeanPostProcessor接口的bean
        beanPostProcessorMap.forEach((beanPostProcessorName, beanPostProcessor) -> {
            // 将每个实现了BeanPostProcessor接口的bean添加到beanFactory中
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        });
    }

    /**
     * 处理BeanFactoryPostProcessor
     * @param beanFactory
     */
    protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        // 获取beanFactory中所有类型为BeanFactoryPostProcessor的bean
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);

        // 遍历beanFactoryPostProcessorMap，调用每个BeanFactoryPostProcessor的postProcessBeanFactory方法
        beanFactoryPostProcessorMap.forEach((beanFactoryPostProcessorName, beanFactoryPostProcessor) -> {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        });
    }

    /**
     * 创建BeanFactory，并加载BeanDefinition
     * @throws BeansException
     */
    protected abstract void refreshBeanFactory() throws BeansException;

    /**
     * 重写父类方法，获取指定名称和类型的bean
     */
    @Override
    public  <T> T getBean(String name,Class<T> clazz) throws BeansException {
        // 调用父类方法，获取指定名称和类型的bean
        return getBeanFactory().getBean(name, clazz);
    }

    /**
     * 重写父类方法，获取指定类型的所有bean
     */
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        // 调用父类方法，获取指定类型的所有bean
        return getBeanFactory().getBeansOfType(type);
    }

    /**
     *  返回一个可配置的bean工厂
     * @return ConfigurableListableBeanFactory
     */
    public abstract ConfigurableListableBeanFactory getBeanFactory();


    /**
     * 根据beanName获取bean对象
    */
    public Object getBean(String beanName){
        // 调用getBeanFactory()方法获取bean工厂
        return getBeanFactory().getBean(beanName);
    }


    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

}
