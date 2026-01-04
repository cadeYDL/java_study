package tech.cornjob;

class Job implements Comparable<Job>{
    private final String jobName;
    private final long startTime;
    private final long delayMs;
    private final Runnable task;
    private final boolean cycle;
    protected Job(long delayMs, Runnable task, boolean cycle,String jobName) {
        this.startTime = System.currentTimeMillis()+delayMs;
        this.delayMs = delayMs;
        this.task = task;
        this.cycle = cycle;
        this.jobName = jobName;
    }
    @Override
    public int compareTo(Job o) {
        return this.startTime<o.startTime ? -1 : (this.startTime==o.startTime ? 0 : 1);
    }


    public boolean canStart() {
        return startTime < System.currentTimeMillis();
    }

    public Runnable getTask() {
        return task;
    }

    public Job GetNextJob() {
        if (!this.cycle){
            return null;
        }
        Job nextJob = new Job(delayMs, task, cycle,jobName);
        return nextJob;
    }

    public long waitTime() {
        return startTime;
    }
}
