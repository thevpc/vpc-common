package net.thevpc.common.util;

import java.text.DecimalFormat;

public class DecimalFormat2 {
    String format;
    int leadingSpaces = 0;
    int trailingSpaces = 0;
    DecimalFormat df;
    String dPattern;

    /**
     * __#0.0__
     *
     * @param format
     */
    public DecimalFormat2(String format) {
        this.format = format;
        StringBuilder sb = new StringBuilder(format);
        while (sb.length() > 0 && sb.charAt(0) == '_') {
            leadingSpaces++;
            sb.delete(0,1);
        }
        while (sb.length() > 0 && sb.charAt(sb.length() - 1) == '_') {
            sb.delete(sb.length() - 1,sb.length());
            trailingSpaces++;
        }
        if (sb.length() > 0 && sb.charAt(0) == '#' && leadingSpaces > 0) {
            throw new IllegalArgumentException("Invalid sequence '_#' ");
        }
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '#' && trailingSpaces > 0) {
            throw new IllegalArgumentException("Invalid sequence '#_' ");
        }
        dPattern = sb.toString();
        df = new DecimalFormat(dPattern);
    }

    public String format(double d) {
        String s0 = df.format(d);
        StringBuilder s = new StringBuilder(s0);
        if (leadingSpaces > 0) {
            int fc = getIntsCount(s.toString());
            int fc0 = getIntsCount(dPattern);
            if (fc < leadingSpaces) {
                for (int i = 0; i < leadingSpaces - fc; i++) {
                    s.insert(0, ' ');
                }
            }
        }
        if (trailingSpaces > 0) {
            int fc = getFractionsCount(s0);
            if (fc < trailingSpaces) {
                for (int i = 0; i < trailingSpaces - fc; i++) {
                    s.append(' ');
                }
            }
        }
        return s.toString();
    }

    private int getIntsCount(String s) {
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() > 0 && !Character.isDigit(sb.charAt(0))) {
            sb.delete(0, 1);
        }
        while (sb.length() > 0 && !Character.isDigit(sb.charAt(sb.length() - 1))) {
            sb.delete(sb.length() - 1, sb.length());
        }
        // 12.8E-3
        int x = s.indexOf('.');
        if (x < 0) {
            return s.length();
        }
        return x;
    }

    private int getFractionsCount(String s) {
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() > 0 && !Character.isDigit(sb.charAt(0))) {
            sb.delete(0, 1);
        }
        while (sb.length() > 0 && !Character.isDigit(sb.charAt(sb.length() - 1))) {
            sb.delete(sb.length() - 1, sb.length());
        }
        // 12.8E-3
        int x = s.indexOf('.');
        if (x < 0) {
            return 0;
        }
        return x + 1;
    }
}
