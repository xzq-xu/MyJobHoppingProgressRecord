package site.xzq_xu.test.ioc;

import org.junit.jupiter.api.Test;
import site.xzq_xu.beans.factory.PropertyValue;
import site.xzq_xu.beans.factory.PropertyValues;
import site.xzq_xu.beans.factory.config.BeanDefinition;
import site.xzq_xu.beans.factory.support.DefaultListableBeanFactory;
import site.xzq_xu.test.ioc.bean.Cat;

/**
 * 测试为Bean填充属性
 */
public class PopulateBeanWithPropertyValuesTest {
    @Test
    public void  testPopulateBeanWithPropertyValues(){
        // 创建一个DefaultListableBeanFactory对象
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 创建一个PropertyValues对象
        PropertyValues propertyValues = new PropertyValues();
        // 添加name属性，值为咪咪
        propertyValues.addPropertyValue(new PropertyValue("name","咪咪"));
        // 添加age属性，值为1
        propertyValues.addPropertyValue(new PropertyValue("age",1));

        // 创建一个BeanDefinition对象，传入Cat.class和propertyValues
        BeanDefinition beanDefinition = new BeanDefinition(Cat.class, propertyValues);

        // 将beanDefinition注册到beanFactory中，key为cat
        beanFactory.registerBeanDefinition("cat", beanDefinition);

        // 从beanFactory中获取cat对象
        Cat cat = (Cat) beanFactory.getBean("cat");

        // 打印cat对象
        System.out.println(cat);



    }

}
