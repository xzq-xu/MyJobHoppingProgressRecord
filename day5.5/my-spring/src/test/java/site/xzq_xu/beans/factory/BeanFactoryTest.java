package site.xzq_xu.beans.factory;


import org.junit.jupiter.api.Test;

public class BeanFactoryTest {
    @Test
    public void testGetBean() throws Exception {
        BeanFactory beanFactory = new BeanFactory();
        HelloService bean = new HelloService();
        beanFactory.registerBean("helloService", bean);
        HelloService helloService = (HelloService) beanFactory.getBean("helloService");
        assert (helloService != null);
        assert (helloService.sayHello()).equals("hello");
        assert (helloService == bean);
    }

    class HelloService {
        public String sayHello() {
            System.out.println("hello");
            return "hello";
        }
    }
}