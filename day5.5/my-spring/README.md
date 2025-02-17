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




