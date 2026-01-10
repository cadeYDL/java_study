package toy.thread_pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.*;

public class MyThreadPool implements ThreadPool {

    private  final BlockingQueue<Runnable> runableTasks;
    private final int maxPoolSize;
    private final int threadPoolSize;
    private final ArrayList<Thread> threadPool;
    private final HashMap<String,Thread> supportMap;
    private final RejectHandler rejectHandler;
    private final long timeout;

    public MyThreadPool(BlockingQueue<Runnable> runableTasks, int maxPoolSize, int threadPoolSize, RejectHandler rejectHandler,long timeout) {
        this.maxPoolSize = maxPoolSize;
        this.threadPoolSize = threadPoolSize;
        this.threadPool = new ArrayList<>(threadPoolSize);
        this.supportMap = new HashMap<>(threadPoolSize);
        this.runableTasks =  Optional.ofNullable(runableTasks).orElse(new ArrayBlockingQueue<>(1024));
        this.rejectHandler = Optional.ofNullable(rejectHandler).orElse((task,MyThreadPool)->{
            throw  new RuntimeException(Thread.currentThread().getName()+"添加任务失败");
        });
        this.timeout = timeout;
    }

    public MyThreadPool(int maxPoolSize, int threadPoolSize, RejectHandler rejectHandler,long timeout) {
        this.maxPoolSize = maxPoolSize;
        this.threadPoolSize = threadPoolSize;
        this.threadPool = new ArrayList<>(threadPoolSize);
        this.supportMap = new HashMap<>(threadPoolSize);
        this.runableTasks =  new ArrayBlockingQueue<>(1024);
        this.rejectHandler = Optional.ofNullable(rejectHandler).orElse((task,MyThreadPool)->{
            throw  new RuntimeException(Thread.currentThread().getName()+"添加任务失败");
        });
        this.timeout = timeout;
    }

    public MyThreadPool(int maxPoolSize, int threadPoolSize,long timeout) {
        this.maxPoolSize = maxPoolSize;
        this.threadPoolSize = threadPoolSize;
        this.threadPool = new ArrayList<>(threadPoolSize);
        this.supportMap = new HashMap<>(maxPoolSize-threadPoolSize);
        this.runableTasks =  new ArrayBlockingQueue<>(3);
        this.rejectHandler = (task,MyThreadPool)->{
            throw  new RuntimeException(Thread.currentThread().getName()+"添加任务失败");
        };
        this.timeout = timeout;
    }

    @Override
    public void execute(Runnable runnable) {
        if (threadPool.size()<this.threadPoolSize){
            Thread thread =new CoreThread();
            this.threadPool.add(thread);
            thread.start();
        }
        if (this.runableTasks.offer(runnable)){
            return;
        }
        if (this.supportMap.size()+this.threadPool.size() <maxPoolSize){
            String key = String.valueOf(System.currentTimeMillis());
            Thread thread = new SupperThread(key);
            this.supportMap.put(key,thread);
            thread.start();
        }
        if (!this.runableTasks.offer(runnable)){
            this.rejectHandler.reject(runnable,this);
        }
    }

    private class CoreThread extends Thread{
        @Override
        public void run() {
            while (true){
                try {
                    Runnable newTask = runableTasks.take();
                    newTask.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private class SupperThread extends Thread{
        String id;
        SupperThread(String inx){
            super("supper"+inx);
            this.id = inx;
        }
        @Override
        public void run() {
            while (true){
                try {
                    Runnable newTask = runableTasks.poll(timeout,TimeUnit.SECONDS);
                    if (newTask==null){
                        supportMap.remove(this);
                        return;
                    }
                    newTask.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
