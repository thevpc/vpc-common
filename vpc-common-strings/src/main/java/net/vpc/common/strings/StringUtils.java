/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.strings;

import java.io.*;
import java.lang.reflect.Array;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author taha.bensalah@gmail.com
 */
public class StringUtils {

    public static final Pattern DOLLAR_PLACE_HOLDER_PATTERN = Pattern.compile("[$][{](?<name>([^}]+))[}]");

    public static final StringConverter UPPER = new StringConverter() {
        @Override
        public String convert(String str) {
            return str == null ? null : str.toUpperCase();
        }
    };

    public static final StringConverter LOWER = new StringConverter() {
        @Override
        public String convert(String str) {
            return str == null ? null : str.toLowerCase();
        }
    };

    public static final StringConverter NORMALIZED = new StringConverter() {
        @Override
        public String convert(String str) {
            return normalizeString(str);
        }
    };

    public static StringConverter combineConverters(final StringConverter... converters) {
        return new StringConverter() {
            @Override
            public String convert(String str) {
                if (converters != null) {
                    for (StringConverter converter : converters) {
                        if (converter != null) {
                            str = converter.convert(str);
                        }
                    }
                }
                return str;
            }
        };
    }

    public static String normalizeString(String expression) {
        if (expression == null) {
            return null;
        }

        String nfdNormalizedString = Normalizer.normalize(expression, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }


    public static String nonNull(Object object) {
        if (object == null) {
            return "";
        }
        return String.valueOf(object);
    }

    public static String nonNull(String value) {
        if (value == null) {
            return "";
        }
        return value;
    }

    public static String trimObject(Object value) {
        if (value == null) {
            return "";
        }
        return value.toString().trim();
    }

    public static String trim(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }

    public static boolean isBlank(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static String escapeRegex(String string) {
        if (string == null) {
            return null;
        }

        StringBuilder buffer = new StringBuilder();
        String special = "*+()^$.{}[]|\\";
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            if (special.indexOf(chars[i]) != -1) {
                buffer.append('\\').append(chars[i]); // prefix all metacharacters with backslash
            } else {
                buffer.append(chars[i]);
            }
        }

        return buffer.toString().toLowerCase();
    }

    public static int indexOfWord(String string, String word) {
        return indexOfWord(string, word, 0, null);
    }

    public static int indexOfWord(String string, String word, int from) {
        return indexOfWord(string, word, from, null);
    }

    public static int indexOfWord(String string, String word, int from, String ponctuations) {
        int currFrom = from;
        if (ponctuations == null) {
            ponctuations = ".,:;";
        }
        if (string == null) {
            string = "";
        }
        if (word == null) {
            word = "";
        }
        if (word.length() == 0) {
            return -1;
        }
        int slength = string.length();
        int wlength = word.length();
        while (true) {
            int i = string.indexOf(word, currFrom);
            if (i >= 0) {
                if ((i == 0 || Character.isWhitespace(string.charAt(i - 1)) || ponctuations.indexOf(string.charAt(i - 1)) >= 0)
                        && ((i + wlength) >= slength || Character.isWhitespace(string.charAt(i + wlength))
                        || ponctuations.indexOf(string.charAt(i + wlength)) >= 0)) {
                    return i;
                }
                currFrom = i + 1;
            } else {
                break;
            }
        }
        return -1;
    }

    public static String wildcardToRegex(String pattern) {
        if (pattern == null) {
            pattern = "*";
        }
        int i = 0;
        char[] cc = pattern.toCharArray();
        StringBuilder sb = new StringBuilder("^");
        while (i < cc.length) {
            char c = cc[i];
            switch (c) {
                case '.':
                case '$':
                case '{':
                case '}':
                case '+': {
                    sb.append('\\').append(c);
                    break;
                }
                case '?': {
                    sb.append("[a-zA-Z_0-9$.]");
                    break;
                }
                case '*': {
                    if (i + 1 < cc.length && cc[i + 1] == '*') {
                        i++;
                        sb.append("[a-zA-Z_0-9$.]*");
                    } else {
                        sb.append("[a-zA-Z_0-9$]*");
                    }
                    break;
                }
                default: {
                    sb.append(c);
                }
            }
            i++;
        }
        sb.append('$');
        return sb.toString();
    }

    /**
     * @param pattern simple shell like
     * @param pathSeparator pathSeparator to consider for '**' escape pattern
     * @return
     */
    public static String wildcardToRegex(String pattern, char pathSeparator) {
        if (pattern == null) {
            pattern = "*";
        }
        int i = 0;
        char[] cc = pattern.toCharArray();
        StringBuilder sb = new StringBuilder("^");
        while (i < cc.length) {
            char c = cc[i];
            switch (c) {
                case '.':
                case '$':
                case '{':
                case '}':
                case '+': {
                    sb.append('\\').append(c);
                    break;
                }
                case '\\': {
                    sb.append(c);
                    i++;
                    sb.append(cc[i]);
                    break;
                }
                case '[': {
                    while (i < cc.length) {
                        sb.append(cc[i]);
                        if (cc[i] == ']') {
                            break;
                        }
                    }
                    break;
                }
                case '?': {
                    sb.append("[^").append(pathSeparator).append("]");
                    break;
                }
                case '*': {
                    if (i + 1 < cc.length && cc[i + 1] == '*') {
                        i++;
                        sb.append(".*");
                    } else {
                        sb.append("[^").append(pathSeparator).append("]*");
                    }
                    break;
                }
                default: {
                    sb.append(c);
                }
            }
            i++;
        }
        sb.append('$');
        return sb.toString();
    }

    public static int indexOfRegexpStart(String value, String regexp) {
        Pattern t = Pattern.compile(regexp);
        Matcher matcher = t.matcher(value);
        if (matcher.find()) {
            return matcher.start();
        }
        return -1;
    }

    public static boolean isJavaIdentifier(String value) {
        if (value == null || value.length() == 0) {
            return false;
        }
        char[] toCharArray = value.toCharArray();
        for (int i = 0; i < toCharArray.length; i++) {
            char u = toCharArray[i];
            if (i == 0) {
                if (!Character.isJavaIdentifierStart(u)) {
                    return false;
                }
            } else if (!Character.isJavaIdentifierPart(u)) {
                return false;
            }
        }
        return true;
    }

    public static StringLocation locationOfRegexp(String value, String regexp) {
        Pattern t = Pattern.compile(regexp);
        Matcher matcher = t.matcher(value);
        if (matcher.find()) {
            return new StringLocation(matcher.start(), matcher.end());
        }
        return null;
    }

    public static int indexOfRegexpEnd(String value, String regexp) {
        Pattern t = Pattern.compile(regexp);
        Matcher matcher = t.matcher(value);
        if (matcher.find()) {
            return matcher.end();
        }
        return -1;
    }

    //    public static void main(String[] args) {
//        String r=replaceVar("${y}hello${world}${world2}${world3}", "${", "}",new StringConverter() {
//
//            @Override
//            public String convert(String str) {
//                return "["+str+"]";
//            }
//        });
//        System.out.println(r);
//    }
    public static String replaceDollarPlaceHolders(String s, StringConverter converter) {
//        return replacePlaceHolders(s, "${", "}", converter);
        // return replacePlaceHolders(s, "${", "}", converter);
        //faster default implementation
        Matcher matcher = DOLLAR_PLACE_HOLDER_PATTERN.matcher(s);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String name = matcher.group("name");
            String x = converter.convert(name);
            if (x == null) {
                x = "${" + name + "}";
            }
            matcher.appendReplacement(sb, Matcher.quoteReplacement(x));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String replacePlaceHolders(String s, String prefix, String suffix, StringConverter converter) {
        int i = 0;
        StringBuilder sb = new StringBuilder();
        while (i < s.length()) {
            int u = s.indexOf(prefix, i);
            if (u < 0) {
                sb.append(substring(s, i, s.length()));
                i = u;
                break;
            } else {
                sb.append(substring(s, i, u));
                i = u + prefix.length();
                u = s.indexOf(suffix, i);
                if (u <= 0) {
                    String var = substring(s, i, s.length());
                    sb.append(converter.convert(var));
                    i = u;
                    break;
                } else {
                    String var = substring(s, i, u);
                    sb.append(converter.convert(var));
                    i = u + suffix.length();
                }
            }
        }
        return sb.toString();
    }

    public static String substring(String full, int from, int to) {
        if (full == null) {
            full = "";
        }
        if (from < 0) {
            from = 0;
        }
        if (to >= full.length()) {
            to = full.length();
        }
        if (to <= from) {
            return "";
        }
        return full.substring(from, to);
    }

    public static String verboseStacktraceToString(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append(throwable.getClass().getName());
        sb.append(":");
        sb.append(throwable.getMessage());
        sb.append("\n");
        sb.append(stacktraceToString(throwable));
        return sb.toString();
    }

    public static String stacktraceToString(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }

    public static boolean matchesWildcardExpression(String str, String pattern) {
        return Pattern.compile(wildcardToRegex(pattern)).matcher(str == null ? "" : str).matches();
    }

    public static boolean matchesWildcardExpression(String str, String pattern, char pathSeparator) {
        return Pattern.compile(wildcardToRegex(pattern, pathSeparator)).matcher(str == null ? "" : str).matches();
    }

    public static <T> String listToString(Collection<T> objects, String separator) {
        return listToString(objects, separator, true, StringTransforms.TRIMMED_NOT_NULL);
    }

    public static <T> String listToString(Collection<T> objects, String separator, boolean noEmpty, StringTransform transform) {
        if (transform == null) {
            transform = StringTransforms.LOWER;
        }
        StringBuilder sb = new StringBuilder();
        if (objects != null) {
            for (Object v : objects) {
                if (v == null) {
                    v = "";
                }
                String s = transform.transform(v == null ? null : v.toString());
                if (!s.isEmpty() || !noEmpty) {
                    if (sb.length() > 0) {
                        sb.append(separator);
                    }
                    sb.append(s);
                }
            }
        }
        return sb.toString();
    }

    /**
     * https://karussell.wordpress.com/2011/04/14/longest-common-substring-algorithm-in-java/
     *
     * @param str1 first string
     * @param str2 second string
     * @return a substring with the longest common substring
     */
    public static String longestSubstring(String str1, String str2, boolean caseInsensitive) {

        StringBuilder sb = new StringBuilder();
        if (str1 == null || str1.isEmpty() || str2 == null || str2.isEmpty()) {
            return "";
        }

// ignore case
        if (caseInsensitive) {
            str1 = str1.toLowerCase();
            str2 = str2.toLowerCase();
        }
// java initializes them already with 0
        int[][] num = new int[str1.length()][str2.length()];
        int maxlen = 0;
        int lastSubsBegin = 0;

        for (int i = 0; i < str1.length(); i++) {
            for (int j = 0; j < str2.length(); j++) {
                if (str1.charAt(i) == str2.charAt(j)) {
                    if ((i == 0) || (j == 0)) {
                        num[i][j] = 1;
                    } else {
                        num[i][j] = 1 + num[i - 1][j - 1];
                    }

                    if (num[i][j] > maxlen) {
                        maxlen = num[i][j];
                        // generate substring from str1 => i
                        int thisSubsBegin = i - num[i][j] + 1;
                        if (lastSubsBegin == thisSubsBegin) {
                            //if the current LCS is the same as the last time this block ran
                            sb.append(str1.charAt(i));
                        } else {
                            //this block resets the string builder if a different LCS is found
                            lastSubsBegin = thisSubsBegin;
                            sb = new StringBuilder();
                            sb.append(str1.substring(lastSubsBegin, i + 1));
                        }
                    }
                }
            }
        }

        return sb.toString();
    }

    public static StringCollection createStringList(String separators, StringConverter keyConverter) {
        return createStringCollection(separators, false, true, keyConverter, keyConverter);
    }

    public static StringCollection createStringSet(String separators, StringConverter keyConverter) {
        return createStringCollection(separators, false, false, keyConverter, keyConverter);
    }

    public static StringCollection createStringMap(String separators, StringConverter keyConverter, StringConverter valueConverter) {
        return createStringCollection(separators, false, false, keyConverter, valueConverter);
    }

    public static StringCollection createStringCollection(String separators, boolean preserveOrder, boolean duplicates, StringConverter keyConverter, StringConverter valueConverter) {
        if (!Objects.equals(keyConverter, valueConverter)) {
            if (duplicates) {
                throw new IllegalArgumentException("Not sopported Mapping with distinct key/value converters");
            } else {
                if (preserveOrder) {
                    return new StringTokStringMap(new LinkedHashMap<String, String>(), separators, keyConverter, valueConverter);
                } else {
                    return new StringTokStringMap(new TreeMap<String, String>(), separators, keyConverter, valueConverter);
                }
            }
        }
        if (duplicates) {
            return new StringTokStringCollection(new ArrayList<String>(), separators, keyConverter);
        } else {
            if (preserveOrder) {
                return new StringTokStringCollection(new LinkedHashSet<String>(), separators, keyConverter);
            } else {
                return new StringTokStringCollection(new TreeSet<String>(), separators, keyConverter);
            }
        }
    }

    public static String[] removeDuplicates(String[] values) {
        LinkedHashSet<String> all = new LinkedHashSet<>();
        if (values != null) {
            Collections.addAll(all, values);
        }
        return all.toArray(new String[all.size()]);
    }

    public static String[] split(String value, String chars) {
        return split(value, chars, true, true);
    }

    public static String[] split(String value, String chars, boolean trim, boolean ignoreEmpty) {
        if (value == null) {
            value = "";
        }
        StringTokenizer st = new StringTokenizer(value, chars);
        List<String> all = new ArrayList<>();
        while (st.hasMoreElements()) {
            String s = st.nextToken();
            if (trim) {
                s = s.trim();
            }
            if (!ignoreEmpty || !s.isEmpty()) {
                all.add(s);
            }
        }
        return all.toArray(new String[all.size()]);
    }

    public static String cut(String value, int max) {
        return cut(value, max, null);
    }

    public static String cut(String value, int max, String etc) {
        if (value == null) {
            value = "";
        }
        if (value.length() > max) {
            if (etc == null) {
                etc = "...";
            }
            value = value.substring(0, max - 3) + etc;
        }
        return value;
    }

    public static String expand(String value, String chars, int min) {
        if (chars == null || chars.length() == 0) {
            chars = " ";
        }
        StringBuilder sb = new StringBuilder(value == null ? "" : value);
        while (sb.length() < min) {
            if (sb.length() + chars.length() <= min) {
                sb.append(chars);
            } else {
                int x = min - sb.length();
                if (x > chars.length()) {
                    x = chars.length();
                }
                sb.append(chars, 0, x);
            }
        }
        return sb.toString();
    }

    public static <T> String listToStringDeep(String separator, boolean noEmpty, StringTransform transform, Object... items) {
        List<Object> any = new ArrayList<>();
        for (Object item : items) {
            if (item != null) {
                if (item.getClass().isArray()) {
                    int max = Array.getLength(item);
                    for (int i = 0; i < max; i++) {
                        any.add(Array.get(item, i));
                    }
                } else if (item instanceof Collection) {
                    any.addAll(((Collection) item));
                } else {
                    any.add(item);
                }
            } else {
                any.add(null);
            }
        }
        return listToString(any, separator, noEmpty, transform);
    }

    public static String trimToNull(String str) {
        if (str == null) {
            return null;
        }
        String trimmed = str.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        return trimmed;
    }

    /**
     * return either a null string or a non empty string. If the object is not a
     * string, it is first converted to a string using String.valueOf(object);
     * if the resulting string is empty, null is returned
     *
     * @param any
     * @return
     */
    public static String trimObjectToNull(Object any) {
        if (any == null) {
            return null;
        }
        String trimmed = any.toString().trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        return trimmed;
    }

    public static String exceptionToString(Throwable ex) {
        for (Class aClass : new Class[]{
            NullPointerException.class,
            ArrayIndexOutOfBoundsException.class,
            ClassCastException.class,
            UnsupportedOperationException.class,
            ReflectiveOperationException.class,}) {
            if (aClass.isInstance(ex)) {
                return ex.toString();
            }
        }
        String message = ex.getMessage();
        if (message == null) {
            message = ex.toString();
        }
        return message;
    }

    public static String fillString(char x, int width) {
        char[] cc = new char[width];
        Arrays.fill(cc, x);
        return new String(cc);
    }

    public static String fillString(String pattern, int width) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("Empty Pattern");
        }
        char[] cc = new char[width];
        int len = pattern.length();
        for (int i = 0; i < cc.length; i++) {
            cc[i] = pattern.charAt(i % len);
        }
        return new String(cc);
    }

