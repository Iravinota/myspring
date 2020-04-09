package com.ws.exp.spring.demo1;

import lombok.Data;

/**
 * Hello
 *
 * @author Eric at 2020-04-09_14:04
 */
@Data
public class Hello implements IPrint {
    private String name;

    @Override
    public void print(String ... args) {
        System.out.println("Hello " + name);
        for (String arg : args) {
            System.out.println("arg: " + arg);
        }
    }
}
