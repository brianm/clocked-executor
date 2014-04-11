package org.skife.clocked;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

class FixedRateScheduledFutureTask<V> extends ScheduledFutureTask<V>
{
    private final Scheduler scheduler;
    private final long interval;

    FixedRateScheduledFutureTask(Scheduler scheduler,
                                 final Callable<V> callable,
                                 final Clock clock,
                                 final long initialDelay,
                                 final long interval)
    {
        super(callable, clock, initialDelay);
        this.scheduler = scheduler;
        this.interval = interval;
    }

    FixedRateScheduledFutureTask(Scheduler scheduler,
                                 final Runnable runnable,
                                 final V result,
                                 final Clock clock,
                                 final long initialDelay,
                                 final long interval)
    {
        super(runnable, result, clock, initialDelay);
        this.scheduler = scheduler;
        this.interval = interval;
    }

    @Override
    public void run()
    {
        scheduler.addAtFixedRate(this, null, interval, interval, TimeUnit.MILLISECONDS);
        super.run();
    }
}
