package com.ws.exp.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * WController
 *
 * @author Eric at 2020-04-07_15:49
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WController {
    String value() default "";
}
