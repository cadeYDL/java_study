package toy.time_wheel;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Stream;

public class TimeWheel {
    private long startTime;
    private final long timeInterval;
    private final MpscTaskQueue<DelayTask>[] slots;
    private final Trigger taskTrigger;
    private final ExecutorService taskRunner;
    private final AtomicBoolean running= new AtomicBoolean();

    public TimeWheel(long timeInterval, int size,int runnerCount) {
        this.timeInterval = timeInterval;
        this.slots = Stream.generate(() -> new MpscTaskQueue<DelayTask>())
                .limit(size)
                .toArray(MpscTaskQueue[]::new);

        this.taskTrigger = new Trigger();
        this.taskRunner = Executors.newFixedThreadPool(runnerCount);
    }

    public void addDelayTask(Runnable task, long deleyTimeMs){
        start();
        DelayTask dTask = new DelayTask(task,deleyTimeMs);
        int inx = Math.toIntExact((dTask.getDeadline()-startTime)/timeInterval%slots.length);
        MpscTaskQueue<DelayTask> slot = slots[inx];
        slot.push(dTask);
    }

    private void start(){
        if (running.compareAndSet(false,true)){
            startTime = System.currentTimeMillis();
            taskTrigger.start();
        }
    }

    public void stop(){
        if (running.compareAndSet(true,false)){
            startTime = 0;
            LockSupport.unpark(taskTrigger);
        }
    }



    private class DelayTask{
        @Getter
        private long deadline;
        private Runnable task;

        public DelayTask(Runnable task, long deleyTimeMs) {
            this.deadline = System.currentTimeMillis()+deleyTimeMs;
            this.task = task;
        }
    }

    private class Trigger extends Thread{
        int tickCount = 0;
        @Override
        public void run(){
            while (running.get()){
                tickCount++;
                final long tickTime = startTime+tickCount*timeInterval;
                while (System.currentTimeMillis()<tickTime){
                    LockSupport.parkUntil(tickTime);
                    if(!running.get()){
                        return;
                    }
                }
                int inx = tickCount % slots.length;
                MpscTaskQueue<DelayTask> slot = slots[inx];
                List<DelayTask> tasks = slot.getAndRemoveTasks((task)-> task.getDeadline()<tickTime);
                tasks.forEach((task)->{
                    taskRunner.execute(task.task);
                });

            }

        }
    }




}
