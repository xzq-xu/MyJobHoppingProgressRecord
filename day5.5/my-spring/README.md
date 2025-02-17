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



