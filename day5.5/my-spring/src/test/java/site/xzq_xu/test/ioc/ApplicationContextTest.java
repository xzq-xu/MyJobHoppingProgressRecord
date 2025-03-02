package site.xzq_xu.test.ioc;

import org.junit.jupiter.api.Test;
import site.xzq_xu.context.ApplicationContext;
import site.xzq_xu.context.support.ClassPathJsonApplicationContext;
import site.xzq_xu.context.support.ClassPathXmlApplicationContext;
import site.xzq_xu.test.ioc.bean.Cat;
import site.xzq_xu.test.ioc.bean.Person;
import static org.assertj.core.api.AssertionsForClassTypes.*;
/**
 * 测试ApplicationContext
 */
public class ApplicationContextTest {

    @Test
    public void  testJsonApplication(){
        ApplicationContext applicationContext = new ClassPathJsonApplicationContext("classpath:spring.json");
        Person person = (Person)applicationContext.getBean("person");

        Cat cat = applicationContext.getBean("cat", Cat.class);
        assertThat(cat.getName()).isEqualTo("Tom");

        assertThat(person.getName()).isEqualTo("张三");

    }

    @Test
    public void  testJsonApplicationWithBeanPostProcessorAndBeanFactoryPostProcessor(){
        ApplicationContext applicationContext = new ClassPathJsonApplicationContext("classpath:" +
                "springWithBeanPostProcessorAndBeanFactoryPostProcessor.json");
        Person person = (Person)applicationContext.getBean("person");
        Cat cat = applicationContext.getBean("cat", Cat.class);
        assertThat(person.getName()).isEqualTo("newName");
        assertThat(cat.getName()).isEqualTo("a doge cat");
    }


    @Test
    public void  testXmlApplicationContext(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        Person person = (Person)applicationContext.getBean("person");

        Cat cat = applicationContext.getBean("cat", Cat.class);
        assertThat(cat.getName()).isEqualTo("Jerry");
        assertThat(person.getName()).isEqualTo("李四");

    }

    @Test
    public void  testXmlApplicationContextWithBeanPostProcessorAndBeanFactoryPostProcessor(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:" +
                "springWithBeanPostProcessorAndBeanFactoryPostProcessor.xml");
        Person person = (Person)applicationContext.getBean("person");
        Cat cat = applicationContext.getBean("cat", Cat.class);
        assertThat(person.getName()).isEqualTo("newName");
        assertThat(cat.getName()).isEqualTo("a doge cat");
    }



}