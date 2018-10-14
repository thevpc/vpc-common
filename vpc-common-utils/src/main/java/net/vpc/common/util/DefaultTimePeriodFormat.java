package net.vpc.common.util;

import java.text.DecimalFormat;

/**
 * Created by vpc on 3/20/17.
 */
public class DefaultTimePeriodFormat implements TimePeriodFormat {

    private static final DecimalFormat SECONDS_FORMAT = new DecimalFormat("00.000");
    private DatePart precision;

    public DefaultTimePeriodFormat() {
        this(DatePart.MILLISECOND);
    }

    public DefaultTimePeriodFormat(DatePart precision) {
        this.precision = precision;
    }


    public String formatMillis(long periodMillis) {
        return formatNanos(periodMillis*1000000L);
    }

    public String formatNanos(long periodNano) {
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
            default: {
                throw new IllegalArgumentException("Unsupported precision use Calendar.DATE,Calendar.HOUR, ...Calendar.MILLISECOND");
            }
        }
        int nano = (int) (periodNano % 1000000);
        long period = periodNano / 1000000;

        int milliSeconds = (int) (period % 1000L);

        double seconds = ((period % (60L * 1000L)) / 1000.0);

        int minutes = (int) ((period % (60L * 60L * 1000L)) / (60L * 1000L));

        int hours = (int) ((period % (24L * 60L * 60L * 1000L)) / (60L * 60L * 1000L));

        int days = (int) (period / (24L * 60L * 60L * 1000L));
        boolean skipZeros = false;
        StringBuilder sb = new StringBuilder();

        if (max > 0) {
            if (days != 0 || (empty && max == 1)) {
                if (!empty) {
                    sb.append(' ');
                }
                sb.append(_StringUtils.formatLeft(days, 2)).append('d');
                empty = false;
            }
            max--;
        }

        if (max > 0) {
            if (hours != 0 || (empty && max == 1) || (!empty && !skipZeros)) {
                if (!empty) {
                    sb.append(' ');
                }
                sb.append(_StringUtils.formatLeft(hours, 2)).append('h');
                empty = false;
            }
            max--;
        }

        if (max > 0) {
            if (minutes != 0 || (empty && max == 1) || (!empty && !skipZeros)) {
                if (!empty) {
                    sb.append(' ');
                }
                sb.append(_StringUtils.formatLeft(minutes, 2)).append("mn");
                empty = false;
            }
            max--;
        }

        if (max > 1) {
            if (seconds != 0 || empty) {
                if (!empty) {
                    sb.append(' ');
                }
                if (seconds == (int) seconds) {
                    sb.append(_StringUtils.formatLeft((int) seconds, 2)).append('s').append("    ");
                } else {
                    sb.append(SECONDS_FORMAT.format(seconds)).append('s');
                }

//                empty = false;
            }
//            max--;
//            max--;

        } else if (max > 0) {
            if (seconds != 0 || empty) {
                if (!empty) {
                    sb.append(' ');
                }
                sb.append((int) seconds);
                sb.append('s');
//                empty = false;
            }
//            max--;
        }
        return sb.toString();
    }

}
