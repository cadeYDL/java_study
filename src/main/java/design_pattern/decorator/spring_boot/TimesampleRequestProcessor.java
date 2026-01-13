package design_pattern.decorator.spring_boot;

import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.Map;

public class TimesampleRequestProcessor implements HandlerMethodArgumentResolver {

    private ApplicationContext applicationContext;
    private RequestResponseBodyMethodProcessor processor;

    public TimesampleRequestProcessor(ApplicationContext r) {
        this.applicationContext = r;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TimesampleRequestResp.class);
    }

    @Nullable
    @Override
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        setupProcessor();
        Object obj = processor.resolveArgument(parameter,mavContainer, webRequest, binderFactory);
        if (!(obj instanceof Map<?,?>)){
            return obj;
        }
        ((Map) obj).put("timestamp",System.currentTimeMillis());
        return obj;
    }

    private void setupProcessor() {
        if (processor != null){
            return;
        }
        for(var r:applicationContext.getBean(RequestMappingHandlerAdapter.class).getArgumentResolvers()){
            if (!(r instanceof RequestResponseBodyMethodProcessor)){
                continue;
            }
            this.processor = (RequestResponseBodyMethodProcessor) r;
            return;
        }
    }
}
