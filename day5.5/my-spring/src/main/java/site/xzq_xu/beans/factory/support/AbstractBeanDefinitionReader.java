package site.xzq_xu.beans.factory.support;

import lombok.Getter;
import lombok.Setter;
import site.xzq_xu.beans.BeansException;
import site.xzq_xu.core.io.DefaultResourceLoader;
import site.xzq_xu.core.io.Resource;
import site.xzq_xu.core.io.ResourceLoader;

/**
 * AbstractBeanDefinitionReader，实现BeanDefinitionReader接口，
 * 拥有两个属性 ResourceLoader和 BeanDefinitionRegister，分别用来加载资源，注册BeanDefinition
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    public static final String BEAN_ELEMENT = "bean";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String ID_ATTRIBUTE = "id";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String REF_ATTRIBUTE = "ref";
    public static final String INIT_METHOD_ATTRIBUTE = "init-method";
    public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
    public static final String SCOPE_ATTRIBUTE = "scope";



    // BeanDefinition注册器
    private final BeanDefinitionRegistry registry;

    // 资源加载器
    private ResourceLoader resourceLoader;

    // 构造函数，传入BeanDefinition注册器
    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this(registry,new DefaultResourceLoader());
    }

    // 构造函数，传入BeanDefinition注册器和资源加载器
    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        this.registry = registry;
        this.resourceLoader = resourceLoader;
    }

    // 设置资源加载器
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    // 获取BeanDefinitionRegistry
    @Override
    public BeanDefinitionRegistry getRegistry() {
        // 返回registry
        return registry;
    }

    // 获取ResourceLoader
    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    // 加载多个locations中的BeanDefinition
    @Override
    public void loadBeanDefinitions(String[] locations) throws BeansException {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }
}
