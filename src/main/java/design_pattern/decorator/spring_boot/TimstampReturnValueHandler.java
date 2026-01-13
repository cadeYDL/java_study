package design_pattern.decorator.spring_boot;

import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

public class TimstampReturnValueHandler implements HandlerMethodReturnValueHandler {
    private final HandlerMethodReturnValueHandler curHandler;
    public TimstampReturnValueHandler(HandlerMethodReturnValueHandler curHandler) {
        this.curHandler = curHandler;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return this.curHandler.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        if (!(returnValue instanceof Map<?,?>)){
            return;
        }
        ((Map)returnValue).put("test", 1111);
        System.out.println("body"+returnValue);
        this.curHandler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);

    }
}
