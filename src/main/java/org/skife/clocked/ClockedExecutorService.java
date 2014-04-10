package org.skife.clocked;

import java.time.Duration;
import java.time.Instant;
import java.util.SortedSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ClockedExecutorService extends ThreadPoolExecutor implements ScheduledExecutorService
{

    private final AtomicReference<Instant> time = new AtomicReference<>(Instant.now());
    private final SortedSet<FutureTask> queued = new ConcurrentSkipListSet<>();
    private final Object lock = new Object();

    public ClockedExecutorService(final int corePoolSize,
                                  final int maximumPoolSize,
                                  final long keepAliveTime,
                                  final TimeUnit unit,
                                  final BlockingQueue<Runnable> workQueue,
                                  final ThreadFactory threadFactory)
    {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public Future<?> advance(Duration d) {
        synchronized (lock) {

        }
        throw new UnsupportedOperationException("Not Yet Implemented!");
    }

    @Override
    public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit)
    {
        synchronized (lock) {

        }
        return null;
    }

    @Override
    public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit)
    {
        synchronized (lock) {

        }
        return null;
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit)
    {
        synchronized (lock) {

        }
        return null;
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit)
    {
        synchronized (lock) {

        }
        return null;
    }

    private static class ScheduledTask
    {

    }


}
