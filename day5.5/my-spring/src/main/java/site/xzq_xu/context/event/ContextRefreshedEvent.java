package site.xzq_xu.context.event;

import site.xzq_xu.context.ApplicationContext;
import site.xzq_xu.context.ApplicationEvent;
import site.xzq_xu.context.support.AbstractApplicationContext;

public class ContextRefreshedEvent extends ApplicationEvent {
    public ContextRefreshedEvent(ApplicationContext source) {
        super(source);
    }
}
