package net.thevpc.common.time;

/**
 * Created by vpc on 3/20/17.
 */
public interface TimePeriodFormat {

    String formatMillis(long periodMillis);

    String formatNanos(long periodNano);

}
