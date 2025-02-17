package site.xzq_xu.beans.factory.support;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.config.BeanDefinition;

import javax.lang.model.util.ElementFilter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 使用 ByteBuddy 实现的实例化策略，通过动态创建子类来实例化 Bean。
 */
public class ByteBuddySubclassingInstantiationStrategy implements InstantiationStrategy {

    @Override
    public Object instantiate(BeanDefinition beanDefinition) throws BeansException {
        try {
            // 获取目标类
            Class<?> beanClass = beanDefinition.getBeanClass();

            // 使用 ByteBuddy 动态创建子类
            return new ByteBuddy()
                    .subclass(beanClass) // 设置父类
                    .method(methodDescription -> !methodDescription.isFinal())
                    .intercept(InvocationHandlerAdapter.of(new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            // 调用父类方法
                            return method.invoke(proxy, args);
                        }
                    }))
                    .make() // 生成字节码
                    .load(beanClass.getClassLoader()) // 加载到类加载器
                    .getLoaded() // 获取生成的类
                    .newInstance(); // 创建实例
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeansException("Failed to instantiate bean using ByteBuddy", e);
        }
    }
}