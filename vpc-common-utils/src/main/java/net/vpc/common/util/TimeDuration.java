package net.vpc.common.util;

public class TimeDuration {
    public static final TimeDuration ZERO = new TimeDuration(0);
    private long nanos;

    public TimeDuration(long nanos) {
        this.nanos = nanos;
    }

    public static String formatPeriodMilli(long period, DatePart precision) {
        StringBuilder sb = new StringBuilder();
        boolean started = false;
        int h = (int) (period / (1000L * 60L * 60L));
        int mn = (int) ((period % (1000L * 60L * 60L)) / 60000L);
        int s = (int) ((period % 60000L) / 1000L);
        int ms = (int) (period % 1000L);
        if (precision.ordinal() >= DatePart.HOUR.ordinal()) {
            if (h > 0) {
                sb.append(_StringUtils.formatRight(h, 2)).append("h ");
                started = true;
            }
            if (precision.ordinal() >= DatePart.MINUTE.ordinal()) {
                if (mn > 0 || started) {
                    sb.append(_StringUtils.formatRight(mn, 2)).append("mn ");
                    started = true;
                }
                if (precision.ordinal() >= DatePart.SECOND.ordinal()) {
                    if (s > 0 || started) {
                        sb.append(_StringUtils.formatRight(s, 2)).append("s ");
                        //started=true;
                    }
                    if (precision.ordinal() >= DatePart.MILLISECOND.ordinal()) {
                        sb.append(_StringUtils.formatRight(ms, 3)).append("ms");
                    }
                }
            }
        }
        return sb.toString();
    }

    public static String formatPeriodMilli(long period) {
        StringBuilder sb = new StringBuilder();
        boolean started = false;
        int h = (int) (period / (1000L * 60L * 60L));
        int mn = (int) ((period % (1000L * 60L * 60L)) / 60000L);
        int s = (int) ((period % 60000L) / 1000L);
        int ms = (int) (period % 1000L);
        if (h > 0) {
            sb.append(_StringUtils.formatRight(h, 2)).append("h ");
            started = true;
        }
        if (mn > 0 || started) {
            sb.append(_StringUtils.formatRight(mn, 2)).append("mn ");
            started = true;
        }
        if (s > 0 || started) {
            sb.append(_StringUtils.formatRight(s, 2)).append("s ");
            //started=true;
        }
        sb.append(_StringUtils.formatRight(ms, 3)).append("ms");
        return sb.toString();
    }

    public long toNanoSeconds() {
        return getTime();
    }

    public long getTime() {
        return nanos;
    }

    public String toString() {
        return formatPeriod(getTime());
    }

    public static String formatPeriod(long periodNanos) {
        return formatPeriod(periodNanos, DatePart.MILLISECOND);
    }

    public static String formatPeriod(long periodNanos, DatePart precision) {
        return DefaultTimePeriodFormat.of(precision).formatNanos(periodNanos);
    }

    public String toString(DatePart precision) {
        return formatPeriod(getTime(), precision);
    }

    public String formatPeriodNano(DatePart precision) {
        StringBuilder sb = new StringBuilder();
        boolean started = false;
        int h = getHours();
        int mn = getMinutes();
        int s = getSeconds();
        int ms = getMilliSeconds();
        int us = getMicroSeconds();
        int ns = getNanoSeconds();
        if (precision.ordinal() >= DatePart.HOUR.ordinal()) {
            if (h > 0) {
                if (started) {
                    sb.append(" ");
                }
                sb.append(_StringUtils.formatRight(h, 2)).append("h");
                started = true;
            }
            if (precision.ordinal() >= DatePart.MINUTE.ordinal()) {
                if (mn > 0 || started) {
                    if (started) {
                        sb.append(" ");
                    }
                    sb.append(_StringUtils.formatRight(mn, 2)).append("mn");
                    started = true;
                }
                if (precision.ordinal() >= DatePart.SECOND.ordinal()) {
                    if (s > 0 || started) {
                        if (started) {
                            sb.append(" ");
                        }
                        sb.append(_StringUtils.formatRight(s, 2)).append("s");
                        started = true;
                    }
                    if (precision.ordinal() >= DatePart.MILLISECOND.ordinal()) {
                        if (s > 0 || started) {
                            if (started) {
                                sb.append(" ");
                            }
                            sb.append(_StringUtils.formatRight(ms, 3)).append("ms");
                            started = true;
                        }
                        if (precision.ordinal() >= DatePart.MICROSECOND.ordinal()) {
                            if (s > 0 || started) {
                                if (started) {
                                    sb.append(" ");
                                }
                                sb.append(_StringUtils.formatRight(us, 3)).append("us");
                                started = true;
                            }
                            if (precision.ordinal() >= DatePart.NANOSECOND.ordinal()) {
                                if (started) {
                                    sb.append(" ");
                                }
                                sb.append(_StringUtils.formatRight(ns, 3)).append("ns");
                            }
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    public int getHours() {
        return (int) (toHours() % 24);
    }

    public int getMinutes() {
        return (int) (toMinutes() % 60L);
    }

    public int getSeconds() {
        return (int) (toSeconds() % 60L);
    }

    public int getMilliSeconds() {
        return (int) (toMilliSeconds() % 1000L);
    }

    public int getMicroSeconds() {
        return (int) (toMicroSeconds() % 1000);
    }

    public int getNanoSeconds() {
        return (int) (getTime() % 1000);
    }

    public long toHours() {
        return getTime() / (60000000000L * 60);
    }

    public long toMinutes() {
        return getTime() / 60000000000L;
    }

    public long toSeconds() {
        return getTime() / 1000000000;
    }

    public long toMilliSeconds() {
        return getTime() / 1000000;
    }

    public long toMicroSeconds() {
        return getTime() / 1000;
    }

    public TimeDuration div(int a) {
        return new TimeDuration(nanos / a);
    }

    public TimeDuration mul(int a) {
        return new TimeDuration(nanos * a);
    }
}
