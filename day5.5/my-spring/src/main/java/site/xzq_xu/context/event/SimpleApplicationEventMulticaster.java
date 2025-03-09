package site.xzq_xu.context.event;

import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.BeanFactory;
import site.xzq_xu.context.ApplicationEvent;
import site.xzq_xu.context.ApplicationListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 容器事件发布者基础实现类
 */
public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {

    public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
        // 设置Bean工厂
        setBeanFactory(beanFactory);
    }

    @Override
    public void multicastEvent(ApplicationEvent event) {
        // 遍历所有的事件监听器
        for (ApplicationListener<ApplicationEvent> applicationListener : applicationListeners) {
            // 判断事件监听器是否支持该事件
            if (supportsEvent(applicationListener, event)) {
                applicationListener.onApplicationEvent(event);
            }
        }
    }

    /**
     *  判断事件监听器是否支持该事件
     * @param applicationListener 事件监听器
     * @param event 事件
     * @return 是否支持
     */
    protected boolean supportsEvent(ApplicationListener<ApplicationEvent> applicationListener, ApplicationEvent event) {
        Type type = applicationListener.getClass().getGenericInterfaces()[0];
        Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
        String className = actualTypeArgument.getTypeName();
        Class<?> eventClass;
        try {
            eventClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new BeansException("监听器 " + applicationListener + " 中的泛型类型 " + className + " 未找到",e);
        }

        return eventClass.isAssignableFrom(event.getClass());
    }


}
