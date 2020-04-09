package com.ws.exp.spring.framework.context;

import com.ws.exp.spring.framework.beans.WBeanWrapper;
import com.ws.exp.spring.framework.beans.config.WBeanDefinition;
import com.ws.exp.spring.framework.beans.support.WBeanDefinitionReader;
import com.ws.exp.spring.framework.beans.support.WDefaultListableBeanFactory;
import com.ws.exp.spring.framework.core.WBeanFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IoC核心容器
 *
 * @author Eric at 2020-04-08_15:58
 */
public class WApplicationContext extends WDefaultListableBeanFactory implements WBeanFactory {

    private String[] configLocations;
    private WBeanDefinitionReader reader;
    // 单例的IoC容器
    private Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();
    // 通用的IoC容器
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
        return null;
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getSimpleName());  // 这里是factoryBeanName，是只有最后的类名的
    }
}
