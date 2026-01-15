package design_pattern.observer.demo;

public class Main {
    public static void main(String[] args) {
        EventBus eb = new Bus();
        eb.register(new MonsterKillerAchievement(3),KillMonsterEvent.class);
        EventListener rsa = new RevengeSeekerAchievement(1,2);
        eb.register(rsa,KillMonsterEvent.class);
        eb.register(rsa,CoinAddEvent.class);
        eb.register(new MonopolyAchievement(5),KillMonsterEvent.class);

        for (int i=10;i>=0;i--){
            eb.publish(new KillMonsterEvent("ydl"));
            eb.publish(new CoinAddEvent("ydl"));
        }


    }
}
