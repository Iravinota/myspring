package com.ws.test.spring.framework.beans.support;

import com.ws.exp.spring.framework.beans.config.WBeanDefinition;
import com.ws.exp.spring.framework.beans.support.WBeanDefinitionReader;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * WBeanDefinitionReaderTest
 *
 * @author Eric at 2020-04-09_13:48
 */
public class WBeanDefinitionReaderTest {

    @Test
    public void testLoadBeanDefinitions() {
        WBeanDefinitionReader reader = new WBeanDefinitionReader("application.properties");
        List<WBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
        for (WBeanDefinition beanDefinition : beanDefinitions) {
            System.out.println(beanDefinition);
        }
        Assert.assertTrue(true);
    }
}
