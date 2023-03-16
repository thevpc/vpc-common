package net.thevpc.common.time;


/**
 * Created by vpc on 3/20/17.
 */
public class DefaultTimeDurationFormat implements TimeDurationFormat {
    //    private static final DecimalFormat SECONDS_FORMAT = new DecimalFormat("00.000");
    private DatePart precision;
    boolean skipZeros = false;
    private static TimeDurationFormat[] _FORMATS = new TimeDurationFormat[DatePart.values().length];

    static {
        DatePart[] values = DatePart.values();
        for (int i = 0; i < values.length; i++) {
            _FORMATS[i] = new DefaultTimeDurationFormat(values[i]);
        }
    }

    public static final TimeDurationFormat DEFAULT = of(DatePart.NANOSECOND);

    public static TimeDurationFormat of(DatePart d) {
        return _FORMATS[d.ordinal()];
    }

    public DefaultTimeDurationFormat() {
        this(DatePart.NANOSECOND);
    }

    public DefaultTimeDurationFormat(DatePart precision) {
        this.precision = precision;
    }


    @Override
    public String formatMillis(long periodMillis) {
        return formatNanos(periodMillis * 1000000L);
    }


    public String formatNanos(long nanos) {
        return format(nanos / 1000000, (int) (nanos % 1000000));
    }

    @Override
    public String format(long millis, int nanos) {
        if (millis < 0) {
            throw new IllegalArgumentException("invalid millis " + millis);
        }
        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException("invalid nanos " + millis);
        }
        int max;
        boolean empty = true;
        switch (precision) {
            case DATE: {
                max = 1;
                break;
            }
            case HOUR: {
                max = 2;
                break;
            }
            case MINUTE: {
                max = 3;
                break;
            }
            case SECOND: {
                max = 4;
                break;
            }
            case MILLISECOND: {
                max = 5;
                break;
            }
            case MICROSECOND: {
                max = 6;
                break;
            }
            case NANOSECOND: {
                max = 7;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported precision use Calendar.DATE,Calendar.HOUR, ...Calendar.MILLISECOND");
            }
        }

        int nano = (int) (nanos % 1000L);
        int micro = (int) (nanos / 1000L);

        int milliSeconds = (int) (millis % 1000L);

        double seconds = (int) ((millis / 1000) % 60);

        int minutes = (int) ((millis / 60000) % 60);

        int hours = (int) ((millis / 3600000) % 24);

        int days = (int) ((millis / 3600000 / 24));

        StringBuilder sb = new StringBuilder();

        if (max > 0) {
            if (days != 0 || (empty && max == 1)) {
                int years = days / 365;
                if (years >= 1) {
                    return "an eternity";
                }
                if (!empty) {
                    sb.append(' ');
                }
                sb.append(_StringUtils.formatRight(days, 2)).append('d');
                empty = false;
            }
            max--;
        }

        if (max > 0) {
            if (hours != 0 || (empty && max == 1) || (!empty && !skipZeros)) {
                if (!empty) {
                    sb.append(' ');
                }
                sb.append(_StringUtils.formatRight(hours, 2)).append('h');
                empty = false;
            }
            max--;
        }

        if (max > 0) {
            if (minutes != 0 || (empty && max == 1) || (!empty && !skipZeros)) {
                if (!empty) {
                    sb.append(' ');
                }
                sb.append(_StringUtils.formatRight(minutes, 2)).append("mn");
                empty = false;
            }
            max--;
        }

        if (max > 0) {
            if (seconds != 0 || (empty && max == 1) || (!empty && !skipZeros)) {
                if (!empty) {
                    sb.append(' ');
                }
                if (max == 1) {
                    sb.append(_StringUtils.formatRight(seconds, 2)).append("s");
                } else {
                    sb.append(_StringUtils.formatRight((int) seconds, 2)).append("s");
                }
                empty = false;
            }
            max--;
        }

        if (max > 0) {
            if (milliSeconds != 0 || (empty && max == 1) || (!empty && !skipZeros)) {
                if (!empty) {
                    sb.append(' ');
                }
                sb.append(_StringUtils.formatRight(milliSeconds, 3)).append("ms");
                empty = false;
            }
            max--;
        }

        if (max > 0) {
            if (micro != 0 || (empty && max == 1) || (!empty && !skipZeros)) {
                if (!empty) {
                    sb.append(' ');
                }
                sb.append(_StringUtils.formatRight(micro, 3)).append("us");
                empty = false;
            }
            max--;
        }

        if (max > 0) {
            if (nano != 0 || (empty && max == 1) || (!empty && !skipZeros)) {
                if (!empty) {
                    sb.append(' ');
                }
                sb.append(_StringUtils.formatRight(nano, 3)).append("ns");
                empty = false;
            }
            max--;
        }

        if (empty) {
            sb.append('0');
        }
        return sb.toString();
    }

}
