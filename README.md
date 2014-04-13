# Test-Oriented Scheduled Executor

ScheduledExecutorService implementation designed for deterministic testing. It runs off an
internal clock which must be manually advanced via the <code>ClockedExecutorService#advance</code>
method.

Additionally, for recurring scheduled tasks, there is a guarantee that the task will nly
be called once for each call to <code>advance(...)</code>. This is important to note as it
rather violates the normal contracts, but is very useful for testing.

* [Example](https://github.com/brianm/clocked-executor/blob/master/src/test/java/org/skife/clocked/ClockedExecutorServiceTest.java)
* [Maven](http://search.maven.org/#search%7Cga%7C1%7Ca%3Aclocked-executor%20g%3Aorg.skife)
