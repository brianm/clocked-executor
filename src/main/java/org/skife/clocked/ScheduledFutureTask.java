package org.skife.clocked;

import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class ScheduledFutureTask<V> extends FutureTask<V> implements ScheduledFuture<V>
{
    private final Clock clock;
    private final long when;

    ScheduledFutureTask(final Callable<V> callable, Clock clock, long delay)
    {
        super(callable);
        this.clock = clock;
        this.when = delay;
    }

    ScheduledFutureTask(final Runnable runnable, final V result, Clock clock, long delay)
    {
        super(runnable, result);
        this.clock = clock;
        this.when = delay;
    }

    @Override
    public long getDelay(final TimeUnit unit)
    {
        return unit.convert(when - clock.getMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(final Delayed o)
    {
        long diff = getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
        if (diff == 0) {
            return 0;
        }
        else if (diff > 0) {
            return 1;
        }
        else
        {
            return -1;
        }
    }

    public boolean isReady()
    {
        return clock.getMillis() >= when;
    }
}
