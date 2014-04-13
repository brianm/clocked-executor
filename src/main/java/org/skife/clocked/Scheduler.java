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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ScheduledFuture;

class Scheduler
{
    private volatile TreeSet<ScheduledFutureTask<?>> tasks = new TreeSet<>();
    private final Clock clock;

    public Scheduler(final Clock clock)
    {
        this.clock = clock;
    }

    public <V> ScheduledFuture<V> add(final ScheduledFutureTask<V> task)
    {
        tasks.add(task);
        return task;
    }

    public List<ScheduledFutureTask<?>> advance(final long millis)
    {
        clock.advanceMillis(millis);

        TreeSet<ScheduledFutureTask<?>> new_tasks = new TreeSet<>();
        new_tasks.addAll(this.tasks);

        List<ScheduledFutureTask<?>> to_run = new ArrayList<>();
        Iterator<ScheduledFutureTask<?>> itty = new_tasks.iterator();
        while (itty.hasNext()) {
            ScheduledFutureTask<?> t = itty.next();
            if (t.isReady()) {
                to_run.add(t);
                itty.remove();
            }
            else
            {
                break;
            }
        }

        this.tasks = new_tasks;
        return to_run;
    }
}
