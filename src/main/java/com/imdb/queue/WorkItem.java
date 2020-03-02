package com.imdb.queue;

/**
 * A work item which can be executed async once submitted to {@link WorkQueue}
 * by {@link com.imdb.workers.ImdbEntityWorker}. This will work for in-memory queue
 * For using SQS or other of process queue we might have to make the task {@link java.io.Serializable}
 */
public class WorkItem {
    private Runnable task;

    public WorkItem(Runnable task) {

        this.task = task;
    }

    public void run() {
        task.run();
    }
}
