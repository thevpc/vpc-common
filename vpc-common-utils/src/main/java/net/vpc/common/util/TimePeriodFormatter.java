package net.vpc.common.util;

/**
 * Created by vpc on 3/20/17.
 */
public class TimePeriodFormatter /*implements DoubleFormatter*/ {

    private DatePart precision;

    public TimePeriodFormatter(DatePart precision) {
        this.precision = precision;
    }

    public String formatLong(long period) {
        StringBuffer sb = new StringBuffer();
        boolean started = false;
        int h = (int) (period / (1000L * 60L * 60L));
        int mn = (int) ((period % (1000L * 60L * 60L)) / 60000L);
        int s = (int) ((period % 60000L) / 1000L);
        int ms = (int) (period % 1000L);
        if (precision.ordinal() >= DatePart.HOUR.ordinal()) {
            if (h > 0) {
                sb.append(h).append(" h ");
                started = true;
            }
            if (precision.ordinal() >= DatePart.MINUTE.ordinal()) {
                if (mn > 0 || started) {
                    sb.append(mn).append(" mn ");
                    started = true;
                }
                if (precision.ordinal() >= DatePart.SECOND.ordinal()) {
                    if (s > 0 || started) {
                        sb.append(s).append(" s ");
                        //started=true;
                    }
                    if (precision.ordinal() >= DatePart.MILLISECOND.ordinal()) {
                        sb.append(ms).append(" ms");
                    }
                }
            }
        }
        return sb.toString();
    }
}
