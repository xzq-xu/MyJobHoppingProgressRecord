package site.xzq_xu.test.ioc;

import org.junit.jupiter.api.Test;
import site.xzq_xu.beans.PropertyValue;
import site.xzq_xu.beans.PropertyValues;
import site.xzq_xu.beans.factory.config.BeanDefinition;
import site.xzq_xu.beans.factory.config.BeanReference;
import site.xzq_xu.beans.factory.support.DefaultListableBeanFactory;
import site.xzq_xu.test.ioc.bean.Cat;
import site.xzq_xu.test.ioc.bean.Person;

/**
 * 关于依赖注入Bean的测试
 */
public class InjectBeanWithDependenciesTest {

    @Test
    public void testInjectBeanWithDependencies(){
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        PropertyValues propertyValuesCat = new PropertyValues();
        propertyValuesCat.addPropertyValue(new PropertyValue("name", "小花"));
        propertyValuesCat.addPropertyValue(new PropertyValue("age", 3));

        PropertyValues propertyValuesPerson = new PropertyValues();
        propertyValuesPerson.addPropertyValue(new PropertyValue("name", "张三"));
        propertyValuesPerson.addPropertyValue(new PropertyValue("age", 18));
        propertyValuesPerson.addPropertyValue(new PropertyValue("cat", new BeanReference("cat")));

        BeanDefinition beanDefinitionPerson = new BeanDefinition(Person.class, propertyValuesPerson);
        BeanDefinition beanDefinitionCat = new BeanDefinition(Cat.class, propertyValuesCat);


        beanFactory.registerBeanDefinition("cat", beanDefinitionCat);
        beanFactory.registerBeanDefinition("person", beanDefinitionPerson);


        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);


        Cat cat =(Cat) beanFactory.getBean("cat");

        System.out.println(person.getCat() == cat);

    }
}
