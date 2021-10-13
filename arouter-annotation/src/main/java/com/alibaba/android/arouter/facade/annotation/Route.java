package com.alibaba.android.arouter.facade.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a page can be route by router.
 *
 * @author Alex <a href="mailto:zhilong.liu@aliyun.com">Contact me.</a>
 * @version 1.0
 * @since 16/8/15 下午9:29
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Route {

    /**
     * Path of route
     */
    // 路由URL字符串
    String path();

    /**
     * Used to merger routes, the group name MUST BE USE THE COMMON WORDS !!!
     * 用于合并路由，组名必须使用常用词!!!
     */
    // 组名，默认为一级路由名；一旦被设置，跳转时必须赋值
    String group() default "";

    /**
     * Name of route, used to generate javadoc.
     * 该路由的名称，用于产生JavaDoc
     */
    String name() default "";

    /**
     * Extra data, can be set by user.
     * 额外数据，可由用户设置。
     * Ps. U should use the integer num sign the switch, by bits. 10001010101010
     * 例如，你应该使用整数数字符号的开关，由位。10001010101010
     */
    // 额外配置的开关信息；譬如某些页面是否需要网络校验、登录校验等
    int extras() default Integer.MIN_VALUE;

    /**
     * The priority of route.
     * 路由的优先级
     */
    int priority() default -1;
}
