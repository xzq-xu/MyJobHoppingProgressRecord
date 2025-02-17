# 手写一个Spring容器


## 最简单的Bean容器
定义一个简单的bean容器BeanFactory，内部包含一个map用以保存bean，只有注册bean和获取bean两个方法
> 分支名： simple-bean-factory
```java
//容器
public class BeanFactory {
	private Map<String, Object> beanMap = new HashMap<>();

	public void registerBean(String name, Object bean) {
		beanMap.put(name, bean);
	}

	public Object getBean(String name) {
		return beanMap.get(name);
	}
}
```
[测试代码](./src/test/java/site/xzq_xu/beans/factory/BeanFactoryTest.java)


## BeanDefinition和BeanDefinitionRegistry

> 分支名： bean-definition-and-bean-definition-register（创建时打错字了） 

主要增加这些类：
- BeanDefinition：bean的定义信息，包含bean的class信息，构造参数、属性值等信息，每个bean对应一个BeanDefinition的实例（单例bean）。简化版本仅包含class信息
- BeanDefinitionRegistry：注册beanDefinition的接口，定义注册BeanDefinition的方法
- SingletonBeanRegistry：定义了添加和获取单例Bean的方法
- DefaultSingletonBeanRegistry：SingletonBeanRegistry的实现类

Bean容器实现BeanDefinitionRegistry和SingletonBeanRegistry接口，向Bean容器重注册BeanDefinition之后，使用Bean时才会实例化
![img.png](img/BeanDefinition和BeanDefinitionRegistry.png)

[测试代码](./src/test/java/site/xzq_xu/test/ioc/BeanDefinitionAndBeanDefinitionRegistryTest.java)


## Bean实例化策略InstantiationStrategy

> 分支名: instantiation-strategy

目前的Bean创建是通过 AbstractAutowireCapableBeanFactory.doCreateBean的方法，
其中主要使用了 ` beanClass.getDeclaredConstructor().newInstance();`调用了无参构造器实例化Bean，
仅使用于Bean存在无参构造器的情形。
所以，针对实例化Bean的方式，抽象出一个实例化策略接口InstantiationStrategy，给出三个实现类：
- SimpleInstantiationStrategy，使用bean的构造方法来实例化
- CglibSubclassingInstantiationStrategy，使用CGLIB动态生成子类 ,在JDK9及以后的JDK中需要添加JVM参数 --add-opens java.base/java.lang=ALL-UNNAMED ，否则运行会报错
- ByteBuddySubClassingInstantiationStrategy，使用ByteBuddy动态生成子类，主要解决使用JDK9引入模块化系统以后，不允许外部模块方法反射访问


![img_1.png](img/InstantiationStrategy.png)


[测试代码](./src/test/java/site/xzq_xu/test/ioc/InstantiationStrategyTest.java)
![img.png](img/InstantiationStrategyTest运行结果.png)




## Bean的属性填充

> 分支名： populate-bean-with-property-values

目前Definition中只有Bean的class信息，这里增加Bean的属性信息，在实例化Bean之后为Bean填充属性
- PropertyValue：保存Bean的属性信息
- PropertyValues: 保存Bean的多个属性信息
- AbstractAutowireCapableBeanFactory.applyPropertyValues：填充Bean的属性

#### 思考：为什么采用PropertyValues，而不是直接使用List<PropertyValue>



[测试代码](./src/test/java/site/xzq_xu/test/ioc/PopulateBeanWithPropertyValuesTest.java)



## Bean的依赖注入，（高级版属性填充，为bean的属性填充bean）

> 分支名： inject-bean-with-dependencies

在上一部分实现了实例化Bean时对Bean的一般属性进行填充，这一部分需要实现填充Bean的属性值为另一个bean的情况
这里引入一个BeanReference，
包装对于另一个Bean的引用，实例化beanA后填充属性时，
若PropertyValue#value为BeanReference，引用beanB，则先去实例化beanB。
为了降低代码复杂度，这里不考虑循环依赖的情况
```java

 protected void applyPropertyValues(String beanName, Object bean,BeanDefinition beanDefinition) {
    try {
        //遍历beanDefinition中的属性值
        for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValues()) {
            String name = propertyValue.getName();
            Object value = propertyValue.getValue();
            if (value instanceof BeanReference beanReference) {
                //如果属性值为BeanReference，则先去实例化BeanReference所引用的Bean
                value = getBean(beanReference.getBeanName());
            }
            //通过反射设置属性值
            BeanUtil.setProperty(bean, name, value);
        }
    } catch (Exception e) {
        //抛出异常
        throw new BeansException("属性填充失败,Bean:"+beanName, e);
    }
}

```



[测试代码](./src/test/java/site/xzq_xu/test/ioc/InjectBeanWithDependenciesTest.java)







## 资源和资源加载器

> 分支名： resource-and-resource-loader 

此前对于bean的加载，都是通过硬编码的方式，这里引入资源类Resource，资源加载器ResourceLoader，为后续从配置文件加载Bean提供基础

- Resource：资源接口，定义了获取资源文件的方式
  - ClassPathResource：从类路径下加载资源
  - FileSystemResource：从文件系统加载资源
  - UrlResource：从URL加载资源
- ResourceLoader：资源加载器接口，定义了加载资源的方法
  - DefaultResourceLoader：资源加载器的默认实现，根据资源路径的前缀选择不同的资源加载方式

![img.png](img/resource-and-resource-loader.png)


[测试代码](./src/test/java/site/xzq_xu/test/ioc/ResourceAndResourceLoaderTest.java)

