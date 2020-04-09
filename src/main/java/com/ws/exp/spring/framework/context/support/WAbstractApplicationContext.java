package com.ws.exp.spring.framework.context.support;

/**
 * IoC容器实现的顶层设计
 *
 * @author Eric at 2020-04-08_15:30
 */
public abstract class WAbstractApplicationContext {
    // 供子类重写
    public void refresh() throws Exception {
    }
}
