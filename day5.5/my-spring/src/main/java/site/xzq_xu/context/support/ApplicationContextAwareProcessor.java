package site.xzq_xu.context.support;

import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.config.BeanPostProcessor;
import site.xzq_xu.context.ApplicationContext;
import site.xzq_xu.context.ApplicationContextAware;

/**
 * 处理ApplicationContextAware接口的BeanPostProcessor
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationContextAware applicationContextAware) {
            applicationContextAware.setApplicationContext(applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
