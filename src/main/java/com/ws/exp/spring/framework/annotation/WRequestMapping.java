package com.ws.exp.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * WRequestMapping
 *
 * @author Eric at 2020-04-07_15:50
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WRequestMapping {
    String value() default "";
}
