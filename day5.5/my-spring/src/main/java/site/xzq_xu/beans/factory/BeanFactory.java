package site.xzq_xu.beans.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * Bean容器，实现注册bean和获取bean
 */
public class BeanFactory {
    private Map<String,Object> beanMap = new HashMap<String, Object>();

    public void registerBean(String name,Object bean){
        beanMap.put(name,bean);
    }

    public Object getBean(String name){
        return beanMap.get(name);
    }

}
