package site.xzq_xu.context.support;

import site.xzq_xu.beans.factory.support.BeanDefinitionReader;
import site.xzq_xu.beans.factory.support.DefaultListableBeanFactory;
import site.xzq_xu.beans.factory.xml.XmlBeanDefinitionReader;
import site.xzq_xu.core.io.Resource;

/**
 * 能够从XML中读取BeanDefinition的抽象的应用程序上下文
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshApplicationContext {
    /**
     * 从XML中读取BeanDefinition
     */
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        // 创建XmlBeanDefinitionReader对象，用于读取Bean定义
        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory, this);
        // 获取配置文件路径
        String[] configLocations = getConfigLocations();
        // 加载Bean定义
        beanDefinitionReader.loadBeanDefinitions(configLocations);
    }

    // 抽象方法，获取配置文件路径
    protected abstract String[] getConfigLocations();
}
