package design_pattern.observer.demo;

public interface EventBus {
    void register(EventListener subscriber,Class<? extends Event> eventType);
    void publish(Event event);
}
