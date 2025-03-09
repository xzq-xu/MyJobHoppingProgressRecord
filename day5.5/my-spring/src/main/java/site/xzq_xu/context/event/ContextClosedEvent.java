package site.xzq_xu.context.event;


import site.xzq_xu.context.ApplicationContext;

/**
 * ContextClosedEvent
 */
public class ContextClosedEvent extends ApplicationContextEvent {

	public ContextClosedEvent(ApplicationContext source) {
		super(source);
	}
}
