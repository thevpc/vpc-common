package net.thevpc.common.time;

/**
 * Created by vpc on 3/20/17.
 */
public interface TimeDurationFormat {

    String formatMillis(long periodMillis);

    String formatNanos(long periodNano);

    String format(long millis,int nanos);

}
