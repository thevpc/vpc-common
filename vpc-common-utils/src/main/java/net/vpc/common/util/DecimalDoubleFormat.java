package net.vpc.common.util;

import net.vpc.common.util.DoubleFormat;

import java.text.DecimalFormat;

public class DecimalDoubleFormat implements DoubleFormat {
    private final DecimalFormat d;
    private final String subFormat;

    public DecimalDoubleFormat(String subFormat) {
        this.subFormat = subFormat;
        d = new DecimalFormat(subFormat);
    }

    @Override
    public String formatDouble(double value) {
        return d.format(value);
    }
}
