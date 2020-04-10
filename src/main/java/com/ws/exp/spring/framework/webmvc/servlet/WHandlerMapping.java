package com.ws.exp.spring.framework.webmvc.servlet;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * WHandlerMapping
 *
 * @author Eric at 2020-04-10_10:09
 */
@AllArgsConstructor
@Data
public class WHandlerMapping {
    private Object controller;  // @RequestMapping所在的实例
    private Method method;      // @RequestMapping对应的方法
    private Pattern pattern;    // URL正则匹配
}
