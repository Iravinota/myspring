package com.ws.exp.spring.framework.core;

/**
 * 所有IoC容器的顶层设计。单例工厂的顶层设计
 *
 * @author Eric at 2020-04-07_16:03
 */
public interface WBeanFactory {
    /**
     * 根据beanName从IOC容器中获得一个实例Bean
     * @param beanName factoryBeanName
     * @return 实例Bean
     * @throws Exception 出现异常
     */
    Object getBean(String beanName) throws Exception;

    Object getBean(Class<?> beanClass) throws Exception;
}
