package toy.time_wheel;

public class Main {
    public static void main(String[] args) {
        TimeWheel timerWheel = new TimeWheel(100,10,1);
        for (int i = 0; i < 100; i++) {
            final int fi = i;
            timerWheel.addDelayTask(() -> {
                System.out.println(fi);
            }, 1000L * i);
        }
    }
}
