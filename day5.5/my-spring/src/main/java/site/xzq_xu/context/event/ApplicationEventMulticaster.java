package site.xzq_xu.context.event;

import site.xzq_xu.context.ApplicationEvent;
import site.xzq_xu.context.ApplicationListener;

/**
 * ApplicationContext事件发布者
 */
public interface ApplicationEventMulticaster {

    void addApplicationListener(ApplicationListener<?> listener);
    void removeApplicationListener(ApplicationListener<?> listener);

    void multicastEvent(ApplicationEvent event);

}
