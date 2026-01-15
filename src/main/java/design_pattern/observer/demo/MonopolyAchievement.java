package design_pattern.observer.demo;

import java.util.HashMap;
import java.util.Map;

public class MonopolyAchievement implements EventListener{
    Map<String, Integer> eventMap = new HashMap<>();
    private  final int limit;
    public MonopolyAchievement(int limit){
         this.limit = limit;
    }
    @Override
    public void onEvent(Event event) {
        int ec = eventMap.getOrDefault(event.source(),0);
        ec++;
        if (ec>=limit){
            System.out.println(event.source()+"获得大富翁成就");
            ec = 0;
        }
        eventMap.put((String) event.source(),ec);
    }
}
