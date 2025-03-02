package site.xzq_xu.context;

import site.xzq_xu.beans.factory.HierarchicalBeanFactory;
import site.xzq_xu.beans.factory.ListableBeanFactory;
import site.xzq_xu.core.io.ResourceLoader;

/**
 * 应用程序上下文
 */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader {
    // 实现ListableBeanFactory接口，可以获取容器中所有的BeanDefinition
    // 实现HierarchicalBeanFactory接口，可以获取父容器
    // 实现ResourceLoader接口，可以加载资源文件


}
