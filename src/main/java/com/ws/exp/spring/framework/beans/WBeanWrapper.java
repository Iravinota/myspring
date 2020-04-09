package com.ws.exp.spring.framework.beans;

/**
 * 封装创建后的bean实例。代理对象或者原生对象都由BeanWrapper来保存。
 *
 * @author Eric at 2020-04-07_16:18
 */
public class WBeanWrapper {
    private Object wrappedInstance;
//    private Class<?> wrappedClass;

    public WBeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    public Object getWrappedInstance() {
        return wrappedInstance;
    }

    // 返回代理以后的Class，可能会是这个$Proxy0
    public Class<?> getWrappedClass() {
        return this.wrappedInstance.getClass();
    }
}
