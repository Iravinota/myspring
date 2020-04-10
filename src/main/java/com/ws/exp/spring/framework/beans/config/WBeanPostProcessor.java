package com.ws.exp.spring.framework.beans.config;

/**
 * WBeanPostProcessor：暂时没什么用
 *
 * @author Eric at 2020-04-09_14:20
 */
public class WBeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }
}
