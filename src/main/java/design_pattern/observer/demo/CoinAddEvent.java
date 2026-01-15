package design_pattern.observer.demo;

public class CoinAddEvent implements Event{
    private final long time = System.currentTimeMillis();
    private final String user;
    public CoinAddEvent(String user) {
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
