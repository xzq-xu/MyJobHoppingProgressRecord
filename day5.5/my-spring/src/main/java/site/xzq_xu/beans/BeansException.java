package site.xzq_xu.beans;

/**
 * 添加一个Beans异常
 */
public class BeansException extends RuntimeException{
    // 构造函数，接收一个字符串参数，调用父类的构造函数
    public BeansException(String msg) {
        super(msg);
    }

    // 构造函数，接收一个字符串参数和一个Throwable对象，调用父类的构造函数
    public BeansException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
