package net.thevpc.common.swing.util;

import net.thevpc.common.swing.label.DatePart;

import java.text.DecimalFormat;

public class TimeDuration {
    private static DecimalFormat $TF2$ = new DecimalFormat("#00");
    private static DecimalFormat $TF3$ = new DecimalFormat("#000");

    public final int h;
    public final int mn;
    public final int s;
    public final int ms;
    public final int ns;


    public static TimeDuration ofMillis(long millis) {
        return ofMillis(millis,0);
    }
    public static TimeDuration ofMillis(long millis,int nanos) {
        int h = (int) (millis / (1000L * 60L * 60L));
        int mn = (int) ((millis % (1000L * 60L * 60L)) / 60000L);
        int s = (int) ((millis % 60000L) / 1000L);
        int ms = (int) (millis % 1000L);
        return new TimeDuration(h,mn,s,ms,nanos);
    }

    public static TimeDuration ofNanos(long nanos) {
        int ns=(int)(nanos%1000000L);
        long r=nanos/1000000;
        int h = (int) (r / (1000L * 60L * 60L));
        int mn = (int) ((r % (1000L * 60L * 60L)) / 60000L);
        int s = (int) ((r % 60000L) / 1000L);
        int ms = (int) (r % 1000L);
        return new TimeDuration(h,mn,s,ms,ns);
    }

    public TimeDuration(int h, int mn, int s, int ms, int ns) {
        this.h = h;
        this.mn = mn;
        this.s = s;
        this.ms = ms;
        this.ns = ns;
    }

    public String formatPeriodFixed(DatePart min, DatePart max) {
        StringBuilder sb = new StringBuilder();
        boolean started = false;
        int mino = min.ordinal();
        int maxo = max.ordinal();
        if (mino >= DatePart.h.ordinal()) {
            if (h > 0 || maxo <= DatePart.h.ordinal()) {
                sb.append($TF2$.format(h));
                started = true;
            }
            if (mino >= DatePart.mn.ordinal()) {
                if (mn > 0 || started || maxo <= DatePart.mn.ordinal()) {
                    if (sb.length() > 0) {
                        sb.append(":");
                    }
                    sb.append($TF2$.format(mn));
                    started = true;
                }
                if (mino >= DatePart.s.ordinal()) {
                    if (s > 0 || started || maxo <= DatePart.s.ordinal()) {
                        if (sb.length() > 0) {
                            sb.append(":");
                        }
                        sb.append($TF2$.format(s));
                        //started=true;
                    }
                    if (mino >= DatePart.ms.ordinal()) {
                        if (sb.length() > 0) {
                            sb.append(".");
                        }
                        sb.append($TF3$.format(ms));
                    }
                }
            }
        }
        return sb.toString();
    }

    public String formatPeriodShort(DatePart precision) {
        StringBuilder sb = new StringBuilder();
        boolean started = false;
        boolean withH = false;
        boolean withMN = false;
        boolean withS = false;
        boolean withMS = false;
        int withCount = 0;
        if (precision.ordinal() >= DatePart.h.ordinal()) {
            if (h > 0) {
                withH = true;
                withCount++;
                sb.append($TF2$.format(h));
                started = true;
            }
            if (precision.ordinal() >= DatePart.mn.ordinal()) {
                if (mn > 0 || started) {
                    if (sb.length() > 0) {
                        sb.append(":");
                    }
                    withMN = true;
                    withCount++;
                    sb.append($TF2$.format(mn));
                    started = true;
                }
                if (precision.ordinal() >= DatePart.s.ordinal()) {
                    if (sb.length() > 0) {
                        sb.append(":");
                    }
                    if (s > 0 || started) {
                        withS = true;
                        withCount++;
                        sb.append($TF2$.format(s));
                        //started=true;
                    }
                    if (precision.ordinal() >= DatePart.ms.ordinal()) {
                        if (sb.length() > 0) {
                            sb.append(":");
                        }
                        withMS = true;
                        withCount++;
                        sb.append($TF3$.format(ms));
                    }
                }
            }
        }
        if (sb.length() == 0) {
            switch (precision) {
                case h:
                    return "0h";
                case mn:
                    return "0mn";
                case s:
                    return "0s";
                case ms:
                    return "0ms";
            }
        }
        if (withCount == 1) {
            if (withH) {
                return sb + "h";
            } else if (withMN) {
                return sb + "mn";
            } else if (withS) {
                return sb + "s";
            } else {
                return sb + "ms";
            }
        }
        return sb.toString();
    }

}
