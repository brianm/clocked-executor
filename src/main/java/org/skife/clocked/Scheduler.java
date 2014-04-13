package org.skife.clocked;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ScheduledFuture;

class Scheduler
{
    private volatile TreeSet<ScheduledFutureTask<?>> tasks = new TreeSet<>();
    private final Clock clock;

    public Scheduler(final Clock clock)
    {
        this.clock = clock;
    }

    public <V> ScheduledFuture<V> add(final ScheduledFutureTask<V> task)
    {
        tasks.add(task);
        return task;
    }

    public List<ScheduledFutureTask<?>> advance(final long millis)
    {
        clock.advanceMillis(millis);

        TreeSet<ScheduledFutureTask<?>> new_tasks = new TreeSet<>();
        new_tasks.addAll(this.tasks);

        List<ScheduledFutureTask<?>> to_run = new ArrayList<>();
        Iterator<ScheduledFutureTask<?>> itty = new_tasks.iterator();
        while (itty.hasNext()) {
            ScheduledFutureTask<?> t = itty.next();
            if (t.isReady()) {
                to_run.add(t);
                itty.remove();
            }
            else
            {
                break;
            }
        }

        this.tasks = new_tasks;
        return to_run;
    }
}
