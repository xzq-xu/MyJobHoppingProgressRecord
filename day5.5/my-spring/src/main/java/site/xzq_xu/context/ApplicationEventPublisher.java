package site.xzq_xu.context;

/**
 * 应用事件发布者接口
 */
public interface ApplicationEventPublisher {

    /**
     * 发布应用事件
     */
    void publishEvent(ApplicationEvent event);
}
