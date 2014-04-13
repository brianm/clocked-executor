package org.skife.clocked;

class Clock
{
    private volatile long time = 0;

    synchronized long getTimeInMillis()
    {
        return time;
    }

    synchronized long advanceMillis(long time)
    {
        time += time;
        return time;
    }
}
