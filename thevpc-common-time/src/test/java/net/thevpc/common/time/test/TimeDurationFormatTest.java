package net.thevpc.common.time.test;

import net.thevpc.common.time.DatePart;
import net.thevpc.common.time.TimeDuration;
import org.junit.jupiter.api.Test;

public class TimeDurationFormatTest {
    @Test
    public void test(){
        TimeDuration t = TimeDuration.of(
                0, 0, 1, 0, 0, 0, 0
        );
        System.out.println(t.format(DatePart.NANOSECOND));
        System.out.println(t.formatShort(DatePart.NANOSECOND));
    }
}
