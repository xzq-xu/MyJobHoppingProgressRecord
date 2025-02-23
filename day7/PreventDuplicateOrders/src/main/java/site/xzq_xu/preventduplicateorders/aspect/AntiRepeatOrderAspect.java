package site.xzq_xu.preventduplicateorders.aspect;


import cn.hutool.crypto.SecureUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import site.xzq_xu.preventduplicateorders.annotation.AntiRepeatOrder;
import site.xzq_xu.preventduplicateorders.annotation.KeyCreator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Aspect
@Component
public class AntiRepeatOrderAspect {

    //    private  final StringRedisTemplate redisTemplate;
    private final Map<String,Boolean> timedCache = new HashMap<>();

//    public AntiRepeatOrderAspect(StringRedisTemplate redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }

    // 定义一个切点，用于匹配带有@AntiRepeatOrder注解的方法或类
    @Pointcut("@annotation(site.xzq_xu.preventduplicateorders.annotation.AntiRepeatOrder) || @within(site.xzq_xu.preventduplicateorders.annotation.AntiRepeatOrder)")
    public void annotatedMethods() {}

    // 定义一个环绕通知，用于在方法执行前后进行拦截
    @Around("annotatedMethods()")
    public Object preventDuplicateExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        // 生成一个唯一的key，用于标识方法
        String key = generateKey(joinPoint);

        StringBuilder sb = getRepeatKeyBuilder(joinPoint);
        if (sb == null) return "请求的方法有误";

        String md5Key = SecureUtil.md5(sb.toString());
        // 检查该方法是否已经执行过，如果没有执行过，则将该方法标记为已执行
//                if (Boolean.FALSE.equals(redisTemplate.hasKey(md5Key)) && Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(md5Key, "", annotation.lockTime(), TimeUnit.SECONDS))) {
//                    try {
//                        return joinPoint.proceed();
//                    } catch (Throwable e) {
//                        log.error("Exception occurred during method execution: " + key, e);
//                        throw new RuntimeException("处理异常请重试");
//                    }
//                    finally {
//                        redisTemplate.delete(md5Key);
//                    }
//                }

        if (Boolean.FALSE.equals(timedCache.containsKey(md5Key)) && ( null == timedCache.putIfAbsent(md5Key, Boolean.TRUE))) {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                log.error("Exception occurred during method execution: " + key, e);
                throw new RuntimeException("处理异常请重试");
            }
            finally {
                timedCache.remove(md5Key);
            }
        }else{
            return "请勿重复提交";
        }
    }

    private static StringBuilder getRepeatKeyBuilder(ProceedingJoinPoint joinPoint) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        //获取注解对象
        AntiRepeatOrder annotation = AnnotationUtils.getAnnotation(method, AntiRepeatOrder.class);

        if (annotation == null) {
            annotation = AnnotationUtils.getAnnotation(method.getDeclaringClass(), AntiRepeatOrder.class);
        }

        if (annotation == null) {
            log.error("No AntiRepeatOrder annotation found on method: {}", method.getName());
            return null;
        }


        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = requestAttributes.getRequest();
        String uri = request.getRequestURI();
        String limitType = annotation.limitType().name();

        StringBuilder sb = new StringBuilder(":recommit:");
        switch (limitType) {
            case "PARAM" -> {
                //参数 防重提交，基于IP，类名、方法名和URL生成唯Key
                String ipAddr = request.getRemoteAddr();
                String className = methodSignature.getDeclaringType().getSimpleName();
                String methodName = methodSignature.getMethod().getName();
                sb.append(className).append("-").append(methodName).append("-").append(uri).append("-").append(ipAddr);
            }
            case "TOKEN" ->{
                // token 防重提交，基于用户token和URL生成唯Key
                String token = request.getHeader("token");
                if (token.isBlank()){
                    throw new RuntimeException("token不存在，请求非法");
                }
                sb.append(token).append("-").append(uri);
            }
            case "CUSTOM" ->{
                // 自定义 防重提交，基于用户自定义Key生成唯Key
                KeyCreator keyCreator = annotation.customKeyCreator().getDeclaredConstructor().newInstance();
                String creatorKey = keyCreator.createKey(request, method, joinPoint.getArgs());
                sb.append(creatorKey);
            }
        }
        return sb;
    }

    // 生成一个唯一的key，用于标识方法
    private String generateKey(ProceedingJoinPoint joinPoint) {
        StringBuilder keyBuilder = new StringBuilder();
        // 将方法名添加到key中
        keyBuilder.append(joinPoint.getSignature().getName());
        // 将方法参数添加到key中
        for (Object arg : joinPoint.getArgs()) {
            keyBuilder.append(":").append(arg.toString());
        }
        // 返回生成的key
        return keyBuilder.toString();
    }
}


