package com.ws.exp.spring.framework.beans.support;

import com.ws.exp.spring.framework.beans.config.WBeanDefinition;
import com.ws.exp.spring.framework.context.support.WAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IoC Context容器子类的典型代表
 * 继承了WAbstractApplicationContext
 * 定义了顶层的IoC缓存beanDefinitionMap
 *
 * @author Eric at 2020-04-08_15:46
 */
public class WDefaultListableBeanFactory extends WAbstractApplicationContext {
    // <factoryBeanName, WBeanDefinition>，存储扫码到的.class配置信息
    protected final Map<String, WBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
}
