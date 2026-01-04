package tech.aqs;

import java.util.ArrayList;

public class Main {
    static int count = 0;
    public static void main(String[] args) {
        MyAQSLock lock = new MyAQSLock();
        ArrayList<Thread> ts = new ArrayList<>();
        int size = 1000;
        for(int i=0;i<size;i++){
           ts.add(new Thread(()->{
                try {
                    Thread.sleep(1000);
                    lock.lock();
                    lock.lock();
                    count++;
                    lock.unlock();
                    lock.unlock();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }));
        }
        ts.forEach(Thread::start);
        for (Thread t : ts) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(count+"=="+size);
    }
}
