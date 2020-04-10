package com.ws.exp.spring.framework.webmvc.servlet;

import java.io.File;
import java.util.Locale;

/**
 * WViewResolver
 *
 * @author Eric at 2020-04-10_15:29
 */
public class WViewResolver {
    private final String DEFAULT_TEMPLATE_SUFFX = ".html";
    private File templateRootDir;

    public WViewResolver(String templateRoot) {
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        templateRootDir = new File(templateRootPath);
    }

    public WView resolveViewName(String viewName, Locale locale) throws Exception {
        if (null == viewName || "".equals(viewName.trim())) {
            return null;
        }
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFX);
        File templateFile = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+", "/"));
        return new WView(templateFile);
    }
}
