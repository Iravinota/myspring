package com.ws.exp.spring.framework.context;

import com.ws.exp.spring.framework.annotation.WAutowired;
import com.ws.exp.spring.framework.annotation.WController;
import com.ws.exp.spring.framework.annotation.WService;
import com.ws.exp.spring.framework.beans.WBeanWrapper;
import com.ws.exp.spring.framework.beans.config.WBeanDefinition;
import com.ws.exp.spring.framework.beans.support.WBeanDefinitionReader;
import com.ws.exp.spring.framework.beans.support.WDefaultListableBeanFactory;
import com.ws.exp.spring.framework.core.WBeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IoC核心容器
 *
 * @author Eric at 2020-04-08_15:58
 */
public class WApplicationContext extends WDefaultListableBeanFactory implements WBeanFactory {

    private String[] configLocations;
    private WBeanDefinitionReader reader;
    // 单例的IoC容器 <beanClassName, classInstance>
    private Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();
    // 通用的IoC容器 <factoryBeanName, wrappered-bean-instance>
    private Map<String, WBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();

    public WApplicationContext(String ... configLocations) {
        this.configLocations = configLocations;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refresh() throws Exception {
        // 1. 加载application.properties配置文件
        reader = new WBeanDefinitionReader(configLocations);
        // 2. 扫描配置文件中指定包下的.class文件，把它们封装成BeanDefinition
        List<WBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
        // 3、Eric - 把.class文件生成的BeanDefinition信息保存在内存中，具体是beanDefinitionMap里
        doRegisterBeanDefinition(beanDefinitions);
        // 4. Eric - 对非延时注册的类进行DI
        doAutowrited();
    }

    // 处理非延迟加载的类，让其自动装配
    private void doAutowrited() {
        for (Map.Entry<String, WBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                try {
                    getBean(beanName);  // Eric - 执行DI
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Eric - 把.class文件生成的BeanDefinition信息保存在内存中，具体是beanDefinitionMap里
    // Eric - 简化处理，beanDefinitionMap中，不允许出现重名的类，即id/factoryBeanName不重名。重名则抛出异常。扫描的包下不可以有重名的简单类名
    // Eric - 多个类继承同一个接口，也会报错
    private void doRegisterBeanDefinition(List<WBeanDefinition> beanDefinitions) throws Exception {
        for (WBeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The “" + beanDefinition.getFactoryBeanName() + "” is exists!!");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }

    /**
     * DI操作
     * @param beanName factoryBeanName
     * @return
     * @throws Exception
     */
    @Override
    public Object getBean(String beanName) throws Exception {
        WBeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);
        Object instance = null;

//        WBeanPostProcessor postProcessor = new WBeanPostProcessor();
//        postProcessor.postProcessBeforeInitialization(instance, beanName);

        instance = instantiateBean(beanName, beanDefinition);
        if (instance == null) {
            return null;
        }

        WBeanWrapper beanWrapper = new WBeanWrapper(instance);
        this.factoryBeanInstanceCache.put(beanName, beanWrapper);

//        postProcessor.postProcessAfterInitialization(instance, beanName);

        // 注入
        populateBean(beanName, new WBeanDefinition(), beanWrapper);

        return factoryBeanInstanceCache.get(beanName).getWrappedInstance();
    }

    // 对添加了注解@WController或@WService的类，进行依赖注入

    /**
     *
     * @param beanName 没用
     * @param beanDefinition 没用
     * @param beanWrapper 对这个beanWrapper中的bean实例进行Autowired处理
     */
    private void populateBean(String beanName, WBeanDefinition beanDefinition, WBeanWrapper beanWrapper) {
        Object instance = beanWrapper.getWrappedInstance();
        Class<?> clazz = beanWrapper.getWrappedClass();

        // 判断只有加了注解的类，才执行依赖注入
        if (!(clazz.isAnnotationPresent(WController.class) ||
                clazz.isAnnotationPresent(WService.class))) {
            return;
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(WAutowired.class)) { // 处理@WAutowired注解
                continue;
            }

            WAutowired autowired = field.getAnnotation(WAutowired.class);
            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();  // 这里得到的是全类名java.lang.String
            }

            // 强制访问
            field.setAccessible(true);
            try {
                if (this.factoryBeanInstanceCache.get(autowiredBeanName) == null) {
                    continue;
                }
                // Eric - 把实例instance中的域field的值设置为value
                field.set(instance, factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    // 从factoryBeanObjectCache中获取已有的bean，或者新建一个bean实例
    private Object instantiateBean(String beanName, WBeanDefinition beanDefinition) {
        String className = beanDefinition.getBeanClassName();
        Object instance = null;
        try {
            if (this.factoryBeanObjectCache.containsKey(className)) {
                instance = this.factoryBeanObjectCache.get(className);
            } else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();

                this.factoryBeanObjectCache.put(className, instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getSimpleName());  // 这里是factoryBeanName，是只有最后的类名的
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new  String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount(){
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig() {
        return reader.getConfig();
    }
}
