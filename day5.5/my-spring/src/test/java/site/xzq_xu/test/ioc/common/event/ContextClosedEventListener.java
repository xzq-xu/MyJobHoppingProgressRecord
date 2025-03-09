package site.xzq_xu.test.ioc.common.event;


import site.xzq_xu.context.ApplicationListener;
import site.xzq_xu.context.event.ContextClosedEvent;

/**
 */
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		System.out.println("监听容器关闭事件");
		System.out.println(this.getClass().getName());
	}
}
