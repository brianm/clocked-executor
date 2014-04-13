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
        count.set(0);
    }

    @Test
    public void testOneOff() throws Exception
    {
        try (ClockedExecutorService clock = new ClockedExecutorService(2))
        {
            clock.schedule(COUNTER, 10, TimeUnit.MILLISECONDS);
            clock.schedule(COUNTER, 5, TimeUnit.MILLISECONDS);
            clock.schedule(COUNTER, 15, TimeUnit.MILLISECONDS);
            clock.schedule(COUNTER, 20, TimeUnit.MILLISECONDS);

            clock.advance(5, TimeUnit.MILLISECONDS).get();
            assertThat(count.get()).isEqualTo(1);

            clock.advance(5, TimeUnit.MILLISECONDS).get();
            assertThat(count.get()).isEqualTo(2);

            clock.advance(2, TimeUnit.MILLISECONDS).get();
            assertThat(count.get()).isEqualTo(2);

            clock.advance(10, TimeUnit.MILLISECONDS).get();
            assertThat(count.get()).isEqualTo(4);
        }
    }

    @Test
    public void testAtFixedInterval() throws Exception
    {
        try (ClockedExecutorService clock = new ClockedExecutorService(2))
        {
            clock.scheduleAtFixedRate(COUNTER, 2, 10, TimeUnit.MILLISECONDS);
            clock.advance(2, TimeUnit.MILLISECONDS).get();
            assertThat(count.get()).isEqualTo(1);

            clock.advance(5, TimeUnit.MILLISECONDS).get();
            assertThat(count.get()).isEqualTo(1);

            clock.advance(5, TimeUnit.MILLISECONDS).get();
            assertThat(count.get()).isEqualTo(2);

            clock.advance(10, TimeUnit.MILLISECONDS).get();
            assertThat(count.get()).isEqualTo(3);
        }
    }

    public static final AtomicLong count = new AtomicLong(0);
    public static final Runnable COUNTER = new Runnable()
    {
        @Override
        public void run()
        {
            count.incrementAndGet();
        }
    };
}
