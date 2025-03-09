package site.xzq_xu.test.ioc.common.event;


import site.xzq_xu.context.ApplicationListener;

/**
 * @author derekyi
 * @date 2020/12/5
 */
public class CustomEventListener implements ApplicationListener<CustomEvent> {

	@Override
	public void onApplicationEvent(CustomEvent event) {
		System.out.println("监听自定义事件");
		System.out.println(this.getClass().getName());
	}
}
