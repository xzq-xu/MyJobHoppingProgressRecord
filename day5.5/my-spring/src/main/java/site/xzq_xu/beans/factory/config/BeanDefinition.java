package site.xzq_xu.beans.factory.config;

import lombok.Getter;
import lombok.Setter;
import site.xzq_xu.beans.PropertyValues;

/**
 * BeanDefinition实例保存bean的信息，包括class类型、方法构造参数、是否为单例等.
 * 添加Bean的属性信息
 * 使用lombok简化代码
 */
@Getter
@Setter
public class BeanDefinition {
    private Class beanClass;

    private PropertyValues propertyValues;

    public BeanDefinition(Class beanClass) {
        this(beanClass,null);
    }

    public BeanDefinition(Class beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues != null?propertyValues:new PropertyValues();
    }


}
