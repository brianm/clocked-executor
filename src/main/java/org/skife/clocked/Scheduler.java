package org.skife.clocked;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class Scheduler
{
    private volatile TreeSet<ScheduledFutureTask<?>> tasks = new TreeSet<>();
    private final Clock clock = new Clock();

    public synchronized ScheduledFuture<?> addOnce(final Runnable command, final long delay, final TimeUnit unit)
    {
        ScheduledFutureTask<?> task = new ScheduledFutureTask<>(command, null, clock, unit.toMillis(delay));
        tasks.add(task);
        return task;
    }

    public synchronized <V> ScheduledFuture<V> addOnce(final Callable<V> callable, final long delay, final TimeUnit unit)
    {
        ScheduledFutureTask<V> task = new ScheduledFutureTask<>(callable, clock, unit.toMillis(delay));
        tasks.add(task);
        return task;
    }

    public synchronized ScheduledFuture<?> addAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit)
    {
        I probbaly need to build this in the executor service and then pass Scheduled thing here, then
        I can rescheudle same against the scheduler safely. Pushing the creation of ScheduledFutureTask down to
        the scheduler is making rescheduling not possible.
        ScheduledFutureTask<?> task = new FixedRateScheduledFutureTask<>(this,
                                                                         command,
                                                                         null,
                                                                         clock,
                                                                         unit.toMillis(initialDelay),
                                                                         unit.toMillis(period));
        tasks.add(task);
        return task;
    }

    public synchronized ScheduledFuture<?> addWithFixedDelay(final Runnable command, final long delay, final long delay1, final TimeUnit unit)
    {
        throw new UnsupportedOperationException("Not Yet Implemented!");
    }

    public synchronized List<ScheduledFutureTask<?>> advance(final long millis)
    {
        clock.advance(millis);
        // advance() totally screws up the sort order, so let's clean it up with a new set :-)
        TreeSet<ScheduledFutureTask<?>> new_tasks = new TreeSet<>();
        new_tasks.addAll(this.tasks);

        Iterator<ScheduledFutureTask<?>> itty = new_tasks.iterator();
        List<ScheduledFutureTask<?>> to_run = new ArrayList<>();
        while (itty.hasNext()) {
            ScheduledFutureTask<?> t = itty.next();
            if (t.isReady()) {
                to_run.add(t);
                itty.remove();
            }
            else
            {
                return to_run;
            }
        }

        this.tasks = new_tasks;
        return to_run;
    }
}
