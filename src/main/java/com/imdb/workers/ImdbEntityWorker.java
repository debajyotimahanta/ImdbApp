package com.imdb.workers;

import com.imdb.queue.WorkItem;
import com.imdb.queue.WorkQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImdbEntityWorker {
    private final ExecutorService executorPool;
    private final int size;
    private WorkQueue queue;

    public ImdbEntityWorker(WorkQueue queue, int size) {
        this.queue = queue;
        this.size = size;
        this.executorPool = Executors.newFixedThreadPool(size);
    }

    public void start() {
        for (int i = 0; i < size; i++) {
            executorPool.execute(new QueuePoller(queue));
        }
    }

    class QueuePoller implements Runnable {

        private WorkQueue queue;

        public QueuePoller(WorkQueue queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                WorkItem work = queue.poll();
                if (work != null) work.run();
            }
        }
    }


}
