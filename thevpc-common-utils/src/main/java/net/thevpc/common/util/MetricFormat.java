package net.thevpc.common.util;

/**
 * Created by vpc on 3/20/17.
 */
public class MetricFormat extends DefaultUnitFormat {
    public MetricFormat(boolean leadingZeros, boolean intermediateZeros, boolean fixedLength, int high, int low, boolean decimal) {
        super("m", leadingZeros, intermediateZeros, fixedLength, high, low, decimal);
    }

    public MetricFormat() {
        this("M-3 M3 I2 D2");
    }

    public MetricFormat(String format) {
        super("m", format);
    }
}
