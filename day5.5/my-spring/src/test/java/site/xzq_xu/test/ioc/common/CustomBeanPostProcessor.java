package site.xzq_xu.test.ioc.common;

import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.config.BeanPostProcessor;
import site.xzq_xu.test.ioc.bean.Cat;

/**
 * 自定义BeanPostProcessor
 */
public class CustomBeanPostProcessor implements BeanPostProcessor {
    // 在初始化之前对bean进行处理
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("before init " + beanName);
        // 如果bean的名称为"cat"，则设置其年龄为10，名称为"a doge cat"
        if ("cat".equals(beanName)) {
            ((Cat)bean).setAge(10);
            ((Cat)bean).setName("a doge cat");
        }
        return bean;
    }

    // 在初始化之后对bean进行处理
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("after init " + beanName);
        return bean;
    }
}
