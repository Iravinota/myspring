package com.ws.exp.spring.demo1;

/**
 * Hi
 *
 * @author Eric at 2020-04-09_14:14
 */
public class Hi implements IPrint {
    @Override
    public void print(String... args) {
        System.out.println("Hi " + args);
    }
}
