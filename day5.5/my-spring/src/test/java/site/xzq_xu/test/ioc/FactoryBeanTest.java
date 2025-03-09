package site.xzq_xu.test.ioc;

import org.junit.jupiter.api.Test;
import site.xzq_xu.context.support.ClassPathXmlApplicationContext;
import site.xzq_xu.test.ioc.bean.Cat;
import site.xzq_xu.test.ioc.bean.Person;
import site.xzq_xu.test.ioc.service.AwareTestService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * 测试ApplicationContext
 */
public class FactoryBeanTest {


    @Test
    public void  testXmlApplication(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:factory-bean.xml");
        applicationContext.registerShutdownHook();

        Cat cat = applicationContext.getBean("cat", Cat.class);
        Cat cat2 = applicationContext.getBean("cat", Cat.class);

        assertThat(cat.getName()).isEqualTo("TomFC");
        assertThat(cat).isEqualTo(cat2);

//        applicationContext.close();
    }

    @Test
    public void  testXmlApplication2(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:factory-bean.xml");
        applicationContext.registerShutdownHook();

        Person person = applicationContext.getBean("person", Person.class);
        Person person2 = applicationContext.getBean("person", Person.class);

        assertThat(person.getName()).isEqualTo("LisiFC");
        assertThat(person).isNotEqualTo(person2);

//        applicationContext.close();
    }



}