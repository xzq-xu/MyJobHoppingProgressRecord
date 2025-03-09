package site.xzq_xu.beans.factory.support;

import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.DisposableBean;
import site.xzq_xu.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 默认的单例bean注册表
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    // 定义一个Map，用于存储beanName和bean的对应关系
    private Map<String,Object> stringObjects = new HashMap<>();

    private final Map<String, DisposableBean>  disposableBeans = new HashMap<>();


    @Override
    // 重写父类方法，获取指定名称的bean
    public Object getSingleton(String beanName) {
        // 从stringObjectMap中获取指定名称的bean
        return stringObjects.get(beanName);
    }


    protected  void  addSingleton(String beanName,Object bean){
        // 将beanName和bean放入stringObjectMap中
        stringObjects.put(beanName,bean);
    }


    public void registerDisposableBean(String beanName,DisposableBean bean){
        // 将beanName和bean放入disposableBeans中
        disposableBeans.put(beanName,bean);
    }

    public void destroySingletons(){
        Set<String> beanNames = disposableBeans.keySet();
        for (String beanName : beanNames) {
            DisposableBean disposableBean = disposableBeans.get(beanName);
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeansException("destroy bean error with BeanName: " + beanName,e);
            }
        }
    }

}
