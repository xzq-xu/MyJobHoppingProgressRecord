package site.xzq_xu.test.ioc;

import org.junit.jupiter.api.Test;
import site.xzq_xu.context.support.ClassPathXmlApplicationContext;
import site.xzq_xu.test.ioc.common.event.CustomEvent;

public class EventAndEventListenerTest {

    @Test
    public void testEventListener() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:event-and-event-listener.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext));

        applicationContext.registerShutdownHook();//或者applicationContext.close()主动关闭容器;
    }

}
