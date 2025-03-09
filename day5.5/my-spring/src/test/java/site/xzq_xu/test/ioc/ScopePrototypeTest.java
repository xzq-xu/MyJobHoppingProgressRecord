package site.xzq_xu.test.ioc;

import org.junit.jupiter.api.Test;
import site.xzq_xu.context.support.ClassPathXmlApplicationContext;
import site.xzq_xu.test.ioc.service.AwareTestService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * 测试ApplicationContext
 */
public class ScopePrototypeTest {


    @Test
    public void  testXmlApplication(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();

        AwareTestService awareTestService = applicationContext.getBean("awareTestService2", AwareTestService.class);
        AwareTestService awareTestService2 = applicationContext.getBean("awareTestService2", AwareTestService.class);

        assertThat(awareTestService).isNotEqualTo(awareTestService2);


//        applicationContext.close();
    }




}