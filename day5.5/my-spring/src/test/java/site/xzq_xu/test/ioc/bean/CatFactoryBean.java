package site.xzq_xu.test.ioc.bean;

import site.xzq_xu.beans.factory.FactoryBean;

public class CatFactoryBean implements FactoryBean<Cat> {
    @Override
    public Cat getObject() throws Exception {
        Cat cat = new Cat();
        cat.setName("TomFC");
        cat.setAge(5);
        return cat;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
