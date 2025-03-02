package site.xzq_xu.beans.factory.support;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import site.xzq_xu.beans.BeansException;
import site.xzq_xu.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;

/**
 * Cglib 实例化策略， 通过cglib动态创建子类
 */
public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy {
    /**
     * 通过cglib动态创建子类
     * 这段代码是一个使用CGLIB（Code Generation Library）库动态创建子类实例的方法。CGLIB是一个强大的代码生成库，可以在运行时动态生成字节码，从而创建新的类。这种方法常用于需要创建代理对象或需要动态修改类的场景。
     *
     * 代码的工作流程如下：
     *
     * 创建一个Enhancer对象，这是CGLIB的核心类，用于生成代理类。
     * 设置Enhancer对象的父类为beanDefinition中定义的类。这意味着生成的代理类将继承这个类。
     * 设置Enhancer对象的回调函数。回调函数是一个MethodInterceptor，它会在代理类的方法被调用时执行。在这个例子中，回调函数只是简单地调用父类的方法。
     * 调用Enhancer对象的create方法来生成代理类的实例。这个方法会动态生成一个新的类，该类继承自指定的父类，并实现了回调函数。
     * 这段代码的用途是在运行时动态创建一个代理对象，这个代理对象可以拦截对父类方法的调用，并在调用前后执行一些额外的逻辑。这在实现AOP（面向切面编程）时非常有用，可以用于日志记录、事务管理、安全检查等场景。
     *
     * 注意事项：
     *
     * 使用CGLIB动态创建代理类需要引入CGLIB库，并且需要确保父类不是final的，因为CGLIB是通过生成子类来创建代理类的。
     * CGLIB生成的代理类在运行时会增加一些额外的开销，因为它需要动态生成字节码。因此，如果性能是一个关键考虑因素，应该谨慎使用。
     * CGLIB生成的代理类在运行时才能确定，因此无法在编译时进行静态检查。

     * @param beanDefinition Bean的定义
     * @return
     * @throws BeansException
     */
    @Override
    public Object instantiate(BeanDefinition beanDefinition) throws BeansException {
        // 创建Enhancer对象
        Enhancer enhancer = new Enhancer();
        // 设置父类
        enhancer.setSuperclass(beanDefinition.getBeanClass());
        // 设置回调函数
        /**
         * 这段代码是使用Java的动态代理机制来设置一个方法拦截器（MethodInterceptor）。具体来说，它使用Enhancer类来创建一个代理对象，并设置一个回调函数，当代理对象的方法被调用时，这个回调函数会被执行。
         *
         * 以下是这段代码的详细解释：
         *
         * enhancer.setCallback(...):
         *
         * enhancer是一个Enhancer对象，它是CGLIB（Code Generation Library）库中的一个类，用于动态生成代理类。
         * setCallback方法用于设置一个回调函数，这个回调函数会在代理对象的方法被调用时执行。
         * (MethodInterceptor) (o, method, args, methodProxy) -> methodProxy.invokeSuper(o, args):
         *
         * 这是一个Lambda表达式，用于定义回调函数。
         * MethodInterceptor是CGLIB库中的一个接口，用于定义方法拦截器。
         * o是代理对象本身。
         * method是被调用的方法。
         * args是传递给方法的参数。
         * methodProxy是代理方法的代理对象。
         * methodProxy.invokeSuper(o, args)会调用被代理类的方法，并传递参数。
         * 实现原理:
         *
         * CGLIB通过生成被代理类的子类来实现代理。子类会覆盖被代理类的方法，并在覆盖的方法中调用回调函数。
         * 当代理对象的方法被调用时，CGLIB会调用子类的方法，子类的方法会调用回调函数，回调函数会决定如何处理方法调用。
         * 用途:
         *
         * 可以用于在方法调用前后添加额外的逻辑，例如日志记录、权限检查、事务管理等。
         * 可以用于实现AOP（面向切面编程）。
         * 注意事项:
         *
         * 使用CGLIB代理时，被代理的类不能是final类，因为CGLIB需要生成被代理类的子类。
         * 被代理的方法不能是final方法，因为子类不能覆盖final方法。
         * CGLIB代理的性能可能不如Java的动态代理，因为它需要生成字节码。
         */
        enhancer.setCallback((MethodInterceptor) (o, method, args, methodProxy) -> methodProxy.invokeSuper(o, args));
        // 创建子类实例
        return enhancer.create();
    }



}