    public static String alignLeft(String s, int width) {
        StringBuilder sb = new StringBuilder();
        if (s != null) {
            sb.append(s);
            int x = width - sb.length();
            if (x > 0) {
                sb.append(fillString(' ', x));
            }
        }
        return sb.toString();
    }

    public static String alignRight(String s, int width) {
        StringBuilder sb = new StringBuilder();
        if (s != null) {
            sb.append(s);
            int x = width - sb.length();
            if (x > 0) {
                sb.insert(0, fillString(' ', x));
            }
        }
        return sb.toString();
    }

    public static String join(String sep, int[] items) {
        StringBuilder sb = new StringBuilder();
        if (items.length > 0) {
            sb.append(items[0]);
        }
        for (int i = 1; i < items.length; i++) {
            sb.append(sep);
            sb.append(items[i]);
        }
        return sb.toString();
    }

    public static String join(String sep, long[] items) {
        StringBuilder sb = new StringBuilder();
        if (items.length > 0) {
            sb.append(items[0]);
        }
        for (int i = 1; i < items.length; i++) {
            sb.append(sep);
            sb.append(items[i]);
        }
        return sb.toString();
    }

    public static String join(String sep, float[] items) {
        StringBuilder sb = new StringBuilder();
        if (items.length > 0) {
            sb.append(items[0]);
        }
        for (int i = 1; i < items.length; i++) {
            sb.append(sep);
            sb.append(items[i]);
        }
        return sb.toString();
    }

