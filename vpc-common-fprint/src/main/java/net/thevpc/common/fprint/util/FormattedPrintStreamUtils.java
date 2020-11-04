package net.thevpc.common.fprint.util;

import net.thevpc.common.fprint.parser.DefaultFormattedPrintStreamParser;

import java.util.Arrays;
import java.util.Formatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormattedPrintStreamUtils {
    // %[argument_index$][flags][width][.precision][t]conversion
    private static final Pattern printfPattern = Pattern.compile("%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])");

    public static String format(Locale locale, String format, Object... args) {

        StringBuilder sb = new StringBuilder();
        Matcher m = printfPattern.matcher(format);
        int x = 0;
        for (int i = 0, len = format.length(); i < len; ) {
            if (m.find(i)) {
                // Anything between the start of the string and the beginning
                // of the format specifier is either fixed text or contains
                // an invalid format string.
                if (m.start() != i) {
                    //checkText(s, i, m.start());
                    sb.append(format.substring(i, m.start()));
                }
                Object arg = x<args.length?args[x]:"MISSING_ARG_"+x;
                sb.append(DefaultFormattedPrintStreamParser.INSTANCE.escapeText(format0(locale, m.group(), arg)));
                x++;
                i = m.end();
            } else {
                sb.append(format.substring(i));
                break;
            }
        }
        return sb.toString();
    }


    private static String format0(Locale locale, String format0, Object arg) {
        StringBuilder sb = new StringBuilder();
        new Formatter(sb, locale).format(format0, new Object[]{arg});
        return sb.toString();
    }

    public static String fillString(char x, int width) {
        char[] cc = new char[width];
        Arrays.fill(cc, x);
        return new String(cc);
    }

    public static boolean isEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }
}
