package site.xzq_xu.context.event;

import site.xzq_xu.context.ApplicationContext;
import site.xzq_xu.context.ApplicationEvent;


/**
 * ApplicationContext事件
 */
public class ApplicationContextEvent extends ApplicationEvent {
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ApplicationContextEvent(Object source) {
        super(source);
    }


    public final ApplicationContext getApplicationContext(){
        return (ApplicationContext) getSource();
    }

}
