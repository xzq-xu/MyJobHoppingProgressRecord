package site.xzq_xu.test.ioc.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Person {
    private String name;
    private int age;
    private Cat cat;

    public void init(){
        System.out.println("Person init");
    }

    public void destroy(){
        System.out.println("Person destroy");
    }


}
