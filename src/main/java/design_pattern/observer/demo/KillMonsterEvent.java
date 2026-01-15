package design_pattern.observer.demo;

import java.util.HashMap;
import java.util.Map;

public class KillMonsterEvent implements Event {
    private final long time = System.currentTimeMillis();
    private final String user;
    public KillMonsterEvent(String user) {
        this.user = user;
    }
    @Override
    public long getTime() {
        return time;
    }

    @Override
    public Object source() {
        return user;
    }
}
