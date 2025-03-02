package site.xzq_xu.context.support;

import site.xzq_xu.beans.factory.json.JsonBeanDefinitionReader;
import site.xzq_xu.beans.factory.support.BeanDefinitionReader;
import site.xzq_xu.beans.factory.support.DefaultListableBeanFactory;

public class ClassPathJsonApplicationContext extends AbstractConfigFileApplicationContext {

    private String[] configLocations;


    public ClassPathJsonApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        refresh();
    }

    @Override
    protected BeanDefinitionReader getBeanDefinitionReader(DefaultListableBeanFactory beanFactory) {
        return new JsonBeanDefinitionReader(beanFactory,this);
    }

    @Override
    protected String[] getConfigLocations() {
        return configLocations;
    }
}
