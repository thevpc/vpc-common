package net.thevpc.common.util;

import java.text.DecimalFormat;

/**
 * Created by vpc on 3/20/17.
 */
public class FrequencyFormat implements DoubleFormat{

    boolean leadingZeros = false;
    boolean intermediateZeros = true;
    private boolean fixedLength = false;
    private boolean decimal = false;
    private long high = Units.TERA;
    private long low = Units.HERTZ;
    private int fractionDigits = 3;
    private int integerDigits = 3;
    private DecimalFormat decimalFormat = null;

    public static final FrequencyFormat INSTANCE=new FrequencyFormat();

    public FrequencyFormat(boolean leadingZeros, boolean intermediateZeros, boolean fixedLength, long high, long low, boolean decimal) {
        this.leadingZeros = leadingZeros;
        this.intermediateZeros = intermediateZeros;
        this.fixedLength = fixedLength;
        this.high = high;
        this.low = low;
        this.decimal = decimal;
        if(fixedLength){
            decimalFormat = new DecimalFormat("0."+_StringUtils.fillString('0',integerDigits));
        }else{
            decimalFormat = new DecimalFormat("0."+_StringUtils.fillString('#',fractionDigits));
        }
    }

    public FrequencyFormat() {
        this("HT I2 D3 ");
    }

