package site.xzq_xu.test.ioc.common.event;


import site.xzq_xu.context.ApplicationListener;
import site.xzq_xu.context.event.ContextRefreshedEvent;

/**
 */
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		System.out.println("监听容器刷新事件");
		System.out.println(this.getClass().getName());
	}
}
