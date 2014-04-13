/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

/**
 * <p>NOT INTENDED FOR USE OUTSIDE CAREFULLY CONTROLLED TESTS</p>
 * <p>
 * ScheduledExecutorService implementation designed for deterministic testing. It runs off an
 * internal clock which must be manually advanced via the {@see ClockedExecutorService#advance}
 * method.
 * </p>
 * <p>
 * Additionally, for recurring scheduled tasks, there is a guarantee that the task will nly
 * be called once for each call to <code>advance(...)</code>. This is important to note as it
 * rather violates the normal contracts, but is very useful for testing.
 * </p>
 */
public class ClockedExecutorService extends ThreadPoolExecutor implements ScheduledExecutorService, AutoCloseable
{
    private final Clock clock;
    private final Scheduler scheduler;

    public ClockedExecutorService(final int corePoolSize)
    {
        super(corePoolSize, corePoolSize, 365L, TimeUnit.DAYS, new LinkedBlockingQueue<Runnable>());
        this.clock = new Clock();
        this.scheduler = new Scheduler(clock);
    }

    public ClockedExecutorService()
    {
        this(1);
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
            return scheduler.add(new ScheduledFutureTask<>(command,
                                                           clock,
                                                           clock.getTimeInMillis() + unit.toMillis(delay)));
        }
    }

    @Override
    public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit)
    {
        synchronized (scheduler) {
            return scheduler.add(new ScheduledFutureTask<>(callable,
                                                           clock,
                                                           clock.getTimeInMillis() + unit.toMillis(delay)));
        }
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit)
    {
        return scheduleRecurring(new RecurringScheduledFutureTask(scheduler,
                                                                  command,
                                                                  clock,
                                                                  clock.getTimeInMillis() + unit.toMillis(initialDelay),
                                                                  unit.toMillis(period)));

    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit)
    {
        return scheduleRecurring(new RecurringScheduledFutureTask(scheduler,
                                                                  command,
                                                                  clock,
                                                                  clock.getTimeInMillis() + unit.toMillis(initialDelay),
                                                                  unit.toMillis(delay)));
    }

    private ScheduledFuture<?> scheduleRecurring(final RecurringScheduledFutureTask task)
    {
        synchronized (scheduler) {
            return scheduler.add(task);
        }
    }

    @Override
    public void close()
    {
        this.shutdownNow();
    }
}