    public static String join(String sep, double[] items) {
        StringBuilder sb = new StringBuilder();
        if (items.length > 0) {
            sb.append(items[0]);
        }
        for (int i = 1; i < items.length; i++) {
            sb.append(sep);
            sb.append(items[i]);
        }
        return sb.toString();
    }

    public static String join(String sep, byte[] items) {
        StringBuilder sb = new StringBuilder();
        if (items.length > 0) {
            sb.append(items[0]);
        }
        for (int i = 1; i < items.length; i++) {
            sb.append(sep);
            sb.append(items[i]);
        }
        return sb.toString();
    }

    public static String join(String sep, boolean[] items) {
        StringBuilder sb = new StringBuilder();
        if (items.length > 0) {
            sb.append(items[0]);
        }
        for (int i = 1; i < items.length; i++) {
            sb.append(sep);
            sb.append(items[i]);
        }
        return sb.toString();
    }

    public static String join(String sep, char[] items) {
        StringBuilder sb = new StringBuilder();
        if (items.length > 0) {
            sb.append(items[0]);
        }
        for (int i = 1; i < items.length; i++) {
            sb.append(sep);
            sb.append(items[i]);
        }
        return sb.toString();
    }

    public static String join(String sep, String[] items) {
        return join(sep, Arrays.asList(items));
    }

