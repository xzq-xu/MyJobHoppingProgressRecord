package site.leftx;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;
import site.leftx.noproxy.A;
import site.leftx.noproxy.B;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");

        A a = context.getBean(A.class);
        B b = context.getBean(B.class);
        Assert.isTrue(a.getB() == b,"不一致");

    }
}