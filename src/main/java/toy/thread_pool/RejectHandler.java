package toy.thread_pool;

public interface RejectHandler {
    void reject(Runnable readable,ThreadPool myThreadPool);
}
