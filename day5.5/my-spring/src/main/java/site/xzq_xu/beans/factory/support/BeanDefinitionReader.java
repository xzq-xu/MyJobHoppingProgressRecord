package site.xzq_xu.beans.factory.support;

import site.xzq_xu.beans.BeansException;
import site.xzq_xu.core.io.Resource;
import site.xzq_xu.core.io.ResourceLoader;

/**
 * 读取BeanDefinition的接口
 */
public interface BeanDefinitionReader {
    /**
     *  获取BeanDefinitionRegistry
     * @return BeanDefinitionRegistry
     */
    BeanDefinitionRegistry getRegistry();

    /**
     * 获取ResourceLoader
     * @return
     */
    ResourceLoader getResourceLoader();

    /**
     *  加载bean定义
     * @param resource 资源
     * @throws BeansException bean异常
     */
    void loadBeanDefinitions(Resource resource) throws BeansException;

    /**
     * 加载bean定义
     * @param location 文件路径
     * @throws BeansException
     */
    void loadBeanDefinitions(String location) throws BeansException;


    /**
     * 加载bean定义
     * @param locations 文件路径列表
     * @throws BeansException
     */
    void loadBeanDefinitions(String[] locations) throws BeansException;
}
