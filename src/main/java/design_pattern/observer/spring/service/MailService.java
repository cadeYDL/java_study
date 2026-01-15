package design_pattern.observer.spring.service;

import design_pattern.observer.spring.event.RegisterEvent;
import design_pattern.observer.spring.event_multicaster.MyMulticaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class MailService implements ApplicationListener<RegisterEvent> {

    @EventListener
    public void onRegister(RegisterEvent event){
        String user = event.getUser();
        System.out.println(user+"欢迎！");
    }

    @Override
    public void onApplicationEvent(RegisterEvent event) {
        String user = event.getUser();
        System.out.println(user+"欢迎！");
    }
}