    public static <T> String join(String sep, T[] items, ObjectToString<T> toStr) {
        return join(sep, Arrays.asList(items), toStr);
    }

    public static <T> String join(String sep, Collection<T> items, ObjectToString<T> toStr) {
        if (toStr == null) {
            toStr = new ObjectToString<T>() {
                @Override
                public String toString(T x) {
                    return String.valueOf(x);
                }
            };
        }
        StringBuilder sb = new StringBuilder();
        Iterator<T> i = items.iterator();
        if (i.hasNext()) {
            sb.append(toStr.toString(i.next()));
        }
        while (i.hasNext()) {
            sb.append(sep);
            sb.append(toStr.toString(i.next()));
        }
        return sb.toString();
    }

    public static String join(String sep, Collection<String> items) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> i = items.iterator();
        if (i.hasNext()) {
            sb.append(i.next());
        }
        while (i.hasNext()) {
            sb.append(sep);
            sb.append(i.next());
        }
        return sb.toString();
    }

    /**
     * @param message in the form "This is a message with ${param}
     * @param parameters
     * @return
     */
    public static String format(String message, Map<String, Object> parameters, MessageNameFormatContext messageNameFormatContext) {
        return new MessageNameFormat(message).format(parameters, messageNameFormatContext);
    }

    public static String literalToString(Object literal) {
        if (literal == null) {
            return "null";
        }
        if (literal instanceof String) {
            String theString = (String) literal;
            int len = theString.length();
            int bufLen = len * 2;
            if (bufLen < 0) {
                bufLen = Integer.MAX_VALUE;
            }
            StringBuilder sb = new StringBuilder(bufLen + 2);
            sb.append("\"");
            boolean escapeUnicode = true;
            for (int x = 0; x < len; x++) {
                char cc = theString.charAt(x);
                // Handle common case first, selecting largest block that
                // avoids the specials below
                if ((cc > 61) && (cc < 127)) {
                    if (cc == '\\') {
                        sb.append('\\');
                        sb.append('\\');
                        continue;
                    }
                    sb.append(cc);
                    continue;
                }
                switch (cc) {
                    case ' ':
//                        if (x == 0 || escapeSpace) {
//                            outBuffer.append('\\');
//                        }
                        sb.append(' ');
                        break;
                    case '\t':
                        sb.append('\\');
                        sb.append('t');
                        break;
                    case '\n':
                        sb.append('\\');
                        sb.append('n');
                        break;
                    case '\r':
                        sb.append('\\');
                        sb.append('r');
                        break;
                    case '\f':
                        sb.append('\\');
                        sb.append('f');
                        break;
//                    case '=': // Fall through
//                    case ':': // Fall through
//                    case '#': // Fall through
//                    case '!':
//                        outBuffer.append('\\');
//                        outBuffer.append(aChar);
//                        break;
                    default:
                        if (((cc < 0x0020) || (cc > 0x007e)) & escapeUnicode) {
                            sb.append('\\');
                            sb.append('u');
                            sb.append(toHex((cc >> 12) & 0xF));
                            sb.append(toHex((cc >> 8) & 0xF));
                            sb.append(toHex((cc >> 4) & 0xF));
                            sb.append(toHex(cc & 0xF));
                        } else {
                            sb.append(cc);
                        }
                }
            }

            sb.append("\"");
            return sb.toString();
        }
        if (literal instanceof Character) {
            return "'" + literal + '\'';
        }
        return String.valueOf(literal);
    }

    /**
     * Convert a nibble to a hex character
     *
     * @param nibble the nibble to convert.
     */
    private static char toHex(int nibble) {
        return hexDigit[(nibble & 0xF)];
    }

    /**
     * A table of hex digits
     */
    private static final char[] hexDigit = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    public static String toCapitalized(String name) {
        char[] chars = name.toLowerCase().toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    public static String toUncapitalized(String name) {
        char[] chars = name.toLowerCase().toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    /**
     * *
     * **
     *
     * @param pattern containing {1}
     * @return
     */
    public static Map<String, String> matchSubstring(String val, String pattern) {
        if (pattern == null) {
            pattern = "";
        }
        int i = 0;
        char[] cc = pattern.toCharArray();
        StringBuilder sb = new StringBuilder();
        Set<String> ret = new HashSet<>();
        while (i < cc.length) {
            char c = cc[i];
            switch (c) {
                case '.':
                case '!':
                case '$':
                case '[':
                case ']':
                case '(':
                case ')':
                case '?':
                case '^':
                case '*':
                case '\\': {
                    sb.append('\\').append(c);
                    break;
                }
                case '{': {
                    StringBuilder sb2 = new StringBuilder();
                    i++;
                    while (i < cc.length && cc[i] != '}') {
                        sb2.append(cc[i]);
                        i++;
                    }
                    sb.append("(?<").append(sb2).append(">(.*))");
                    if (ret.contains(sb2.toString())) {
                        throw new IllegalArgumentException("Already declared " + sb2);
                    }
                    ret.add(sb2.toString());
                    break;
                }
                default: {
                    sb.append(c);
                }
            }
            i++;
        }
        sb.append("$");
        sb.insert(0, "^");
        Pattern p = Pattern.compile(sb.toString());
        Matcher matcher = p.matcher(val);
        while (matcher.find()) {
            Map<String, String> ok = new HashMap<>();
            for (String k : ret) {
                ok.put(k, matcher.group(k));
            }
            return ok;
        }
        return null;
    }

    public static String replaceTail(String s, String oldTail, String newTail) {
        if (s.endsWith(oldTail)) {
            return s.substring(0, s.length() - oldTail.length()) + newTail;
        }
        throw new IllegalArgumentException("Missing old Tail");
    }

    public static String replaceHead(String s, String oldHead, String newHead) {
        if (s.startsWith(oldHead)) {
            return newHead + s.substring(oldHead.length());
        }
        throw new IllegalArgumentException("Missing old Head");
    }

    public static String formatLeft(Object number, int size) {
        String s = String.valueOf(number);
        int len = s.length();
        int bufferSize = Math.max(size, len);
        StringBuilder sb = new StringBuilder(bufferSize);
        sb.append(s);
        for (int i = bufferSize - len; i > 0; i--) {
            sb.append(' ');
        }
        return sb.toString();
    }

    public static String formatRight(Object number, int size) {
        String s = String.valueOf(number);
        int len = s.length();
        int bufferSize = Math.max(size, len);
        StringBuilder sb = new StringBuilder(bufferSize);
        for (int i = bufferSize - len; i > 0; i--) {
            sb.append(' ');
        }
        sb.append(s);
        return sb.toString();
    }

    public static Map<String, String> parseMap(String text, String entrySeparators) {
        return parseMap(text, "=", entrySeparators);
    }

    public static Map<String, String> parseMap(String text, String eqSeparators, String entrySeparators) {
        Map<String, String> m = new LinkedHashMap<>();
        StringReader reader = new StringReader(text == null ? "" : text);
        while (true) {
            StringBuilder key = new StringBuilder();
            int r = 0;
            try {
                r = readToken(reader, eqSeparators + entrySeparators, key);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String t = key.toString();
            if (r == -1) {
                if (!t.isEmpty()) {
                    m.put(t, null);
                }
                break;
            } else {
                char c = (char) r;
                if (eqSeparators.indexOf(c) >= 0) {
                    StringBuilder value = new StringBuilder();
                    try {
                        r = readToken(reader, entrySeparators, value);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    m.put(t, value.toString());
                    if (r == -1) {
                        break;
                    }
                } else {
                    //
                }
            }
        }
        return m;
    }

    public static int readToken(Reader reader, String stopTokens, StringBuilder result) throws IOException {
        while (true) {
            int r = reader.read();
            if (r == -1) {
                return -1;
            }
            if (r == '\"' || r == '\'') {
                char s = (char) r;
                while (true) {
                    r = reader.read();
                    if (r == -1) {
                        throw new RuntimeException("Expected " + '\"');
                    }
                    if (r == s) {
                        break;
                    }
                    if (r == '\\') {
                        r = reader.read();
                        if (r == -1) {
                            throw new RuntimeException("Expected " + '\"');
                        }
                        switch ((char) r) {
                            case 'n': {
                                result.append('\n');
                                break;
                            }
                            case 'r': {
                                result.append('\r');
                                break;
                            }
                            case 'f': {
                                result.append('\f');
                                break;
                            }
                            default: {
                                result.append((char) r);
                            }
                        }
                    } else {
                        char cr = (char) r;
                        result.append(cr);
                    }
                }
            } else {
                char cr = (char) r;
                if (stopTokens != null && stopTokens.indexOf(cr) >= 0) {
                    return cr;
                }
                result.append(cr);
            }
        }
    }

    public static StringBuilder clear(StringBuilder c) {
        return c.delete(0, c.length());
    }

    /**
     * code from org.apache.tools.ant.types.Commandline copyrights goes to
     * Apache Ant Authors (Licensed to the Apache Software Foundation (ASF))
     * Crack a command line.
     *
     * @param line the command line to process.
     * @return the command line broken into strings. An empty or null toProcess
     * parameter results in a zero sized array.
     */
    public static String[] parseCommandline(String line) {
        if (line == null || line.length() == 0) {
            //no command? no string
            return new String[0];
        }
        // parse with a simple finite state machine

        final int normal = 0;
        final int inQuote = 1;
        final int inDoubleQuote = 2;
        int state = normal;
        final StringTokenizer tok = new StringTokenizer(line, "\"\' ", true);
        final ArrayList<String> result = new ArrayList<String>();
        final StringBuilder current = new StringBuilder();
        boolean lastTokenHasBeenQuoted = false;

        while (tok.hasMoreTokens()) {
            String nextTok = tok.nextToken();
            switch (state) {
                case inQuote:
                    if ("\'".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true;
                        state = normal;
                    } else {
                        current.append(nextTok);
                    }
                    break;
                case inDoubleQuote:
                    if ("\"".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true;
                        state = normal;
                    } else {
                        current.append(nextTok);
                    }
                    break;
                default:
                    switch (nextTok) {
                        case "\'":
                            state = inQuote;
                            break;
                        case "\"":
                            state = inDoubleQuote;
                            break;
                        case " ":
                            if (lastTokenHasBeenQuoted || current.length() != 0) {
                                result.add(current.toString());
                                current.setLength(0);
                            }
                            break;
                        default:
                            current.append(nextTok);
                            break;
                    }
                    lastTokenHasBeenQuoted = false;
                    break;
            }
        }
        if (lastTokenHasBeenQuoted || current.length() != 0) {
            result.add(current.toString());
        }
        if (state == inQuote || state == inDoubleQuote) {
            throw new IllegalArgumentException("unbalanced quotes in " + line);
        }
        return result.toArray(new String[result.size()]);
    }

    public static String coalesce(String... cmd) {
        for (String string : cmd) {
            if (!isBlank(string)) {
                return string;
            }
        }
        return null;
    }

    private static Pattern SUB_PATTERN = Pattern.compile("£(?<index>[0-9]+)");

    public static Pattern2 compileSubPatterns2(String text, String... subPatterns) {
        Matcher matcher = SUB_PATTERN.matcher(text);
        StringBuilder sb = new StringBuilder();
        int pos = 0;
        while (matcher.find()) {
            sb.append(Pattern.quote(text.substring(pos, matcher.start())));
            String u = subPatterns[Integer.parseInt(matcher.group("index"))];
            sb.append('(');
            sb.append(u);
            sb.append(')');
            pos = matcher.end();
        }
        sb.append(Pattern.quote(text.substring(pos)));
        return Pattern2.compile(sb.toString());
    }

    /**
     * Builds a pattern where all the string is escaped but £? will be replaced
     * by valid patterns Example :
     * <pre>
     * StringUtils.compileSubPatterns("\"java.lang:type=Threading\".operations.dumpAllThreads[£0].threadName", "?&lt;Item&gt;[0-9]+")
     * </pre>
     *
     * @param text
     * @param subPatterns
     * @return
     */
    public static Pattern compileSubPatterns(String text, String... subPatterns) {
        Matcher matcher = SUB_PATTERN.matcher(text);
        StringBuilder sb = new StringBuilder();
        int pos = 0;
        while (matcher.find()) {
            sb.append(Pattern.quote(text.substring(pos, matcher.start())));
            String u = subPatterns[Integer.parseInt(matcher.group("index"))];
            sb.append('(');
            sb.append(u);
            sb.append(')');
            pos = matcher.end();
        }
        sb.append(Pattern.quote(text.substring(pos)));
        return Pattern.compile(sb.toString());
    }

}
