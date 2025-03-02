package site.xzq_xu.test.ioc;

import org.junit.jupiter.api.Test;
import site.xzq_xu.beans.factory.support.DefaultListableBeanFactory;
import site.xzq_xu.beans.factory.xml.XmlBeanDefinitionReader;
import site.xzq_xu.test.ioc.bean.Cat;
import site.xzq_xu.test.ioc.bean.Person;
import site.xzq_xu.test.ioc.common.CustomBeanFactoryPostProcessor;
import site.xzq_xu.test.ioc.common.CustomBeanPostProcessor;

import static org.assertj.core.api.AssertionsForClassTypes.*;

/**
 * 测试 BeanFactoryPostProcessor 和 BeanPostProcessor
 */
public class BeanFactoryPostProcessorAndBeanPostProcessorTest {


    @Test
    public void testBeanFactoryPostProcessor() {
        //创建一个DefaultListableBeanFactory对象
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //创建一个XmlBeanDefinitionReader对象，用于加载spring.xml文件中的BeanDefinition
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        //加载spring.xml文件中的BeanDefinition
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        //在所有BeanDefinition加载完成后，但在bean实例化之前，修改BeanDefinition的属性值
        //创建一个CustomBeanFactoryPostProcessor对象
        CustomBeanFactoryPostProcessor beanFactoryPostProcessor = new CustomBeanFactoryPostProcessor();
        //调用postProcessBeanFactory方法，修改BeanDefinition的属性值
        beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);

        //从beanFactory中获取名为person的bean
        Person person = (Person) beanFactory.getBean("person");
        //打印person对象
        System.out.println(person);
        //name属性在CustomBeanFactoryPostProcessor中被修改为newName
        assertThat(person.getName()).isEqualTo("newName");
        assertThat(person.getAge()).isEqualTo(99);
    }


    @Test
    public void testBeanPostProcessor() {
        //创建一个DefaultListableBeanFactory对象
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //创建一个XmlBeanDefinitionReader对象，用于加载spring.xml文件中的BeanDefinition
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        //加载spring.xml文件中的BeanDefinition
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        //添加自定义的beanPostProcessor
        CustomBeanPostProcessor customBeanPostProcessor = new CustomBeanPostProcessor();
        beanFactory.addBeanPostProcessor(customBeanPostProcessor);

        //从beanFactory中获取名为person的bean
        Cat cat = (Cat) beanFactory.getBean("cat");
        //打印person对象
        System.out.println(cat);
        //name属性在CustomBeanFactoryPostProcessor中被修改为newName
        assertThat(cat.getName()).isEqualTo("a doge cat");
        assertThat(cat.getAge()).isEqualTo(10);

    }



}
