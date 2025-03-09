package site.xzq_xu.beans.factory;

/**
 * Bean工厂感知接口
 */
public interface BeanFactoryAware extends Aware {
    void setBeanFactory(BeanFactory beanFactory);
}
