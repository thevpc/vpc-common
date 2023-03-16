package net.thevpc.common.time;


import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by vpc on 3/20/17.
 */
public class ShortTimeDurationFormat implements TimeDurationFormat {
    //    private static final DecimalFormat SECONDS_FORMAT = new DecimalFormat("00.000");
    private DatePart precision;
    boolean skipZeros = false;
    private static TimeDurationFormat[] _FORMATS = new TimeDurationFormat[DatePart.values().length];
    private static DecimalFormat $TF2$ = new DecimalFormat("#00");
    private static DecimalFormat $TF3$ = new DecimalFormat("#000");

    static {
        DatePart[] values = DatePart.values();
        for (int i = 0; i < values.length; i++) {
            _FORMATS[i] = new ShortTimeDurationFormat(values[i]);
        }
    }

    public static final TimeDurationFormat DEFAULT = of(DatePart.NANOSECOND);

    public static TimeDurationFormat of(DatePart d) {
        return _FORMATS[d.ordinal()];
    }

    public ShortTimeDurationFormat() {
        this(DatePart.NANOSECOND);
    }

    public ShortTimeDurationFormat(DatePart precision) {
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
        Set<DatePart> visited = new HashSet<>();
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
                sb.append($TF2$.format(days)).append('d');
                visited.add(DatePart.DATE);
                empty = false;
            }
            max--;
        }

        if (max > 0) {
            if (hours != 0 || (empty && max == 1) || (!empty && !skipZeros)) {
                sb.append($TF2$.format(hours));
                visited.add(DatePart.HOUR);
                empty = false;
            }
            max--;
        }

        if (max > 0) {
            if (minutes != 0 || (empty && max == 1) || (!empty && !skipZeros)) {
                if (!empty) {
                    sb.append(':');
                }
                sb.append($TF2$.format(minutes));
                visited.add(DatePart.MINUTE);
                empty = false;
            }
            max--;
        }

        if (max > 0) {
            if (seconds != 0 || (empty && max == 1) || (!empty && !skipZeros)) {
                if (!empty) {
                    sb.append(':');
                }
                if (max == 1) {
                    sb.append($TF2$.format(seconds));
                } else {
                    sb.append($TF2$.format((int) seconds));
                }
                visited.add(DatePart.SECOND);
                empty = false;
            }
            max--;
        }

        boolean hasDot = false;
        if (max > 0) {
            if (milliSeconds != 0 || (empty && max == 1) || (!empty && !skipZeros)) {
                if (!empty && !hasDot) {
                    sb.append('.');
                    hasDot = true;
                }
                sb.append($TF3$.format(milliSeconds));
                visited.add(DatePart.MILLISECOND);
                empty = false;
            }
            max--;
        }

        if (max > 0) {
            if (micro != 0 || (empty && max == 1) || (!empty && !skipZeros)) {
                if (!empty && !hasDot) {
                    sb.append('.');
                    hasDot = true;
                }
                sb.append($TF3$.format(micro));
                visited.add(DatePart.MICROSECOND);
                empty = false;
            }
            max--;
        }

        if (max > 0) {
            if (nano != 0 || (empty && max == 1) || (!empty && !skipZeros)) {
                if (!empty && !hasDot) {
                    sb.append('.');
                    hasDot = true;
                }
                sb.append($TF3$.format(nano));
                visited.add(DatePart.NANOSECOND);
                empty = false;
            }
            max--;
        }

        if (empty) {
            sb.append('0');
            sb.append(suffix(precision));
        } else if (visited.size() == 1) {
            if(!visited.contains(DatePart.DATE)) {
                sb.append(suffix((DatePart) visited.toArray()[0]));
            }
        }
        return sb.toString();
    }

    private String suffix(DatePart precision) {
        switch (precision) {
            case DATE: {
                return ("d");
            }
            case HOUR: {
                return ("h");
            }
            case MINUTE: {
                return ("mn");
            }
            case SECOND: {
                return ("s");
            }
            case MILLISECOND: {
                return ("ms");
            }
            case MICROSECOND: {
                return ("us");
            }
            case NANOSECOND: {
                return ("ns");
            }
        }
        return "";
    }
}
