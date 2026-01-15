package design_pattern.observer.spring.service;

import design_pattern.observer.spring.event.RegisterEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class GiftService implements ApplicationListener<RegisterEvent> {


    @EventListener
    public void onRegister(RegisterEvent event){
        String user = event.getUser();
        System.out.println(user+"新手礼包到手！");
    }

    @Override
    public void onApplicationEvent(RegisterEvent event) {
        String user = event.getUser();
        System.out.println(user+"新手礼包到手！");
    }
}
