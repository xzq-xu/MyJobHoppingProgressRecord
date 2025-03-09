package site.xzq_xu.test.ioc.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import site.xzq_xu.beans.factory.DisposableBean;
import site.xzq_xu.beans.factory.InitializingBean;

@Getter
@Setter
@ToString
public class Cat implements InitializingBean , DisposableBean {
    private String name;

    private Integer age;

    @Override
    public void destroy() throws Exception {
        System.out.println("cat destroy");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("cat afterPropertiesSet");
    }
}
