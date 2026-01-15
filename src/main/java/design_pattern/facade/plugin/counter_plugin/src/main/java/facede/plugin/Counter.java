package facede.plugin;

import java.util.concurrent.atomic.AtomicInteger;
import facede.plugin.MyPlugin;

public class Counter implements MyPlugin {
    private AtomicInteger count = new AtomicInteger(0);
    @Override
    public void beforeGetTime() {
        System.out.println("beforeGetTime"+count.getAndIncrement());
    }
}
