package com.ws.exp.spring.framework.webmvc.servlet;

import com.ws.exp.spring.framework.context.WApplicationContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;

/**
 * Web MVC的入口
 *
 * @author Eric at 2020-04-10_9:17
 */
@Slf4j
public class WDispacherServlet extends HttpServlet {
    private final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";
    private WApplicationContext context;
    private List<WHandlerMapping> handlerMappings = new ArrayList<>();
    private Map<WHandlerMapping, WHandlerAdapter> handlerAdapters = new HashMap<>();
    private List<WViewResolver> viewResolvers = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("500 Exception,Details:\r\n" + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", "").replaceAll(",\\s", "\r\n"));
            e.printStackTrace();
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //1、通过从request中拿到URL，去匹配一个HandlerMapping
        WHandlerMapping handler = getHandler(req);
        if (handler == null) {
            processDispatchResult(req, resp, new WModelAndView("404"));
            return;
        }

        //2、准备调用前的参数
        WHandlerAdapter ha = getHandlerAdapter(handler);

        //3、真正的调用方法,返回ModelAndView存储了要穿页面上值，和页面模板的名称
        WModelAndView mv = ha.handle(req, resp, handler);

        //这一步才是真正的输出
        processDispatchResult(req, resp, mv);
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, WModelAndView mv) throws Exception {
        //把给我的ModleAndView变成一个HTML、OuputStream、json、freemark、veolcity
        //ContextType
        if (null == mv) {
            return;
        }

        //如果ModelAndView不为null，怎么办？
        if (this.viewResolvers.isEmpty()) {
            return;
        }

        for (WViewResolver viewResolver : this.viewResolvers) {
            WView view = viewResolver.resolveViewName(mv.getViewName(), null);
            view.render(mv.getModel(), req, resp);
            return;
        }
    }

    // 从handlerMapping中，找与url匹配的handler。 即@RequestMapping中的值和url匹配时的处理方法
    private WHandlerMapping getHandler(HttpServletRequest req) {
        if (this.handlerMappings.isEmpty()) {
            return null;
        }

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");

        for (WHandlerMapping handler : handlerMappings) {
            try {
                Matcher matcher = handler.getPattern().matcher(url);
                //如果没有匹配上继续下一个匹配
                if (!matcher.matches()) {
                    continue;
                }

                return handler;
            } catch (Exception e) {
                throw e;
            }
        }
        return null;
    }
}
