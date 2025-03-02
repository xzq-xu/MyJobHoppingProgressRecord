package site.xzq_xu.beans.factory;

import site.xzq_xu.beans.BeansException;

import java.util.Map;

/**
 * 可以获取所有Bean的BeanFactory
 */
public interface ListableBeanFactory extends BeanFactory{
    /**
     *  根据类型获取所有Bean的Map
     * @param type
     * @return
     * @throws BeansException
     */
     <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

     /**
      * 获取定义的所有Bean的名称
      * @return
      */
     String[] getBeanDefinitionNames();
}
