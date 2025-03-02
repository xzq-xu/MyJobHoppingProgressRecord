package site.xzq_xu.context.support;

import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.ConfigurableListableBeanFactory;
import site.xzq_xu.beans.factory.support.DefaultListableBeanFactory;
import site.xzq_xu.core.io.Resource;


/**
 * 能够刷新容器的抽象应用程序上下文
 */
public abstract class AbstractRefreshApplicationContext extends AbstractApplicationContext {

    // 定义一个DefaultListableBeanFactory类型的变量
    private  DefaultListableBeanFactory beanFactory;

    // 重写refreshBeanFactory方法
    @Override
    protected void refreshBeanFactory() throws BeansException {
        //1.创建beanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = createBeanFactory();
        //2.加载beanDefinition
        loadBeanDefinitions(defaultListableBeanFactory);
        //3.将beanFactory赋值给当前类
        this.beanFactory = defaultListableBeanFactory;
    }

    /**
     * 加载beanDefinition
     * @param defaultListableBeanFactory
     */
    // 定义一个抽象方法，用于加载beanDefinition
    protected  abstract void loadBeanDefinitions(DefaultListableBeanFactory defaultListableBeanFactory);

    /**
     * 创建BeanFactory
     * @return
     */
    // 定义一个方法，用于创建BeanFactory
    protected DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    // 重写getBeanFactory方法
    @Override
    public ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
