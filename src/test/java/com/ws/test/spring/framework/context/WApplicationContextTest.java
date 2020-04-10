package com.ws.test.spring.framework.context;

import com.ws.exp.spring.framework.context.WApplicationContext;
import org.junit.Assert;
import org.junit.Test;

/**
 * WApplicationContextTest
 *
 * @author Eric at 2020-04-09_16:53
 */
public class WApplicationContextTest {

    @Test
    public void test() throws Exception {
        WApplicationContext context = new WApplicationContext("application.properties");
        Object bean = context.getBean("hello");
        System.out.println("Hello bean: " + bean);      // Hello bean: Hello(name=null)
        Assert.assertTrue(bean != null);

        bean = context.getBean("myAction");
        System.out.println("MyAction bean: " + bean);   // MyAction bean: com.ws.exp.spring.demo1.MyAction@2530c12
        Assert.assertTrue(bean != null);

        bean = context.getBean("com.ws.exp.spring.demo1.IPrint");
        System.out.println("IPrint bean: " + bean);     // IPrint bean: Hello(name=null)
        Assert.assertTrue(bean != null);

        Assert.assertTrue(true);
    }
}
