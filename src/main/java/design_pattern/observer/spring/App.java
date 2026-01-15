package design_pattern.observer.spring;

import design_pattern.observer.spring.event_multicaster.MyMulticaster;
import design_pattern.observer.spring.service.GiftService;
import design_pattern.observer.spring.service.MailService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(App.class);
        context.getBean(MyMulticaster.class).addApplicationListener(context.getBean(GiftService.class));
        context.getBean(MyMulticaster.class).addApplicationListener(context.getBean(MailService.class));
    }
}
