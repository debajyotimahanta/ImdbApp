package com.imdb.queue;

public class WorkItem {


    private Runnable task;

    public WorkItem(Runnable task) {

        this.task = task;
    }

    public void run() {
        task.run();
    }
}
