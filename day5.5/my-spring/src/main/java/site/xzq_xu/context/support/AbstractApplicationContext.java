package site.xzq_xu.context.support;

import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.ConfigurableListableBeanFactory;
import site.xzq_xu.beans.factory.config.BeanFactoryPostProcessor;
import site.xzq_xu.beans.factory.config.BeanPostProcessor;
import site.xzq_xu.context.ApplicationEvent;
import site.xzq_xu.context.ApplicationListener;
import site.xzq_xu.context.ConfigurableApplicationContext;
import site.xzq_xu.context.event.ApplicationEventMulticaster;
import site.xzq_xu.context.event.ContextClosedEvent;
import site.xzq_xu.context.event.ContextRefreshedEvent;
import site.xzq_xu.context.event.SimpleApplicationEventMulticaster;
import site.xzq_xu.core.io.DefaultResourceLoader;
import site.xzq_xu.core.io.Resource;

import java.util.Collection;
import java.util.Map;


/**
 * 抽象应用程序上下文
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";
    private ApplicationEventMulticaster applicationEventMulticaster;



    /**
     *重写父类的方法 // 刷新容器
      */
    @Override
    public void refresh() throws BeansException {
        //1. 创建BeanFactory，加载Bean定义信息
        refreshBeanFactory();
        // 获取beanFactory
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        //添加ApplicationContextAwareProcessor ， 让实现了自ApplicationContextAware的bean能够感知到applicationContext
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));


        //2. 在Bean实例化之前执行BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);

        //3. 注册BeanPostProcessor
        registerBeanPostProcessors(beanFactory);

        //初始化事件发布者
        initApplicationEventMulticaster();
        //注册事件监听器
        registerListeners();

        //提前实例化单例bean
        beanFactory.preInstantiateSingletons();

        //发布容器刷新完成事件
        finishRefresh();
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


    @Override
    // 重写close()方法
    public void close() {
        // 调用doClose()方法
        doClose();
    }

    // 关闭方法
    protected void doClose() {
        //发布容器关闭事件
        publishEvent(new ContextClosedEvent(this));
        // 销毁bean
        destroyBeans();
    }

    // 销毁bean
    protected void destroyBeans() {
        // 调用bean工厂的销毁单例方法
        getBeanFactory().destroySingletons();
    }

    @Override
    // 重写registerShutdownHook方法
    public void registerShutdownHook() {
        // 创建一个线程，线程执行doClose方法
        Thread shutdownHook = new Thread(this::doClose);
        // 将线程添加到JVM的关闭钩子中
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }


    /**
     * 初始化事件发布者
     */
    protected void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.addSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
    }

    /**
     * 注册事件监听器
     */
    protected void registerListeners() {
        Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener applicationListener : applicationListeners) {
            applicationEventMulticaster.addApplicationListener(applicationListener);
        }
    }

    /**
     * 发布容器刷新完成事件
     */
    protected void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }
}
