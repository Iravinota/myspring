package com.ws.exp.spring.framework.webmvc.servlet;

import com.ws.exp.spring.framework.annotation.WRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理URL中上送的参数列表与Method中的参数列表进行对应，并完成类型转换
 *
 * @author Eric at 2020-04-10_10:21
 */
public class WHandlerAdapter {
    public boolean supports(Object handler) {
        return handler instanceof WHandlerMapping;
    }

    /**
     * 根据URL上送参数，执行对应的方法
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public WModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
        WHandlerMapping handlerMapping = (WHandlerMapping)handler;
        // 把方法的形参列表和request的参数列表所在顺序进行一一对应
        Map<String, Integer> paramIndexMapping = new HashMap<>();
        //提取方法中加了注解的参数
        //把方法上的注解拿到，得到的是一个二维数组
        //因为一个参数可以有多个注解，而一个方法又有多个参数
        Annotation[][] pa = handlerMapping.getMethod().getParameterAnnotations();
        for (int i = 0; i < pa.length; i++) {
            for (Annotation a : pa[i]) {
                if (a instanceof WRequestParam) {
                    String paramName = ((WRequestParam)a).value();
                    if (!"".equals(paramName.trim())) {
                        paramIndexMapping.put(paramName, i);    // @RequestParam对应的第几个参数
                    }
                }
            }
        }

        // 提取方法中的request和response参数
        Class<?>[] paramsTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < paramsTypes.length; i++) {
            Class<?> type = paramsTypes[i];
            if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                paramIndexMapping.put(type.getName(), i);
            }
        }

        // Eric - 获取URL上送参数的参数列表
        Map<String, String[]> params = request.getParameterMap();
        // Eric - method中参数列表
        Object[] paramValues = new Object[paramsTypes.length];

        for (Map.Entry<String, String[]> param : params.entrySet()) {
            String value = Arrays.toString(param.getValue())        // [aaa, bbb]
                    .replaceAll("\\[|\\]", "")     // 去掉[和]
                    .replaceAll("\\s", ",");
            if (!paramIndexMapping.containsKey(param.getKey())) {
                continue;
            }
            int index = paramIndexMapping.get(param.getKey());
            paramValues[index] = caseStringValue(value, paramsTypes[index]);
        }

        if(paramIndexMapping.containsKey(HttpServletRequest.class.getName())) {
            int reqIndex = paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = request;
        }

        if(paramIndexMapping.containsKey(HttpServletResponse.class.getName())) {
            int respIndex = paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = response;
        }

        // Eric - 执行@RequestMapping对应的方法，实参从URL中获取，instance实例通过参数handler传递过来
        Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);

        if (result == null || result instanceof Void) {
            return null;
        }

        boolean isModelAndView = handlerMapping.getMethod().getReturnType() == WModelAndView.class;
        if(isModelAndView){
            return (WModelAndView) result;
        }

        return null;
    }

    private Object caseStringValue(String value, Class<?> paramsType) {
        if (String.class == paramsType) {
            return value;
        } else if (Integer.class == paramsType) {
            return Integer.valueOf(value);
        } else if (Double.class == paramsType) {
            return Double.valueOf(value);
        } else {
            if (value != null) {
                return value;
            }
            return null;
        }
    }
}
