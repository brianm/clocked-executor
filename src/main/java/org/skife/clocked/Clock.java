package org.skife.clocked;

class Clock
{
    private volatile long time = 0;

    long getMillis()
    {
        return time;
    }

    long advance(long millis)
    {
        time += millis;
        return time;
    }

}
