package toy.spring.sub;

import toy.spring.BeanPostProcessor;
import toy.spring.Component;

@Component
public class MyBeanProcessor implements BeanPostProcessor {
    @Override
    public Object afterInitializeBean(Object bean, String beanName) {
        System.out.println(beanName+"初始化完成");
        return bean;
    }
}
