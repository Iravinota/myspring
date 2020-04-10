package com.ws.exp.spring.framework.webmvc.servlet;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * Eric - 处理页面模板和要往页面传递的参数的对应关系
 *
 * @author Eric at 2020-04-10_14:09
 */
@AllArgsConstructor
@Getter
public class WModelAndView {
    private String viewName;
    private Map<String, ?> model;

    public WModelAndView(String viewName) {
        this.viewName = viewName;
    }
}
