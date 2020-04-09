package com.ws.exp.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * WAutowired
 *
 * @author Eric at 2020-04-07_15:47
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WAutowired {
    String value() default "";
}
