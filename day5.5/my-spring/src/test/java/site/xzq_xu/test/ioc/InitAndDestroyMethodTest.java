package site.xzq_xu.test.ioc;

import org.junit.jupiter.api.Test;
import site.xzq_xu.context.support.ClassPathJsonApplicationContext;
import site.xzq_xu.context.support.ClassPathXmlApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * 测试ApplicationContext
 */
public class InitAndDestroyMethodTest {

    @Test
    public void  testJsonApplication(){
        ClassPathJsonApplicationContext applicationContext = new ClassPathJsonApplicationContext("classpath:init-and-destroy-method.json");

        applicationContext.close();
//        applicationContext.registerShutdownHook();

    }

    @Test
    public void  testXmlApplication(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:init-and-destroy-method.xml");
        applicationContext.registerShutdownHook();
//        applicationContext.close();
    }




}