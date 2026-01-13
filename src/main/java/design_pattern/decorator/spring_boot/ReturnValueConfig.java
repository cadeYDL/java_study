package design_pattern.decorator.spring_boot;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;
@Configuration
public class ReturnValueConfig implements InitializingBean {
    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    public ReturnValueConfig(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        this.requestMappingHandlerAdapter = requestMappingHandlerAdapter;
    }

    @Override
    public void afterPropertiesSet()  {
        List<HandlerMethodReturnValueHandler> originHandlers = requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>(originHandlers.size());
        for (HandlerMethodReturnValueHandler originHandler : originHandlers) {
            if (originHandler instanceof RequestResponseBodyMethodProcessor) {
                newHandlers.add(new TimstampReturnValueHandler(originHandler));
            }else{
                newHandlers.add(originHandler);
            }
        }
        requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
    }
}
