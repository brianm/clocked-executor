package org.skife.clocked;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class CompositeFuture implements Future<Void>
{
    private final Iterable<? extends Future<?>> futures;

    CompositeFuture(final Iterable<? extends Future<?>> futures) {this.futures = futures;}

    @Override
    public boolean cancel(final boolean mayInterruptIfRunning)
    {
        throw new UnsupportedOperationException("Not Yet Implemented!");
    }

    @Override
    public boolean isCancelled()
    {
        throw new UnsupportedOperationException("Not Yet Implemented!");
    }

    @Override
    public boolean isDone()
    {
        throw new UnsupportedOperationException("Not Yet Implemented!");
    }

    @Override
    public Void get() throws InterruptedException, ExecutionException
    {
        for (Future<?> future : futures) {
            future.get();
        }
        return null;
    }

    @Override
    public Void get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
    {
        long start = System.currentTimeMillis();
        long duration = unit.toMillis(timeout);
        long until = start + duration;
        for (Future<?> future : futures) {
            long now = System.currentTimeMillis();
            if (now >= until) {
                throw new InterruptedException();
            }
            future.get(until - now, TimeUnit.MILLISECONDS);
        }
        return null;
    }
}
