package design_pattern.observer.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bus implements EventBus{
    private Map<Class<? extends Event>, List<EventListener>> subscribers = new HashMap<>();
    @Override
    public void register(EventListener subscriber,Class<? extends Event> eventType) {
        List<EventListener> list = subscribers.getOrDefault(eventType,new ArrayList<>());
        list.add(subscriber);
        subscribers.put(eventType,list);
    }

    @Override
    public void publish(Event event) {
        List<EventListener> publishSubscribers = subscribers.get(event.getClass());
        for(EventListener subscriber:publishSubscribers){
            subscriber.onEvent(event);
        }
    }
}
