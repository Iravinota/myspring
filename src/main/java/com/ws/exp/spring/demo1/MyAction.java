package com.ws.exp.spring.demo1;

import com.ws.exp.spring.framework.annotation.WAutowired;
import com.ws.exp.spring.framework.annotation.WController;
import com.ws.exp.spring.framework.annotation.WRequestMapping;

/**
 * MyAction
 *
 * @author Eric at 2020-04-09_14:10
 */
@WController
@WRequestMapping("/aaa")
public class MyAction {
    @WAutowired
    private Hello helloInstance;

    public void print() {
        helloInstance.print();
    }
}
