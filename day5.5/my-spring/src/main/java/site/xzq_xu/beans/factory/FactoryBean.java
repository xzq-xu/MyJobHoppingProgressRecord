package site.xzq_xu.beans.factory;

/**
 * 工厂Bean，用于创建Bean实例
 * @param <T>
 */
public interface FactoryBean<T> {

    /**
     *  获取对象
     * @return 对象
     * @throws Exception 异常
     */
    T getObject() throws Exception;

    /**
     * 是否为单例
     * @return
     */
    boolean isSingleton();

}
