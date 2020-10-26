package net.vpc.common.util;

import java.text.DecimalFormat;

/**
 * #0.0#
 * Created by vpc on 3/20/17.
 */
public class DefaultNumberFormat implements DoubleFormat {

    private int high = 6;
    private int low = -2;
    private String decimalFormatString = null;
    private DecimalFormat decimalFormat = null;
    private String unitCode;
    private int[] pows = {-12, -9, -6, -3, -2, -1, 0, 1, 2, 3, 6, 9, 12};

    public DefaultNumberFormat(String unitCode, String decimalFormat, int high, int low) {
        this.unitCode = unitCode;
        this.decimalFormatString = decimalFormat;
        this.decimalFormat = new DecimalFormat(decimalFormat);
        this.high = high;
        this.low = low;
    }

    public DefaultNumberFormat(String format) {
        this(null, format);
    }

    public DefaultNumberFormat(String unitCode, String format) {
        if (format == null) {
            format = "E-3..3 #0.00";
        }
        this.unitCode = unitCode;
        int low = 0;
        int high = 0;
        if (format != null) {
            boolean startInterval = true;
            char[] charArray = format.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                char c = Character.toUpperCase(charArray[i]);
                switch (c) {
                    case ' ': {
                        //ignore
                        break;
                    }
                    case 'E': {
                        String u = readNbr(charArray, i + 1);
                        if (u == null) {
                            throw new IllegalArgumentException("expected E<low>..<up>");
                        }
                        low = Integer.parseInt(u);
                        i += 1 + u.length();
                        if (i < charArray.length) {
                            if (Character.isWhitespace(charArray[i])) {
                                //skip
                            } else if (i + 1 < charArray.length && charArray[i] == '.' && charArray[i] == '.') {
                                i += 2;
                                u = readNbr(charArray, i);
                                if (u == null) {
                                    throw new IllegalArgumentException("expected E<low>..<up>");
                                }
                                high = Integer.parseInt(u);
                                i += u.length();
                            } else {
                                throw new IllegalArgumentException("expected E<low>..<up>");
                            }
                        }
                        //ignore
                        break;
                    }
                    case '\'': {
                        i++;
                        StringBuilder sb = new StringBuilder();
                        while (i < charArray.length && charArray[i] != '\'') {
                            sb.append(charArray[i]);
                            i++;
                        }
                        decimalFormatString = sb.toString();
                        break;
                    }
                    case '+':
                    case '-':
                    case '*':
                    case '#':
                    case '0': {
                        StringBuilder sb = new StringBuilder();
                        while (i < charArray.length && charArray[i] != ' ') {
                            sb.append(charArray[i]);
                            i++;
                        }
                        decimalFormatString = sb.toString();
                        break;
                    }
                    case ':': {
                        if (unitCode == null) {
                            StringBuilder sb = new StringBuilder();
                            while (i + 1 < charArray.length && !Character.isWhitespace(charArray[i + 1])) {
                                sb.append(charArray[i + 1]);
                                i++;
                            }
                            this.unitCode = sb.toString();
                        } else {
                            throw new IllegalArgumentException("Unexpected ':'");
                        }
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Unsupported " + c);
                    }
                }
            }
        }
        if (this.unitCode == null || this.unitCode.isEmpty()) {
            throw new IllegalArgumentException("Missing Unit");
        }
        if (low == '\0') {
            low = 'H';
        }
        if (high == '\0') {
            high = 'T';
        }
        this.low = (low);
        this.high = (high);
        if (this.high < this.low) {
            int t = this.low;
            this.low = this.high;
            this.high = t;
        }
        //force decimal
        if (decimalFormatString == null) {
            decimalFormatString = "0.00";
        }
        if (decimalFormatString.contains("*")) {
            //how to manage this????
            decimalFormat = new DecimalFormat(decimalFormatString);
        } else {
            decimalFormat = new DecimalFormat(decimalFormatString);
        }
    }

    private static String readNbr(char[] charArray, int from) {
        if (from + 1 < charArray.length && charArray[from] == '-' && Character.isDigit(charArray[from + 1])) {
            StringBuilder sb = new StringBuilder();
            sb.append(charArray[from]);
            sb.append(charArray[from + 1]);
            from += 2;
            while (from < charArray.length && Character.isDigit(charArray[from])) {
                sb.append(charArray[from]);
                from++;
            }
            return sb.toString();
        } else if (from < charArray.length && Character.isDigit(charArray[from])) {
            StringBuilder sb = new StringBuilder();
            while (from < charArray.length && Character.isDigit(charArray[from])) {
                sb.append(charArray[from]);
                from++;
            }
            return sb.toString();
        }
        return null;
    }

    public String toPattern() {
        return ':' + getUnitCode() +
                ' ' + 'E' + low + ".." + high +
                ' ' + decimalFormatString;
    }

    public int getHigh() {
        return high;
    }

    public int getLow() {
        return low;
    }

    //    public static void main(String[] args) {
