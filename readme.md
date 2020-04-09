# MySpring

参考[《Spring5核心原理与30个类手写实战》随书代码示例工程](https://github.com/gupaoedu-tom/spring5-samples)，自己手写Spring代码，梳理Spring架构。

自己手写的代码都以W开头，以和spring framework中的代码进行区分

## 1. IoC容器

- src/main/java/com.ws.exp.spring.framework.annotation: 注解
- com.ws.exp.spring.framework.core: IoC顶层接口设计
  - BeanFactory: 从IoC容器中获取bean实例
- com.ws.exp.spring.framework.beans: 
  - config.WBeanDefinition: bean的定义，从XML文件解析到内存中
  - WBeanWrapper: 封装创建后的bean实例。代理对象或者原生对象都由BeanWrapper来保存
- com.ws.exp.spring.framework.context.support.WAbstractApplicationContext: IoC Context容器类的顶层实现类，实现IoC容器相关的公共逻辑refresh()
- com.ws.exp.spring.framework.beans.support.WDefaultListableBeanFactory
  - IoC Context容器子类的典型代表
  - 继承了WAbstractApplicationContext
  - 定义了顶层的IoC缓存beanDefinitionMap
- com.ws.exp.spring.framework.beans.support.WBeanDefinitionReader
  - 用在WApplicationContext类中
  - 读取指定包下的所有.class文件，生成BeanDefinition
- com.ws.exp.spring.framework.context.WApplicationContext
  - IoC核心容器
  - 入口
  - 执行扫描.class文件，并封装成BeanDefinition，存入beanDefinitionMap里
  - 对非延迟类执行DI - doAutowrited() -> getBean()
  
## 2. DI

