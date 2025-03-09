package site.xzq_xu.test.ioc.bean;

import site.xzq_xu.beans.factory.FactoryBean;

public class PersonFactoryBean implements FactoryBean<Person> {
    @Override
    public Person getObject() throws Exception {
        var cat = new Person();
        cat.setName("LisiFC");
        cat.setAge(52);
        return cat;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
