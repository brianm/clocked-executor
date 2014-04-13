package org.skife.clocked;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ClockedExecutorService extends ThreadPoolExecutor implements ScheduledExecutorService, AutoCloseable
{
    private final Clock clock = new Clock();
    private final Scheduler scheduler = new Scheduler(clock);

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
            return scheduler.add(new ScheduledFutureTask<>(command, clock, clock.getMillis() + unit.toMillis(delay)));
        }
    }

    @Override
    public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit)
    {
        synchronized (scheduler) {
            return scheduler.add(new ScheduledFutureTask<>(callable, clock, clock.getMillis() + unit.toMillis(delay)));
        }
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit)
    {
        synchronized (scheduler) {
            return scheduler.add(new FixedRateScheduledFutureTask(scheduler,
                                                                  command,
                                                                  clock,
                                                                  clock.getMillis() + unit.toMillis(initialDelay),
                                                                  unit.toMillis(period)));
        }
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit)
    {
        throw new UnsupportedOperationException("Not Yet Implemented!");
    }

    @Override
    public void close()
    {
        this.shutdownNow();
    }
}
