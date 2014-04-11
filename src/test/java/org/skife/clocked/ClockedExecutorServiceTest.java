package org.skife.clocked;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

public class ClockedExecutorServiceTest
{
    @Before
    public void setUp() throws Exception
    {
        counter.set(0);
    }

    @Test
    public void testOneOff() throws Exception
    {
        try (ClockedExecutorService clock = new ClockedExecutorService(2))
        {
            clock.schedule(NOOP, 10, TimeUnit.MILLISECONDS);
            clock.schedule(NOOP, 5, TimeUnit.MILLISECONDS);
            clock.schedule(NOOP, 15, TimeUnit.MILLISECONDS);
            clock.schedule(NOOP, 20, TimeUnit.MILLISECONDS);

            clock.advance(5, TimeUnit.MILLISECONDS).get();
            assertThat(counter.get()).isEqualTo(1);

            clock.advance(5, TimeUnit.MILLISECONDS).get();
            assertThat(counter.get()).isEqualTo(2);

            clock.advance(2, TimeUnit.MILLISECONDS).get();
            assertThat(counter.get()).isEqualTo(2);

            clock.advance(10, TimeUnit.MILLISECONDS).get();
            assertThat(counter.get()).isEqualTo(4);
        }
    }

    @Test
    public void testAtFixedInterval() throws Exception
    {
        try (ClockedExecutorService clock = new ClockedExecutorService(2))
        {
            clock.scheduleAtFixedRate(NOOP, 5, 5, TimeUnit.MILLISECONDS);
            clock.advance(7, TimeUnit.MILLISECONDS);
        }
    }

    public static final AtomicLong counter = new AtomicLong(0);

    public static final Runnable NOOP = new Runnable()
    {
        @Override
        public synchronized void run()
        {
            counter.incrementAndGet();
        }
    };
}
