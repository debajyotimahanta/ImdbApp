package imdb;

import com.imdb.exceptions.RetryException;
import com.imdb.queue.WorkItem;
import com.imdb.queue.WorkQueue;

import java.util.concurrent.ConcurrentLinkedQueue;

public class FakeQueue implements WorkQueue {
    ConcurrentLinkedQueue<WorkItem> queue;

    public FakeQueue() {
        queue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void put(WorkItem entity) throws RetryException {
        queue.add(entity);
    }

    @Override
    public WorkItem poll() {
        return queue.poll();
    }

    public ConcurrentLinkedQueue<WorkItem> getQueue() {
        return queue;
    }
}
