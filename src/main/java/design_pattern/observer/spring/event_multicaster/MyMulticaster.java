package design_pattern.observer.spring.event_multicaster;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.AbstractApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;


import javax.annotation.Nullable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MyMulticaster extends AbstractApplicationEventMulticaster {
    ExecutorService executor = Executors.newFixedThreadPool(10);

    @Override
    public void multicastEvent(ApplicationEvent event) {
        this.multicastEvent(event,null);
    }

    @Override
    public void multicastEvent(ApplicationEvent event, @Nullable ResolvableType eventType) {
       ResolvableType type = eventType!=null?eventType:ResolvableType.forInstance(event);
       for(ApplicationListener listener:getApplicationListeners(event,type)){
           executor.execute(()->listener.onApplicationEvent(event));
       }
    }
}
