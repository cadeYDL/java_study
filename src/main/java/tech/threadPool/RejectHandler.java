package tech.threadPool;

public interface RejectHandler {
    void reject(Runnable readable,ThreadPool myThreadPool);
}
