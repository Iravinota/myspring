package com.ws.exp.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * WRequestParam
 *
 * @author Eric at 2020-04-07_15:51
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WRequestParam {
    String value() default "";
}
