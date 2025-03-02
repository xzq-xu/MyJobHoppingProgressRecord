package site.xzq_xu.test.ioc.common;

import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.PropertyValue;
import site.xzq_xu.beans.PropertyValues;
import site.xzq_xu.beans.factory.ConfigurableListableBeanFactory;
import site.xzq_xu.beans.factory.config.BeanDefinition;
import site.xzq_xu.beans.factory.config.BeanFactoryPostProcessor;

/**
 * 自定义的BeanFactoryPostProcessor，用修改person的属性
 */
public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // do something
        // 获取person的BeanDefinition
        BeanDefinition personBeanDefinition = beanFactory.getBeanDefinition("person");
        // 获取person的属性值
        PropertyValues propertyValues = personBeanDefinition.getPropertyValues();
        // 修改person的name属性
        propertyValues.addPropertyValue(new PropertyValue("name", "newName"));
        // 修改person的age属性
        propertyValues.addPropertyValue(new PropertyValue("age","99"));
    }
}
