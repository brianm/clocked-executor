package org.skife.clocked;

import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeTest
{
    @Test
    public void testInstantComparison() throws Exception
    {
        Instant zero = Instant.ofEpochSecond(0);
        Instant one = Instant.ofEpochSecond(1);

        assertThat(zero.isBefore(one)).isTrue();
    }

    @Test
    public void testTimeUnitTemporalUnityCOnversions() throws Exception
    {

    }
}
