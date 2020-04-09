package com.ws.exp.spring.framework.beans.support;

import com.ws.exp.spring.framework.beans.config.WBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 用在WApplicationContext类中
 * 读取指定包下的所有.class文件，生成BeanDefinition
 *
 * @author Eric at 2020-04-08_16:00
 */
public class WBeanDefinitionReader {
    private final String SCAN_PACKAGE = "scanPackage";  // 从application.properties文件中读取的参数key值

    private List<String> registyBeanClasses = new ArrayList<>();    // 扫描到的类
    private Properties config = new Properties();

    /**
     * @param locations 虽然传入的是数组，可只会使用第1个。简化的处理逻辑
     */
    public WBeanDefinitionReader(String ... locations) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(
                locations[0].replace("classpath:", ""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    // 从scanPackage指定的路径开始扫描所有.class文件，并把类名（com.ws.exp.spring.xxx.Demo）存入缓存中
    private void doScanner(String scanPackage) {
        URL url = this.getClass().getResource("/" + scanPackage.replaceAll("\\.", "/"));    // 把.替换成/
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (file.getName().endsWith(".class")) {
                    String className = (scanPackage + "." + file.getName()).replace(".class", "");
                    registyBeanClasses.add(className);
                }
            }
        }
    }

    public Properties getConfig() {
        return config;
    }

    // 把指定包下的所有.class文件扫描成BeanDefinition，以便后续的IoC操作
    public List<WBeanDefinition> loadBeanDefinitions() {
        List<WBeanDefinition> result = new ArrayList<>();
        try {
            for (String className : registyBeanClasses) {
                Class<?> beanClass = Class.forName(className);  // 类信息
                // 不对接口进行实例化
                if (beanClass.isInterface()) { continue; }

                //beanName有三种情况:
                //1、默认是类名首字母小写
                //2、自定义名字
                //3、接口注入
                result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()), beanClass.getName()));

                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> i : interfaces) {
                    //如果是多个实现类，只能覆盖
                    //为什么？因为Spring没那么智能，就是这么傻
                    //这个时候，可以自定义名字
                    result.add(doCreateBeanDefinition(i.getName(), beanClass.getName()));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    // 把每一个配信息解析成一个BeanDefinition
    private WBeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        WBeanDefinition beanDefinition = new WBeanDefinition();
        beanDefinition.setFactoryBeanName(factoryBeanName);
        beanDefinition.setBeanClassName(beanClassName);
        return beanDefinition;
    }

    // 简单逻辑，仅说明作用
    private String toLowerFirstCase(String simpleName) {
        char [] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
