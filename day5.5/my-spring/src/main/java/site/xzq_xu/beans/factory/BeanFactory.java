package site.xzq_xu.beans.factory;

import site.xzq_xu.beans.BeansException;

import java.util.HashMap;
import java.util.Map;

/**
 * Bean容器，实现注册bean和获取bean
 */
public interface BeanFactory {


    /**
     * 获取bean ,如果不存在将抛出BeansException异常
     * @param name
     * @return
     * @throws BeansException
     */
    public Object getBean(String name) throws BeansException;

}
