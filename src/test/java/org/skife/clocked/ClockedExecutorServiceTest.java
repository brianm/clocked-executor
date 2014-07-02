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
        ClockedExecutorService executor = new ClockedExecutorService();
        try {
            executor.schedule(COUNTER, 10, TimeUnit.MILLISECONDS);
            executor.schedule(COUNTER, 5, TimeUnit.MILLISECONDS);
            executor.schedule(COUNTER, 15, TimeUnit.MILLISECONDS);
            executor.schedule(COUNTER, 20, TimeUnit.MILLISECONDS);

            executor.advance(5, TimeUnit.MILLISECONDS).get();
            assertThat(count.get()).isEqualTo(1);

            executor.advance(5, TimeUnit.MILLISECONDS).get();
            assertThat(count.get()).isEqualTo(2);

            executor.advance(2, TimeUnit.MILLISECONDS).get();
            assertThat(count.get()).isEqualTo(2);

            executor.advance(10, TimeUnit.MILLISECONDS).get();
            assertThat(count.get()).isEqualTo(4);
        }
        finally {
            executor.close();
        }
    }

    @Test
    public void testAtFixedInterval() throws Exception
    {
        ClockedExecutorService clock = new ClockedExecutorService();
        try {
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
        finally {
            clock.close();
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
