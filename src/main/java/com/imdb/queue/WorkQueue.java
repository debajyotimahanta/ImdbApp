package com.imdb.queue;

import com.imdb.exceptions.RetryException;

public interface WorkQueue {
    void put(WorkItem entity) throws RetryException;

    WorkItem poll();
}
