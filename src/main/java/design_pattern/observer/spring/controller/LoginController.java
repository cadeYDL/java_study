package design_pattern.observer.spring.controller;


import design_pattern.observer.spring.event.RegisterEvent;
import design_pattern.observer.spring.event_multicaster.MyMulticaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private MyMulticaster multicaster;

    @RequestMapping("/login")
    public String login(@RequestParam("user") String user){
        multicaster.multicastEvent(new RegisterEvent(user));
        return user+"login";
    }
}
