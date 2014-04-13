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

class RecurringScheduledFutureTask<V> extends ScheduledFutureTask<V>
{
    private final Scheduler scheduler;
    private final Runnable command;
    private final long period;

    public RecurringScheduledFutureTask(Scheduler scheduler,
                                        final Runnable command,
                                        final Clock clock,
                                        final long when,
                                        final long period)
    {
        super(command, clock, when);
        this.scheduler = scheduler;
        this.command = command;
        this.period = period;
    }

    @Override
    public void run()
    {
        scheduler.add(new RecurringScheduledFutureTask<>(scheduler,
                                                         command,
                                                         getClock(),
                                                         getWhen() + period,
                                                         period));
        super.run();
    }

    @Override
    public boolean equals(final Object obj)
    {
        return super.equals(obj);
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }
}
