package design_pattern.observer.demo;

import java.util.HashMap;
import java.util.Map;

public class MonsterKillerAchievement implements EventListener {
    Map<String, Integer> eventMap = new HashMap<>();

    private  final int limit;
    public MonsterKillerAchievement(int limit){
        this.limit = limit;
    }

    @Override
    public void onEvent(Event event) {
        int ec = eventMap.getOrDefault(event.source(),0);
        ec++;
        if (ec>=limit){
            System.out.println(event.source()+"获得怪物杀手成就");
            ec = 0;
        }
        eventMap.put((String) event.source(),ec);
    }
}
