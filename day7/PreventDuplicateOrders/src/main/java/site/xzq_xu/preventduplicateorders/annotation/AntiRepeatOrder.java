package site.xzq_xu.preventduplicateorders.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AntiRepeatOrder {
    // 定义枚举类型，用于限制类型
    enum LimitType {
        // 使用令牌进行限制
        TOKEN,
        // 使用参数进行限制
        PARAM,
        // 自定义限制
        CUSTOM
    }

    // 默认使用参数进行限制
    LimitType limitType() default LimitType.PARAM;
    // 默认锁定时间为5秒
    long lockTime() default 5;

    Class<? extends KeyCreator> customKeyCreator() default KeyCreator.class;

}




