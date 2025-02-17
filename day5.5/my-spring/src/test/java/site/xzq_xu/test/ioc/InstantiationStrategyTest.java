package site.xzq_xu.test.ioc;

import org.junit.jupiter.api.Test;
import site.xzq_xu.beans.factory.config.BeanDefinition;
import site.xzq_xu.beans.factory.support.ByteBuddySubclassingInstantiationStrategy;
import site.xzq_xu.beans.factory.support.CglibSubclassingInstantiationStrategy;
import site.xzq_xu.beans.factory.support.DefaultListableBeanFactory;
import site.xzq_xu.beans.factory.support.SimpleInstantiationStrategy;

public class InstantiationStrategyTest {
    @Test
    public void  testInstantiationStrategy(){

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        DefaultListableBeanFactory beanFactory2 = new DefaultListableBeanFactory();
        DefaultListableBeanFactory beanFactory3 = new DefaultListableBeanFactory();

        beanFactory.setInstantiationStrategy(new SimpleInstantiationStrategy());
        beanFactory2.setInstantiationStrategy(new CglibSubclassingInstantiationStrategy());
        beanFactory3.setInstantiationStrategy(new ByteBuddySubclassingInstantiationStrategy());

        BeanDefinition beanDefinition = new BeanDefinition(TestBean.class);


        beanFactory.registerBeanDefinition("testBean",beanDefinition);
        beanFactory2.registerBeanDefinition("testBean",beanDefinition);
        beanFactory3.registerBeanDefinition("testBean",beanDefinition);

        Object testBean = beanFactory.getBean("testBean");
        System.out.println(testBean.getClass().getName());

        Object testBean2 = beanFactory2.getBean("testBean");
        System.out.println(testBean2.getClass().getName());

        Object testBean3 = beanFactory3.getBean("testBean");
        System.out.println(testBean3.getClass().getName());


    }
}


