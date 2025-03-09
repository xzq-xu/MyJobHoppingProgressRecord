package site.xzq_xu.test.ioc.common.event;


import site.xzq_xu.context.ApplicationContext;
import site.xzq_xu.context.event.ApplicationContextEvent;

/**
 */
public class CustomEvent extends ApplicationContextEvent {

	public CustomEvent(ApplicationContext source) {
		super(source);
	}
}
