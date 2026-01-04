package tech.cornjob;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.LockSupport;

public class ScheduleService {
    private final ExecutorService executor;
    private final Trigger trigger;
    public ScheduleService(ExecutorService executor){
        this.executor = Optional.ofNullable(executor).orElse(Executors.newFixedThreadPool(6));
        this.trigger = new Trigger();
    }
    void schedule(Runnable task,String jobName,long delayMS,boolean cycle) {
        Job job = new Job(delayMS,task,cycle,jobName);
        this.trigger.Offer(job);
    }

    private class Trigger{
        PriorityBlockingQueue<Job> queue = new PriorityBlockingQueue<>(10);
        Thread trigger = new Thread(()->{
            while (true){
                while(queue.isEmpty()){
                    LockSupport.park();
                }
                Job job = queue.peek();
                if(job.canStart()){
                    job = queue.poll();
                    Runnable task = job.getTask();
                    executor.execute(task);
                    Job nextJob = job.GetNextJob();
                    if (nextJob!=null){
                        queue.offer(nextJob);
                    }
                }else{
                    LockSupport.parkUntil(job.waitTime());
                }
            }

        });
        {
            trigger.start();
            System.out.println("触发器！启动！");
        }

        void Offer(Job job){
            queue.add(job);
            wakeUp();
        }

        void wakeUp(){
            LockSupport.unpark(trigger);
        }
    }
}
