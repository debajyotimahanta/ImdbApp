package com.imdb.queue;

import com.imdb.exceptions.RetryException;

/**
 * A queue to peform job async. Currently we are using in memory
 * Later we might want to use SQS.
 */
public interface WorkQueue {
    void put(WorkItem entity) throws RetryException;

    WorkItem poll();
}
