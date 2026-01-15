package design_pattern.observer.spring.event;

import org.springframework.context.ApplicationEvent;

public class RegisterEvent extends ApplicationEvent {
    public RegisterEvent(Object source) {
        super(source);
    }

    public String getUser(){
        return getSource().toString();
    }
}
