package site.xzq_xu.beans.factory.support;

import site.xzq_xu.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认的单例bean注册表
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    // 定义一个Map，用于存储beanName和bean的对应关系
    private Map<String,Object> stringObjectMap = new HashMap<>();

    @Override
    // 重写父类方法，获取指定名称的bean
    public Object getSingleton(String beanName) {
        // 从stringObjectMap中获取指定名称的bean
        return stringObjectMap.get(beanName);
    }


    protected  void  addSingleton(String beanName,Object bean){
        // 将beanName和bean放入stringObjectMap中
        stringObjectMap.put(beanName,bean);
    }

}
