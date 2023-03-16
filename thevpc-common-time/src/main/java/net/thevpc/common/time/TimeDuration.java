package net.thevpc.common.time;

public class TimeDuration {
    public static final TimeDuration ZERO = ofNanos(0);
    private long millis;
    private int d;
    private int h;
    private int mn;
    private int s;
    private int ms;
    private int us;
    private int ns;

    public static TimeDuration ofNanos(long nanos) {
        if (nanos < 0) {
            throw new IllegalArgumentException("invalid nanos " + nanos);
        }
        long millis = nanos / 1000000;
        int inanos = (int) (nanos % 1000);
        int imicro = (int) ((nanos % 1000000L) / 1000);
        int h = (int) (millis / (1000L * 60L * 60L));
        int mn = (int) ((millis % (1000L * 60L * 60L)) / 60000L);
        int s = (int) ((millis % 60000L) / 1000L);
        int ms = (int) (millis % 1000L);
        int d = h / 24;
        h = h % 24;
        return new TimeDuration(
                millis, d, h, mn, s, ms, imicro, inanos
        );
    }

    public static TimeDuration ofMillis(long millis) {
        return ofMillis(millis, 0);
    }

    public static TimeDuration of(int d, int h, int mn, int s, int ms, int us, int ns) {
        if (d < 0) {
            throw new IllegalArgumentException("invalid d " + d);
        }
        if (h < 0 || h >= 24) {
            throw new IllegalArgumentException("invalid h " + h);
        }
        if (mn < 0 || mn >= 60) {
            throw new IllegalArgumentException("invalid mn " + mn);
        }
        if (s < 0 || s >= 60) {
            throw new IllegalArgumentException("invalid s " + s);
        }
        if (ms < 0 || ms >= 1000) {
            throw new IllegalArgumentException("invalid ms " + ms);
        }
        if (us < 0 || us >= 1000) {
            throw new IllegalArgumentException("invalid us " + us);
        }
        if (ns < 0 || ns >= 1000) {
            throw new IllegalArgumentException("invalid ms " + ns);
        }
        long millis = ms + 1000 * s + 1000 * 60 * mn + 1000 * 3600 * h + 1000L * 3600 * 24 * d;
        return new TimeDuration(
                millis, d, h, mn, s, ms, us, ns
        );
    }

    public static TimeDuration ofMillis(long millis, int nanos) {
        if (millis < 0) {
            throw new IllegalArgumentException("invalid millis " + millis);
        }
        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException("invalid nanos " + millis);
        }
        int inanos = (int) (nanos % 1000);
        int imicro = (int) ((nanos % 1000000L) / 1000);
        int h = (int) (millis / (1000L * 60L * 60L));
        int mn = (int) ((millis % (1000L * 60L * 60L)) / 60000L);
        int s = (int) ((millis % 60000L) / 1000L);
        int ms = (int) (millis % 1000L);
        int d = h / 24;
        h = h % 24;
        return new TimeDuration(
                millis, d, h, mn, s, ms, imicro, inanos
        );
    }

    private TimeDuration(long millis, int d, int h, int mn, int s, int ms, int us, int ns) {
        this.millis = millis;
        this.d = d;
        this.h = h;
        this.mn = mn;
        this.s = s;
        this.ms = ms;
        this.us = us;
        this.ns = ns;
    }

    public long toNanoSeconds() {
        return getTimeMillis();
    }

    public long getTimeMillis() {
        return millis;
    }

    public long getTimeNanos() {
        return millis * 1000000 + us + 1000 + ns;
    }

    public String toString() {
        return format();
    }

    public String format() {
        return format(DatePart.MILLISECOND);
    }

    public String format(DatePart precision) {
        return DefaultTimeDurationFormat.of(precision).format(millis, ns + us * 1000);
    }

    public String formatShort() {
        return formatShort(DatePart.MILLISECOND);
    }

    public String formatShort(DatePart precision) {
        return ShortTimeDurationFormat.of(precision).format(millis, ns + us * 1000);
    }

    public String toString(DatePart precision) {
        return format(precision);
    }

    public int getHours() {
        return h;
    }

    public int getMinutes() {
        return mn;
    }

    public int getSeconds() {
        return s;
    }

    public int getMilliSeconds() {
        return ms;
    }

    public int getMicroSeconds() {
        return us;
    }

    public int getNanoSeconds() {
        return ns;
    }

    public long toHours() {
        return getTimeMillis() / (60000000000L * 60);
    }

    public long toMinutes() {
        return getTimeMillis() / 60000000000L;
    }

    public long toSeconds() {
        return getTimeMillis() / 1000000000;
    }

    public long toMilliSeconds() {
        return getTimeMillis() / 1000000;
    }

    public long toMicroSeconds() {
        return getTimeMillis() / 1000;
    }
}
