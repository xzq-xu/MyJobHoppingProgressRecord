package site.xzq_xu.test.ioc.service;

import lombok.Getter;
import lombok.Setter;
import site.xzq_xu.beans.factory.BeanFactory;
import site.xzq_xu.beans.factory.BeanFactoryAware;
import site.xzq_xu.context.ApplicationContext;
import site.xzq_xu.context.ApplicationContextAware;

@Setter
@Getter
public class AwareTestService implements BeanFactoryAware, ApplicationContextAware {
    private ApplicationContext applicationContext;
    private BeanFactory beanFactory;

    public String hello(){
        System.out.println("hello");
        return "hello";
    }
}