//        MetricFormat frt = new MetricFormat();
////        System.out.println(frt.format(0));
//        double f = 10E-6;
//        for (int i = 0; i < 10; i++) {
//            String s = frt.format(f);
//            System.out.println(s + " <== " + f);
//            f = f * 10;
//        }
//    }
    private boolean isDecimal() {
        return false;
    }

    private String formatLeftPow(double number, int pow) {
        return formatLeft(number * 1.0 / Math.pow(10, pow));
    }

    private String formatLeft(double number) {
        return formatLeft(number, getMinFormatLength());
    }

    private int getMinFormatLength() {
        int x = 0;
        for (char c : decimalFormatString.toCharArray()) {
            if (c != '*' && c != '#') {
                x++;
            }
        }
        return x;
    }

    private boolean isFixedLength() {
        return decimalFormatString.contains("*") || decimalFormatString.contains("#");
    }

    private String formatLeft(Object number, int size) {
        if (decimalFormat != null) {
            String s = decimalFormat.format(number);
            if (isFixedLength()) {
                return _StringUtils.formatLeft(s, size);
            } else {
                return s;
            }
        } else {
            throw new IllegalArgumentException("Not supported yet");
        }
    }

    @Override
    public String formatDouble(double value) {
        return format(value);
    }


    private int evalInv(String pow) {
        if (!pow.endsWith(getUnitCode())) {
            throw new IllegalArgumentException("invalid power");
        }
        pow = pow.substring(0, pow.length() - getUnitCode().length());
        switch (pow) {
            case "p":
                return -1;
            case "n":
                return -8;
            case "u":
                return -6;
            case "m":
                return -3;
            case "c":
                return -2;
            case "d":
                return -1;
            case "":
                return 0;
            case "da":
                return 1;
            case "h":
                return 2;
            case "k":
                return 3;
            case "M":
                return 6;
            case "G":
                return 9;
            case "T":
                return 12;
        }
        throw new IllegalArgumentException("Unsupported");
    }

    private String strUnit(int pow) {
        switch (pow) {
            case -12:
                return "p" + getUnitCode();
            case -9:
                return "n" + getUnitCode();
            case -6:
                return "u" + getUnitCode();
            case -3:
                return "m" + getUnitCode();
            case -2:
                return "c" + getUnitCode();
            case -1:
                return "d" + getUnitCode();
            case 0:
                return getUnitCode();
            case 1:
                return "da" + getUnitCode();
            case 2:
                return "h" + getUnitCode();
            case 3:
                return "k" + getUnitCode();
            case 6:
                return "M" + getUnitCode();
            case 9:
                return "G" + getUnitCode();
            case 12:
                return "T" + getUnitCode();
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public String getUnitCode() {
        return unitCode;
    }

    public String format(double value) {
        StringBuilder sb = new StringBuilder();
        boolean neg = value < 0;
        int sign = neg ? -1 : 1;
        double v = value < 0 ? -value : value;
        double r = v;
        if (v == 0) {
            sb.append(formatLeft(0));
            sb.append(strUnit(low));
        } else {
            if (v < Math.pow(10, low)) {
                sb.append(formatLeftPow(v * sign, low));
                sb.append(strUnit(low));
            } else if (v >= Math.pow(10, high)) {
                sb.append(formatLeftPow(v * sign, high)).append(strUnit(high));
            } else {
                boolean ok = false;
                for (int i = pows.length - 1; i >= 0; i--) {
                    double b = Math.pow(10, pows[i]);
                    if (v >= b) {
                        sb.append(formatLeftPow(v * sign, pows[i])).append(strUnit(pows[i]));
                        ok = true;
                        break;
                    }
                }
                if (!ok) {
                    sb.append(formatLeftPow(v * sign, pows[0])).append(strUnit(pows[0]));
                }
            }
        }
        return sb.toString();
    }

}
