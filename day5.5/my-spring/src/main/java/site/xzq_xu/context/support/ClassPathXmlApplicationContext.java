package site.xzq_xu.context.support;

/**
 * 从ClassPath中加载配置文件的应用程序上下文
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {
    // 配置文件路径
    private String[] configLocations;


    // 通过配置文件路径初始化应用程序上下文
    public ClassPathXmlApplicationContext(String configLocation) {
        this(new String[]{configLocation});
    }

    // 通过配置文件路径数组初始化应用程序上下文
    public ClassPathXmlApplicationContext(String[] configLocations) {
        this.configLocations = configLocations;
        refresh();
    }

    // 获取配置文件路径数组
    @Override
    protected String[] getConfigLocations() {
        return configLocations;
    }

}
