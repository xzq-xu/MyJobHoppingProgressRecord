package site.xzq_xu.test.ioc;

import org.junit.jupiter.api.Test;
import site.xzq_xu.beans.factory.config.BeanDefinition;
import site.xzq_xu.beans.factory.support.DefaultListableBeanFactory;

/**
 * @Description: BeanDefinition和BeanDefinitionRegistry测试类
 * @Author: XZQ
 */

public class BeanDefinitionAndBeanDefinitionRegistryTest {

    // 测试BeanFactory
    @Test
    public void  testBeanFactory(){
        // 创建一个DefaultListableBeanFactory实例
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 创建一个BeanDefinition实例，指定BeanFactoryTestService类
        BeanDefinition beanDefinition = new BeanDefinition(BeanFactoryTestService.class);

        // 将BeanDefinition注册到BeanFactory中，指定Bean的名称为testBean
        beanFactory.registerBeanDefinition("testBean",beanDefinition);

        // 从BeanFactory中获取名为testBean的Bean，并强制转换为BeanFactoryTestService类型
        BeanFactoryTestService testBean1 = (BeanFactoryTestService) beanFactory.getBean("testBean");
        // 调用testBean的test方法
        testBean1.test();

        BeanFactoryTestService testBean2 = (BeanFactoryTestService) beanFactory.getBean("testBean");
        // 断言testBean1和testBean2是同一个对象
        assert (testBean1 == testBean2);

    }

}