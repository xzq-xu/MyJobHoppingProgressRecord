package site.xzq_xu.beans.factory;

/**
 * void destroy() throws Exception;
 */
public interface DisposableBean {

    // 销毁方法，用于在Bean销毁时执行一些清理操作
    void destroy() throws Exception;

}
