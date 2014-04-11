package org.skife.clocked;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ClockedExecutorService extends ThreadPoolExecutor implements ScheduledExecutorService, AutoCloseable
{
    private final Scheduler scheduler = new Scheduler();

    public ClockedExecutorService(final int corePoolSize)
    {
        super(corePoolSize, corePoolSize, 365L, TimeUnit.DAYS, new LinkedBlockingQueue<Runnable>());
    }

    public Future<?> advance(long time, TimeUnit unit) throws ExecutionException, InterruptedException
    {
        synchronized (scheduler) {
            List<ScheduledFutureTask<?>> runnables = scheduler.advance(unit.toMillis(time));
            for (ScheduledFutureTask<?> runnable : runnables) {
                this.submit(runnable);
            }
            return new CompositeFuture(runnables);
        }
    }

    @Override
    public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit)
    {
        synchronized (scheduler) {
            return scheduler.addOnce(command, delay, unit);
        }
    }

    @Override
    public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit)
    {
        synchronized (scheduler) {
            return scheduler.addOnce(callable, delay, unit);
        }
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit)
    {
        synchronized (scheduler) {
            return scheduler.addAtFixedRate(command, initialDelay, period, unit);
        }
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit)
    {
        synchronized (scheduler) {
            return scheduler.addWithFixedDelay(command, initialDelay, delay, unit);
        }
    }

    @Override
    public void close()
    {
        this.shutdownNow();
    }
}
