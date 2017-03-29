# Test-Oriented Scheduled Executor
[![Maven Central](https://img.shields.io/maven-central/v/org.skife/clocked-executor.svg?label=Maven%20Central)](https://search.maven.org/#search%7Cga%7C1%7Ca%3Aclocked-executor%20g%3Aorg.skife)


ScheduledExecutorService implementation designed for deterministic testing. It runs off an
internal clock which must be manually advanced via the <code>ClockedExecutorService#advance</code>
method.

Additionally, for recurring scheduled tasks, there is a guarantee that the task will only
be called once for each call to <code>advance(...)</code>. This is important to note as it
rather violates the normal contracts, but is very useful for testing.

See this [example](src/test/java/org/skife/clocked/ClockedExecutorServiceTest.java).
