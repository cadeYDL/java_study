package toy.spring.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import toy.spring.Component;
import toy.spring.web.Controller;
import toy.spring.web.Interceptor;
import toy.spring.web.InterceptorMapping;

@Component
@InterceptorMapping(value = {"/web/json","/web/local","/web/string"})
public class logInterceptor implements Interceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("logInterceptor.preHandle:"+request.getRequestURI()+" time:"+System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("logInterceptor.postHandle:"+request.getRequestURI()+" time:"+System.currentTimeMillis());
    }
}
