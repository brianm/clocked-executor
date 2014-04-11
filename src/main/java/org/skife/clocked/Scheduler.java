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
    private final TreeSet<ScheduledFutureTask<?>> tasks = new TreeSet<>();
    private final Clock clock = new Clock();

    public ScheduledFuture<?> addOnce(final Runnable command, final long delay, final TimeUnit unit)
    {
        ScheduledFutureTask<?> task = new ScheduledFutureTask<>(command, null, clock, unit.toMillis(delay), ScheduleType.once);
        tasks.add(task);
        return task;
    }

    public <V> ScheduledFuture<V> addOnce(final Callable<V> callable, final long delay, final TimeUnit unit)
    {
        ScheduledFutureTask<V> task = new ScheduledFutureTask<>(callable, clock, unit.toMillis(delay), ScheduleType.once);
        tasks.add(task);
        return task;
    }

    public ScheduledFuture<?> addAtFixedRate(final Runnable command, final long delay, final long period, final TimeUnit unit)
    {
        ScheduledFutureTask<?> task = new ScheduledFutureTask<>(command, null, clock, unit.toMillis(delay), ScheduleType.rate);
        tasks.add(task);
        return task;
    }

    public ScheduledFuture<?> addWithFixedDelay(final Runnable command, final long delay, final long delay1, final TimeUnit unit)
    {
        throw new UnsupportedOperationException("Not Yet Implemented!");
    }

    public List<ScheduledFutureTask<?>> advance(final long millis)
    {
        clock.advance(millis);

        Iterator<ScheduledFutureTask<?>> itty = tasks.iterator();
        List<ScheduledFutureTask<?>> tasks = new ArrayList<>();
        while (itty.hasNext()) {
            ScheduledFutureTask<?> t = itty.next();
            if (t.isReady()) {
                switch(t.getType()) {
                    case once:
                        tasks.add(t);
                        break;
                    case rate:
                    case delay:
                    default:
                        throw new UnsupportedOperationException("Not Yet Implemented!");
                }
                itty.remove();
            }
            else {
                return tasks;
            }
        }
        return tasks;
    }
}
