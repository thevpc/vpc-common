/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.strings;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.Normalizer;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author taha.bensalah@gmail.com
 */
public class StringUtils {

    public static String normalize(String expression) {
        if (expression == null) {
            return null;
        }

        String nfdNormalizedString = Normalizer.normalize(expression, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    /**
     * return either a null string or a non empty string. If the object is not a
     * string, it is first converted to a string using String.valueOf(object);
     * if the resulting string is empty, null is returned
     *
     * @param object
     * @return
     */
    public static String nonEmpty(Object object) {
        String v = null;
        if (object == null) {
            return null;
        } else {
            v = String.valueOf(object);
        }
        if (isEmpty(v)) {
            return null;
        }
        return v;
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

    public static boolean isEmpty(String string) {
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
        if(string==null){
            string="";
        }
        if(word==null){
            word="";
        }
        if(word.length()==0){
            return -1;
        }
        int slength = string.length();
        int wlength = word.length();
        while (true) {
            int i = string.indexOf(word, currFrom);
            if (i >= 0) {
                if (
                        (i == 0 || Character.isWhitespace(string.charAt(i - 1)) || ponctuations.indexOf(string.charAt(i - 1)) >= 0)
                                && ((i + wlength) >= slength || Character.isWhitespace(string.charAt(i + wlength))
                                || ponctuations.indexOf(string.charAt(i + wlength)) >= 0)
                        ) {
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
                case '!':
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
     * @param pattern       simple shell like
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
                case '!':
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
        return replacePlaceHolders(s, "${", "}", converter);
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
        for (Object v : objects) {
            String s = transform == null ? String.valueOf(v) : transform.transform(v == null ? null : v.toString());
            if (!s.isEmpty() || !noEmpty) {
                if (sb.length() > 0) {
                    sb.append(separator);
                }
                sb.append(s);
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
        if (str1 == null || str1.isEmpty() || str2 == null || str2.isEmpty())
            return "";

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
                    if ((i == 0) || (j == 0))
                        num[i][j] = 1;
                    else
                        num[i][j] = 1 + num[i - 1][j - 1];

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
}
