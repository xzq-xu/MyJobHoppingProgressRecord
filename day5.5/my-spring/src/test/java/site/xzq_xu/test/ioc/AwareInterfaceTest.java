package site.xzq_xu.test.ioc;

import org.junit.jupiter.api.Test;
import site.xzq_xu.context.support.ClassPathJsonApplicationContext;
import site.xzq_xu.context.support.ClassPathXmlApplicationContext;
import site.xzq_xu.test.ioc.service.AwareTestService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.not;

/**
 * 测试ApplicationContext
 */
public class AwareInterfaceTest {


    @Test
    public void  testXmlApplication(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();

        AwareTestService awareTestService = applicationContext.getBean("awareTestService", AwareTestService.class);

        assertThat(awareTestService.getApplicationContext()).isNotNull();
        assertThat(awareTestService.getBeanFactory()).isNotNull();


//        applicationContext.close();
    }




}