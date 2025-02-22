package site.xzq_xu.test.ioc;

import org.junit.jupiter.api.Test;
import site.xzq_xu.beans.factory.json.JsonBeanDefinitionReader;
import site.xzq_xu.beans.factory.support.DefaultListableBeanFactory;
import site.xzq_xu.beans.factory.xml.XmlBeanDefinitionReader;
import site.xzq_xu.test.ioc.bean.Cat;
import site.xzq_xu.test.ioc.bean.Person;

public class FIleBeanDefinitionTest {


    @Test
    public void  testXmlFile(){
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);

        Cat cat = (Cat) beanFactory.getBean("cat");
        System.out.println(cat);

        System.out.println(person.getCat() == cat);

    }


    @Test
    public void testJsonFile(){
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        JsonBeanDefinitionReader beanDefinitionReader = new JsonBeanDefinitionReader(beanFactory);
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.json");

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);

        Cat cat = (Cat) beanFactory.getBean("cat");
        System.out.println(cat);

        System.out.println(person.getCat() == cat);

    }


}
