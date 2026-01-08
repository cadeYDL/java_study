package toy.spring.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class WebHandler {
    @Getter
    private final Method method;
    @Getter
    private final Object controllerBean;
    @Getter
    private final ResultType resultType;


    public WebHandler(Method method, Object controllerBean) {
        this.method = method;
        this.controllerBean = controllerBean;
        this.resultType = getResultType(method);
    }

    private ResultType getResultType(Method method) {
        if (method.isAnnotationPresent(ResponseBody.class)) {
            return ResultType.JSON;
        }
        if (method.getReturnType().equals(ModelAndView.class)) {
            return ResultType.LOCAL;
        }
        return ResultType.HTML;
    }


    enum ResultType{
        JSON,HTML,LOCAL
    }



}
