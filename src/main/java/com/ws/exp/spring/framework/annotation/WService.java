package com.ws.exp.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * WService
 *
 * @author Eric at 2020-04-07_15:39
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WService {
    String value() default "";
}
