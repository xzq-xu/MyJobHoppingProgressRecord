package site.xzq_xu.core.io;

/**
 * 资源加载器接口
 */
public interface ResourceLoader {

    /**
     * 根据location参数获取资源
     * @param location
     * @return
     */
    Resource getResource(String location);
}
