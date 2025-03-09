package site.xzq_xu.context;

import site.xzq_xu.beans.factory.Aware;

/**
 * applicationContext 感知接口
 */
public interface ApplicationContextAware extends Aware {

    void  setApplicationContext(ApplicationContext applicationContext);
}