    public String toPattern(){
        StringBuilder sb=new StringBuilder();
        if(leadingZeros){
            sb.append('0');
        }
        sb.append(evalInv(low));
        sb.append(evalInv(high));
        if(decimal){
            sb.append('D').append(fractionDigits);
        }
        sb.append('I').append(integerDigits);
        if(fixedLength){
            sb.append('F');
        }
        if(intermediateZeros){
            sb.append(' ');
            sb.append('0');
        }

        return sb.toString();
    }
    public FrequencyFormat(String format) {
        leadingZeros = false;
        intermediateZeros = false;
        char low = '\0';
        char high = '\0';
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
                    case 'H':
                    case 'K':
                    case 'M':
                    case 'G':
                    case 'T': {
                        if (startInterval) {
                            startInterval = false;
                            low = c;
                        } else {
                            high = c;
                        }
                        break;
                    }
                    case 'D': {
                        decimal = true;
                        if (i + 1 < charArray.length && Character.isDigit(charArray[i + 1])) {
                            i++;
                            fractionDigits = charArray[i] - '0';
                        }
                        break;
                    }
                    case 'F': {
                        fixedLength = true;
                        break;
                    }
                    case 'I': {
                        if (i + 1 < charArray.length && Character.isDigit(charArray[i + 1])) {
                            i++;
                            integerDigits = charArray[i] - '0';
                        }
                        break;
                    }
                    case '0': {
                        if (i == 0) {
                            leadingZeros = true;
                        } else {
                            intermediateZeros = true;
                        }
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Unsupported " + c);
                    }
                }
            }
        }
        if (low == '\0') {
            low = 'H';
        }
        if (high == '\0') {
            high = 'T';
        }
        this.low = eval(low);
        this.high = eval(high);
        if (this.high < this.low) {
            long t = this.low;
            this.low = this.high;
            this.high = t;
        }
        if(fixedLength){
            decimalFormat = new DecimalFormat("0."+_StringUtils.fillString('0',integerDigits));
        }else{
            decimalFormat = new DecimalFormat("0."+_StringUtils.fillString('#',fractionDigits));
        }
    }

    private long eval(char c) {
        switch (c) {
            case 'H': {
                return Units.HERTZ;
            }
            case 'K': {
                return Units.KILO;
            }
            case 'M': {
                return Units.MEGA;
            }
            case 'G': {
                return Units.GIGA;
            }
            case 'T': {
                return Units.TERA;
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    private char evalInv(long c) {
        if(c==Units.HERTZ){
            return 'H';
        }
        if(c==Units.KILO){
            return 'K';
        }
        if(c==Units.MEGA){
            return 'M';
        }
        if(c==Units.GIGA){
            return 'G';
        }
        if(c==Units.TERA){
            return 'T';
        }
        throw new IllegalArgumentException("Unsupported");
    }

    private String formatLeft(double number) {
        String s = (decimal) ?
                formatLeft(number, integerDigits + 1 + fractionDigits)
                : formatLeft(number, integerDigits);
//        System.out.println("formatLeft " + s+" <= "+number);
        return s;
    }

    private String formatLeft(Object number, int size) {
        if (!decimal) {
            if (fixedLength) {
                return _StringUtils.formatLeft(number, size,fixedLength);
            } else {
                return String.valueOf(number);
            }
        } else {
            String s = decimalFormat.format(number);
            if (fixedLength) {
                return _StringUtils.formatLeft(s, size,fixedLength);
            } else {
                return s;
            }
        }
    }

    //@Override
    public String formatDouble(double value) {
        return format(value);
    }

    public String format(double frequency) {
        return format((long) frequency);
    }

    private String strUnit(long r) {
        if (r <= Units.HZ) {
            return ("Hz");
        } else if (r <= Units.KHZ) {
            return ("KHz");
        } else if (r <= Units.MHZ) {
            return ("MHz");
        } else if (r <= Units.GHZ) {
            return ("GHz");
        } else {
            return ("THz");
        }
    }

    public String format(long bytes) {
        StringBuilder sb = new StringBuilder();
        boolean neg = bytes < 0;
        int sign = neg?-1:1;
        long v = bytes < 0 ? -bytes : bytes;
        long r = v;
        if (decimal) {
            if (v == 0) {
                sb.append(formatLeft(0));
                sb.append(strUnit(low));
            } else {
                if (v < low) {
                    sb.append(formatLeft(v * 1.0 / low*sign));
                    sb.append(strUnit(low));
                } else if (v >= high) {
                    sb.append(formatLeft(v * 1.0 / high*sign)).append(strUnit(high));
                } else if (v >= Units.TERA) {
                    sb.append(formatLeft(v * 1.0 / Units.TERA*sign)).append(strUnit(Units.TERA));
                } else if (v >= Units.GIGA) {
                    sb.append(formatLeft(v * 1.0 / Units.GIGA*sign)).append(strUnit(Units.GIGA));
                } else if (v >= Units.MEGA) {
                    sb.append(formatLeft(v * 1.0 / Units.MEGA*sign)).append(strUnit(Units.MEGA));
                } else if (v >= Units.KILO) {
                    sb.append(formatLeft(v * 1.0 / Units.KILO*sign)).append(strUnit(Units.KILO));
                } else if (v >= Units.HZ) {
                    sb.append(formatLeft(v * 1.0 *sign)).append(strUnit(1));
                }
            }
            return sb.toString();
        } else {
            boolean empty = true;
            if (low <= Units.TERA) {
                if (high >= Units.TERA) {
                    r = v / Units.TERA;
                    if (r > 0 || (!empty && intermediateZeros)) {
                        if (sb.length() > 0) {
                            sb.append(" ");
                        }
                        sb.append(formatLeft(r)).append("THz");
                        v = v % Units.TERA;
                        empty = false;
                    }
                }
                if (low <= Units.GIGA) {
                    if (high >= Units.GIGA) {
                        r = v / Units.GIGA;
                    }
                    if ((leadingZeros && empty) || r > 0 || (!empty && intermediateZeros)) {
                        if (sb.length() > 0) {
                            sb.append(" ");
                        }
                        sb.append(formatLeft(r)).append("GHz");
                        v = v % Units.GIGA;
                        empty = false;
                    }
                    if (low <= Units.MEGA) {
                        if (high >= Units.MEGA) {
                            r = v / Units.MEGA;
                            if ((leadingZeros && empty) || r > 0 || (!empty && intermediateZeros)) {
                                if (sb.length() > 0) {
                                    sb.append(" ");
                                }
                                sb.append(formatLeft(r)).append("MHz");
                                v = v % Units.MEGA;
                                empty = false;
                            }
                        }
                        if (low <= Units.KILO) {
                            if (high >= Units.KILO) {
                                r = v / Units.KILO;
                                if ((leadingZeros && empty) || r > 0 || (!empty && intermediateZeros)) {
                                    if (sb.length() > 0) {
                                        sb.append(" ");
                                    }
                                    sb.append(formatLeft(r)).append("KHz");
                                    v = v % Units.KILO;
                                    empty = false;
                                }
                            }
                            if (low <= 1) {
                                if ((leadingZeros && empty) || v > 0 || sb.length() == 0 || (!empty && intermediateZeros)) {
                                    if (sb.length() > 0) {
                                        sb.append(" ");
                                    }
                                    sb.append(formatLeft(v)).append("Hz");
                                    empty = false;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (sb.length() == 0) {
            if (neg) {
                sb.insert(0, "-");
            }
            sb.append(formatLeft(0)).append(strUnit(low));
        } else {
            if (neg) {
                sb.insert(0, "-");
            }
        }
        return sb.toString();
    }
}
