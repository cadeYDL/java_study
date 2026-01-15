package design_pattern.observer.demo;

import java.util.HashMap;
import java.util.Map;

public class RevengeSeekerAchievement implements EventListener{
    Map<String,EventCount> eventMap = new HashMap<>();
    private final int killLimiter;
    private final int coinLimiter;
    public RevengeSeekerAchievement(int killLimiter,int coinLimiter){
        this.killLimiter = killLimiter;
        this.coinLimiter = coinLimiter;
    }
    @Override
    public void onEvent(Event event) {
        EventCount ec = eventMap.getOrDefault(event.source(),new EventCount());
        if(event instanceof KillMonsterEvent){
            ec.killCount++;
        }
        if(event instanceof CoinAddEvent){
            ec.coinCount++;
        }
        if(ec.killCount >= killLimiter && ec.coinCount >= coinLimiter){
            System.out.println(event.source() + "获得赏金猎人成就");
            ec.killCount = 0;
            ec.coinCount = 0;
        }
        eventMap.put((String) event.source(),ec);
    }

    class EventCount{
        int killCount;
        int coinCount;
    }
}
