package org.skife.clocked;

class FixedRateScheduledFutureTask<V> extends ScheduledFutureTask<V>
{
    private final Scheduler scheduler;
    private final Runnable command;
    private final long period;

    public FixedRateScheduledFutureTask(Scheduler scheduler,
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
        scheduler.add(new FixedRateScheduledFutureTask<>(scheduler,
                                                         command,
                                                         getClock(),
                                                         getWhen() + period,
                                                         period));
        super.run();
    }
}
