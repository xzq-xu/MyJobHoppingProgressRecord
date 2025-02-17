package site.xzq_xu.beans.factory.config;

import lombok.Getter;

/**
 * 包装对于另一个Bean的引用
 */
@Getter
public class BeanReference {
    // Bean的名称
    private final String beanName;

    // 构造函数，传入Bean的名称
    public BeanReference(String beanName) {
        this.beanName = beanName;
    }
}
