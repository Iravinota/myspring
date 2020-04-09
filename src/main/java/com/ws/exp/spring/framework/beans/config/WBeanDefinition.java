package com.ws.exp.spring.framework.beans.config;

import lombok.Data;

/**
 * WBeanDefinition: 主要用于保存bean相关配置信息
 * WBeanDefinitionReader会根据application.properties中的scanPackage参数，读取其包下所有.class文件，生成BeanDefinition
 *
 * @author Eric at 2020-04-07_16:12
 */
@Data
public class WBeanDefinition {
    private String factoryBeanName;     // 首字母小写的简单类名，例如wBeanDefinition，Class.getSimpleName(); 或者接口的全类名
    private String beanClassName;       // 全类名，例如com.ws.exp.spring.framework.beans.config.WBeanDefinition
    private boolean lazyInit = false;
    private boolean isSingleton = true;
}
