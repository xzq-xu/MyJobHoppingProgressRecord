package site.xzq_xu.context.support;

import site.xzq_xu.beans.factory.support.BeanDefinitionReader;
import site.xzq_xu.beans.factory.support.DefaultListableBeanFactory;

/**
 * 能够从 配置文件 中读取BeanDefinition的抽象的应用程序上下文
 */
public abstract class AbstractConfigFileApplicationContext extends AbstractRefreshApplicationContext {
    /**
     * 从配置文件中读取BeanDefinition
     */
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        // 创建XmlBeanDefinitionReader对象，用于读取Bean定义
        BeanDefinitionReader beanDefinitionReader = getBeanDefinitionReader(beanFactory);
        // 获取配置文件路径
        String[] configLocations = getConfigLocations();
        // 加载Bean定义
        beanDefinitionReader.loadBeanDefinitions(configLocations);
    }

    protected abstract BeanDefinitionReader getBeanDefinitionReader(DefaultListableBeanFactory beanFactory);

    // 抽象方法，获取配置文件路径
    protected abstract String[] getConfigLocations();
}

