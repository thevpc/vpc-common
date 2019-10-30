package net.vpc.common.util;

/**
 * Created by vpc on 3/20/17.
 */
public class SimpleTimePeriodFormat implements TimePeriodFormat {
    public static final TimePeriodFormat INSTANCE = new SimpleTimePeriodFormat();

    @Override
    public String formatMillis(long periodMillis) {
        return formatNanos(periodMillis * 1000000);
    }

    @Override
    public String formatNanos(long periodNano) {
        StringBuilder sb = new StringBuilder();
        int nano = (int) (periodNano % 1000000);
        long period = periodNano / 1000000;
        boolean started = false;
        int h = (int) (period / (1000L * 60L * 60L));
        int mn = (int) ((period % (1000L * 60L * 60L)) / 60000L);
        int s = (int) ((period % 60000L) / 1000L);
        int ms = (int) (period % 1000L);

        if (h > 0) {
            sb.append(_StringUtils.formatRight(h,2)).append("h ");
            started = true;
        }
        if (mn > 0 || started) {
            sb.append(_StringUtils.formatRight(mn,2)).append("mn ");
            started = true;
        }
        if (s > 0 || started) {
            sb.append(_StringUtils.formatRight(s,2)).append("s ");
            //started=true;
        }
        sb.append(_StringUtils.formatRight(ms,3)).append("ms");

        if (ms < 10) {
            sb.append(" ").append(_StringUtils.formatRight(nano,6)).append("nanos");
        }
        return sb.toString();
    }

}
