package com.ws.test;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;

/**
 * URLTest
 *
 * @author Eric at 2020-04-08_16:10
 */
public class URLTest implements Serializable {
    private String aaa;
    private URLTest urlTest;

    public void testURL() {
        URL url = this.getClass().getResource("/com/ws/exp");
        System.out.println(url);    // file:/E:/pro/javapro/02-experiment/myspring/target/classes/com/ws/exp
        System.out.println(url.getFile());  // /E:/pro/javapro/02-experiment/myspring/target/classes/com/ws/exp
    }

    public void testClass() {
        try {
            Class<?> beanClass = Class.forName("com.ws.test.URLTest");
            System.out.println(beanClass);  // class com.ws.test.URLTest
            System.out.println("SimpleName: " + beanClass.getSimpleName()); // URLTest
            System.out.println("Name: " + beanClass.getName());             // com.ws.test.URLTest

            Class<?>[] interfaces = beanClass.getInterfaces();
            for (Class<?> i : interfaces) {
                System.out.println("Interface: " + i);
                System.out.println(i.getName());
                System.out.println(i.getSimpleName());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void testFields() {
        try {
            Class<?> beanClass = Class.forName("com.ws.test.URLTest");
            Field[] fields = beanClass.getDeclaredFields();
            System.out.println("----testFields----");
            for (Field field : fields) {
                System.out.println(field.getType().getName());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void testArrays() {
        System.out.println(Arrays.toString(new String[]{"aaa", "bbb"}));
    }

    public static void main(String[] args) {
        URLTest urlTest = new URLTest();
        urlTest.testURL();
        urlTest.testClass();
        urlTest.testFields();
        urlTest.testArrays();
    }
}
