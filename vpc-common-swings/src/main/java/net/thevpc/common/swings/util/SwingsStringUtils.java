/**
 * ====================================================================
 * vpc-commons library
 *
 * Description: <start><end>
 *
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swings.util;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 3 janv. 2006 21:40:14
 */
public final class SwingsStringUtils {

    public static boolean isCapitalized(String str) {
        return str.length() > 1
                && str.charAt(0) == Character.toUpperCase(str.charAt(0))
                && str.substring(1).equals(str.substring(1));
    }

    public static String copyFormat(String partword, String fullword) {
        if (partword.equals(partword.toLowerCase())) {
            return fullword.toLowerCase();
        } else if (partword.equals(partword.toUpperCase())) {
            return fullword.toUpperCase();
        } else if (isCapitalized(partword)) {
            return capitalize(fullword);
        }
        return fullword;
    }

    public static String capitalize(String str) {
        StringBuilder sb = new StringBuilder();
        boolean capital = true;
        for (int i = 0; i < str.length(); i++) {
            sb.append(
                    capital ? Character.toUpperCase(str.charAt(i))
                            : Character.toLowerCase(str.charAt(i)));
            capital = str.charAt(i) == ' ';
        }
        return sb.toString();
    }

    public static String textToHtml(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i)) {
                case 34: // '"'
                case 38: // '&'
                case 39: // '\''
                case 60: // '<'
                case 62: // '>'
                    sb.append("&#").append((int) str.charAt(i)).append(";");
                    break;
                case '\n': // '>'
                    sb.append("<br>");
                    break;

                default:
                    sb.append(str.charAt(i));
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * The same as String.substring but will never throw Exception on invalid
     * indexes but return ""
     *
     * @param string
     * @param start
     * @param end
     * @return
     */
    public static String substring(String string, int start, int end) {
        if (string == null || string.length() == 0) {
            return "";
        }
        if (start < 0) {
            start = 0;
        }
        if (end > string.length()) {
            end = string.length();
        }
        if (end <= start) {
            return "";
        } else {
            return string.substring(start, end);
        }
    }

    private SwingsStringUtils() {
    }

    public static String replaceChars(String string, int startOffset, int endOffset, String olds, String news) {
        String ret = substring(string, startOffset, endOffset);
        return substring(string, 0, startOffset) + replaceChars(ret, olds, news) + substring(string, endOffset);
    }

    public static String replaceChars(String str, String olds, String news) {
        String ret = str;
        int j;
        while ((j = ret.indexOf(olds)) >= 0) {
            ret = substring(ret, 0, j) + news + substring(ret, j + olds.length());
        }
        return ret;
    }

    public static String replaceString(String string, String oldPortion, String newPotion) {
        String x = "";
        for (int i = 0; i < string.length(); ) {
            String souschaine = substring(string, i, i + oldPortion.length());
            if (oldPortion.equals(souschaine)) {
                x = x + newPotion;
                i += oldPortion.length();
            } else {
                x = x + string.charAt(i);
                i++;
            }
        }

        return x;
    }

    public static String insertStringAt(String mainString, int pos, String portion) {
        return substring(mainString, 0, pos) + portion + substring(mainString, pos);
    }

    public static int lastIndexOfOneOf(String string, String chars, int index) {
        int max = -1;
        for (int i = 0; i < chars.length(); i++) {
            int x = string.lastIndexOf(chars.charAt(i), index);
            if (x > max) {
                max = x;
            }
        }

        return max;
    }

    public static String substring(String string, int start) {
        if (string == null) {
            return "";
        } else {
            return substring(string, start, string.length());
        }
    }

    public static String[] split(String string, char separator, boolean first) {
        if (string == null || string.length() == 0) {
            return null;
        }
        int i = first ? string.indexOf(separator) : string.lastIndexOf(separator);
        if (i < 0) {
            return null;
        } else {
            String[] r = new String[2];
            r[0] = substring(string, 0, i);
            r[1] = substring(string, i + 1);
            return r;
        }
    }

    public static String[] tokenize(String string, String delim) {
        ArrayList v = new ArrayList();
        StringTokenizer st = new StringTokenizer(string, delim);
        while (st.hasMoreTokens()) {
            v.add(st.nextToken());
        }
        return (String[]) v.toArray(new String[v.size()]);
    }

    public static String left(String string, char separator, boolean first) {
        String[] s = split(string, separator, first);
        return s != null ? s[1] : null;
    }

    public static String right(String string, char separator, boolean first) {
        String[] s = split(string, separator, first);
        return s != null ? s[0] : null;
    }

    public static String replace(String string, String olds, String news) {
        String ret = string;
        int j;
        while ((j = ret.indexOf(olds)) >= 0) {
            ret = substring(ret, 0, j) + news + substring(ret, j + olds.length());
        }
        return ret;
    }

    public static String replace(String string, int startOffset, int endOffset, String olds, String news) {
        String ret = substring(string, startOffset, endOffset);
        return substring(string, 0, startOffset) + replace(ret, olds, news) + substring(string, endOffset);
    }

    public static String removeStartingWhites(String string, String whites, String newLineDelimiters) {
        StringTokenizer st = new StringTokenizer(string, newLineDelimiters);
        StringBuilder ret = new StringBuilder();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            int i;
            for (i = 0; i < token.length(); i++) {
                if (whites.indexOf(token.charAt(i)) < 0) {
                    break;
                }
            }

            if (i == 0) {
                ret.append(token).append(newLineDelimiters.charAt(0));
            } else {
                ret.append(substring(token, i)).append(newLineDelimiters.charAt(0));
            }
        }
        return ret.toString();
    }

    public static String nonull(String string) {
        return string == null ? "" : string;
    }

    public static boolean isEmpty(String id) {
        return id==null || id.trim().isEmpty();
    }

    public static class SplitResult{
        private String value;
        private boolean delimiter;

        public SplitResult(String value, boolean delimiter) {
            this.value = value;
            this.delimiter = delimiter;
        }

        public String getValue() {
            return value;
        }

        public boolean isDelimiter() {
            return delimiter;
        }

        @Override
        public String toString() {
            return  delimiter?("Delimiter{"+value+"}"):("Value{"+value+"}");
        }
    }

    public static SplitResult[] split(String input, String regexp) {
        int limit = 0;
        int index = 0;
        boolean matchLimited = limit > 0;
        ArrayList<SplitResult> matchList = new ArrayList<SplitResult>();
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(input);

        // Add segments before each match found
        while (m.find()) {
            if (!matchLimited || matchList.size() < limit - 1) {
                String match = input.subSequence(index, m.start()).toString();
                if (match.length() > 0) {
                    matchList.add(new SplitResult(match,false));
                }
                index = m.end();
            } else if (matchList.size() == limit - 1) { // last one
                String match = input.subSequence(index,
                        input.length()).toString();
                matchList.add(new SplitResult(match,false));
                index = m.end();
            }
            matchList.add(new SplitResult(m.group(),true));
        }

        // If no match was found, return this
        if (index == 0){
            if(input.length()==0){
                return new SplitResult[0];
            }
            return new SplitResult[]{new SplitResult(input.toString(),false)};
        }
        // Add remaining segment
        if (!matchLimited || matchList.size() < limit)
            matchList.add(new SplitResult(input.subSequence(index, input.length()).toString(),false));

        // Construct result
        int resultSize = matchList.size();
        if (limit == 0){
            while (resultSize > 0 && matchList.get(resultSize - 1).getValue().equals("")){
                resultSize--;
            }
        }
        SplitResult[] result = new SplitResult[resultSize];
        return matchList.subList(0, resultSize).toArray(result);

    }

    public static boolean isInteger(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch(Exception any){
            return false;
        }
    }

    public static boolean isDouble(String s){
        try{
            Double.parseDouble(s);
            return true;
        }catch(Exception any){
            return false;
        }
    }
}
