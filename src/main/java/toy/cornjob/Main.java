package toy.cornjob;

public class Main {

    public static void main(String... args) {
        ScheduleService scheduleService = new ScheduleService(null);
        long start = System.currentTimeMillis();
        scheduleService.schedule(()->System.out.println(System.currentTimeMillis()+":"+1),"100ms",100,false);
        long end = System.currentTimeMillis();
        System.out.println(end-start);

        scheduleService.schedule(()->System.out.println(System.currentTimeMillis()+":"+2),"100ms+cycle",1000,true);
        scheduleService.schedule(()->System.out.println(System.currentTimeMillis()+":"+3),"100ms+cycle",10000,true);
        scheduleService.schedule(()->System.out.println(System.currentTimeMillis()+":"+4),"100ms+cycle",20000,true);
        scheduleService.schedule(()->System.out.println(System.currentTimeMillis()+":"+5),"100ms+cycle",30000,true);

    }
}
