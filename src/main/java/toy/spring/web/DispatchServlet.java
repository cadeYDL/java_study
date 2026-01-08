package toy.spring.web;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import toy.spring.BeanPostProcessor;
import toy.spring.Component;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class DispatchServlet extends HttpServlet implements BeanPostProcessor {
    private static final Pattern PATTERN = Pattern.compile("@\\{(.*?)}");
    HashMap<String,WebHandler> path2Handler = new HashMap<>();
    HashMap<String, List<Interceptor>> path2Interceptors = new HashMap<>();
    @Override
    public Object afterInitializeBean(Object bean, String beanName) {
        if (bean.getClass().isAnnotationPresent(InterceptorMapping.class) && Interceptor.class.isAssignableFrom(bean.getClass())) {
            return registerInterceptor((Interceptor) bean);
        }
        if(!bean.getClass().isAnnotationPresent(Controller.class)){
            return bean;
        }
        RequestMapping classMapping = bean.getClass().getDeclaredAnnotation(RequestMapping.class);
        String classPath = classMapping==null?"":classMapping.path();
        Arrays.stream(bean.getClass().getMethods()).filter(m->m.isAnnotationPresent(RequestMapping.class)).forEach(m->{
            RequestMapping methodMapping = m.getDeclaredAnnotation(RequestMapping.class);
            String path =classPath.concat(methodMapping.path());
            if (path2Handler.containsKey(path)) {
                throw new RuntimeException("path already exists");
            }
            path2Handler.put(path,new WebHandler(m,bean));
        });
        return bean;
    }

    private Object registerInterceptor(Interceptor bean) {
        Arrays.stream(bean.getClass().getDeclaredAnnotation(InterceptorMapping.class).value()).forEach(path->{
            List<Interceptor> interceptors =  path2Interceptors.get(path);
            if (interceptors==null){
                interceptors = new ArrayList<>();
            }
            interceptors.add(bean);
            path2Interceptors.put(path,interceptors);
        });
        return bean;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        WebHandler handler = path2Handler.get(req.getRequestURI());
        if (handler == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        List<Interceptor> interceptors = path2Interceptors.get(req.getRequestURI());
        Object control = handler.getControllerBean();
        Object[] args = resolveArgs(req,handler.getMethod());

        try {
            if (interceptors!=null) {
                for (Interceptor interceptor : interceptors) {
                    if (!interceptor.preHandle(req,resp)){
                        return;
                    }
                }
            }
            Object result = handler.getMethod().invoke(control,args);
            dealResult(result,handler.getResultType(),resp);
            if (interceptors!=null) {
                for (Interceptor interceptor : interceptors) {
                    interceptor.postHandle(req,resp,handler);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] resolveArgs(HttpServletRequest req, Method method) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Param p = parameter.getAnnotation(Param.class);
            String value = p!=null?req.getParameter(p.value()): req.getParameter(parameter.getName());
            boolean notRequired = p!=null && !p.required();
            if (value==null&&!notRequired){
                throw new RuntimeException("参数不全");
            }
            Class<?> type = parameter.getType();
            switch (type.getSimpleName()){
                case "String":
                    if (value==null){
                        value = "";
                    }
                    args[i] = value;
                    break;
                case "int","Integer":
                    if (value==null){
                        value = "0";
                    }
                    args[i] = Integer.parseInt(value);
                    break;
                default:
                    args[i] = null;
            }
        }
        return args;
    }

    private void dealResult(Object result, WebHandler.ResultType resultType, HttpServletResponse resp) throws Exception {
        switch (resultType){
            case HTML->{
                resp.setContentType("text/html;charset=utf-8");
                resp.getWriter().write(result.toString());
            }
            case JSON -> {
                resp.setContentType("application/json;charset=utf-8");
                resp.getWriter().write(JSONObject.toJSONString(result));
            }
            case LOCAL->{
                ModelAndView mv = (ModelAndView) result;
                String view = mv.getView();
                InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(view);
                try (resourceAsStream) {
                    String html = new String(resourceAsStream.readAllBytes());
                    html = renderTemplate(html, mv.getContext());
                    resp.setContentType("text/html;charset=UTF-8");
                    resp.getWriter().write(html);
                }
            }

        }
    }

    private String renderTemplate(String template, Map<String, String> context) {
        Matcher matcher = PATTERN.matcher(template);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = context.getOrDefault(key, "");
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
