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

import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class ScheduledFutureTask<V> extends FutureTask<V> implements ScheduledFuture<V>
{
    private final Clock clock;
    private final long when;

    ScheduledFutureTask(final Callable<V> callable, Clock clock, long when)
    {
        super(callable);
        this.clock = clock;
        this.when = when;
    }

    ScheduledFutureTask(final Runnable runnable, Clock clock, long when)
    {
        super(runnable, null);
        this.clock = clock;
        this.when = when;
    }

    @Override
    public long getDelay(final TimeUnit unit)
    {
        return unit.convert(when - clock.getTimeInMillis(), TimeUnit.MILLISECONDS);
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

    @Override
    public boolean equals(final Object obj)
    {
        return this == obj;
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    boolean isReady()
    {
        return clock.getTimeInMillis() >= getWhen();
    }

    protected Clock getClock()
    {
        return clock;
    }

    protected long getWhen()
    {
        return when;
    }
}
