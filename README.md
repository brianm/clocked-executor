# Testing-Oriented implementation of ScheduledExecutorService

ScheduledExecutorService implementation designed for deterministic testing. It runs off an
internal clock which must be manually advanced via the <code>ClockedExecutorService#advance</code>
method.

Additionally, for recurring scheduled tasks, there is a guarantee that the task will nly
be called once for each call to <code>advance(...)</code>. This is important to note as it
rather violates the normal contracts, but is very useful for testing.


