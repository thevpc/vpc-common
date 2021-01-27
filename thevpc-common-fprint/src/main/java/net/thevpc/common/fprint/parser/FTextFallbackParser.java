/**
 * ====================================================================
 *            Nuts : Network Updatable Things Service
 *                  (universal package manager)
 *
 * is a new Open Source Package Manager to help install packages
 * and libraries for runtime execution. Nuts is the ultimate companion for
 * maven (and other build managers) as it helps installing all package
 * dependencies at runtime. Nuts is not tied to java and is a good choice
 * to share shell scripts and other 'things' . Its based on an extensible
 * architecture to help supporting a large range of sub managers / repositories.
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/
package net.thevpc.common.fprint.parser;

import net.thevpc.common.fprint.TextFormat;
import net.thevpc.common.fprint.FormattedPrintStreamParser;
import net.thevpc.common.fprint.TextFormats;

import java.util.Objects;

/**
 * Created by vpc on 5/23/17.
 */
public class FTextFallbackParser implements FormattedPrintStreamParser {
    public static final FormattedPrintStreamParser INSTANCE=new FTextFallbackParser();

    private static class NodePattern {

        private String start;
        private String stop;
        private boolean complex;

        public NodePattern(String start, String stop, boolean complex) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hashCode(this.start);
            hash = 97 * hash + Objects.hashCode(this.stop);
            hash = 97 * hash + (this.complex ? 1 : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final NodePattern other = (NodePattern) obj;
            if (this.complex != other.complex) {
                return false;
            }
            if (!Objects.equals(this.start, other.start)) {
                return false;
            }
            if (!Objects.equals(this.stop, other.stop)) {
                return false;
            }
            return true;
        }

    }

    private static final NodePattern[] patterns = new NodePattern[]{
        new NodePattern("<<<<<<", ">>>>>>", true),
        new NodePattern("<<<<<", ">>>>>", true),
        new NodePattern("<<<<", ">>>>", true),
        new NodePattern("<<<", ">>>", true),
        new NodePattern("<<", ">>", true),
        new NodePattern("<", ">", true),
        new NodePattern("(((((((", ")))))))", true),
        new NodePattern("((((((", "))))))", true),
        new NodePattern("(((((", ")))))", true),
        new NodePattern("((((", "))))", true),
        new NodePattern("(((", ")))", true),
        new NodePattern("((", "))", true),
        new NodePattern("(", ")", true),
        new NodePattern("{{{", "}}}", true),
        new NodePattern("{{", "}}", true),
        new NodePattern("[[[", "]]]", true),
        new NodePattern("[[", "]]", true),
        new NodePattern("__", "__", true),
        new NodePattern("===", "===", true),
        new NodePattern("==", "==", true),
        new NodePattern("***", "***", true),
        new NodePattern("**", "**", true),
        new NodePattern("@@@", "@@@", true),
        new NodePattern("@@", "@@", true),
        new NodePattern("\"\"\"", "\"\"\"", false),
        new NodePattern("\"\"", "\"\"", false),
        new NodePattern("\"", "\"", false),
        new NodePattern("''", "''", false),
        new NodePattern("'", "'", false)
    };

    private static int[] findNext2(NodePattern[] patterns, char[] source, int index) {
        int expectedIndex = 0;
        char[][] startChars = new char[patterns.length][];
        char[][] endChars = new char[patterns.length][];
        for (int i = 0; i < startChars.length; i++) {
            startChars[i] = patterns[i].start.toCharArray();
            endChars[i] = patterns[i].stop.toCharArray();
        }
        while (expectedIndex < patterns.length) {
            int[] s = findNext(startChars, expectedIndex, source, index);
            if (s != null) {
                int[] e = findNext(new char[][]{endChars[s[0]]}, 0, source, s[1] + startChars[s[0]].length);
                if (e != null) {
                    return new int[]{s[0], s[1], e[1]};
                }
                expectedIndex = s[0] + 1;
            } else {
                break;
            }
        }
        return null;
    }

    private static int[] findNext(char[][] expected, int expectedIndex, char[] source, int index) {
        int[] pos = new int[expected.length];
        int best = -1;
        for (int i = expectedIndex; i < expected.length; i++) {
            pos[i] = findNext(expected[i], source, index);
            if (pos[i] >= 0) {
                if (best == -1 || pos[i] < pos[best]) {
                    best = i;
                }
            }
        }
        if (best >= 0) {
            return new int[]{best, pos[best]};
        }
        return null;
    }

    private static int findNext(char[] expected, char[] source, int index) {
        for (int i = index; i <= source.length - expected.length; i++) {
            if (source[i] == '\\') {
                i++;
            } else {
                if (expect(expected, source, i)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static boolean expect(char[] expected, char[] source, int index) {
        if (index + expected.length < source.length) {
            for (int i = 0; i < expected.length; i++) {
                if (expected[i] != source[index + i]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static String unescape(String text) {
        StringBuilder sb = new StringBuilder();
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '\\') {
                i++;
                if (i < chars.length) {
                    sb.append(chars[i]);
                } else {
                    sb.append(c);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public TextNode parse(String text) {
        TextNodeList found = new TextNodeList();
        if (text == null) {
            text = "";
        }
        char[] chars = text.toCharArray();
        int index = 0;
        while (index < chars.length) {
            int[] startLocation = findNext2(patterns, chars, index);
            if (startLocation == null) {
                //not found
                found.add(new TextNodePlain(unescape(new String(chars, index, chars.length - index))));
                index = chars.length;
            } else {
                String start = patterns[startLocation[0]].start;
                String stop = patterns[startLocation[0]].stop;
                int startPos = startLocation[1];
                int endPos = startLocation[2];
                if (startPos > index) {
                    found.add(new TextNodePlain(unescape(new String(chars, index, startPos - index))));
                }
                String styleName = start;
                TextFormat style = null;
                String text2 = unescape(new String(chars, startPos + start.length(), endPos - (startPos + start.length())));
                TextNodePlain p = new TextNodePlain(text2);

                switch (styleName) {
                    case "---": {
                        style = TextFormats.FG_BLUE;
                        break;
                    }
//                    case "[[[": {
//                        style = TextFormats.FG_MAGENTA;
//                        text2 = "[" + text2 + "]";
//                        break;
//                    }
                    case "[": {
                        style = TextFormats.BG_CYAN;
                        break;
                    }
                    case "[[": {
                        style = TextFormats.BG_BLUE;
                        break;
                    }
                    case "[[[": {
                        style = TextFormats.BG_MAGENTA;
                        break;
                    }
                    case "[[[[": {
                        style = TextFormats.BG_RED;
                        break;
                    }
                    case "[[[[[": {
                        style = TextFormats.BG_WHITE;
                        break;
                    }
                    case "[[[[[[": {
                        style = TextFormats.BG_BLACK;
                        break;
                    }
                    case "(": {
                        style = TextFormats.FG_CYAN;
                        break;
                    }
                    case "((": {
                        style = TextFormats.FG_BLUE;
                        break;
                    }
                    case "(((": {
                        style = TextFormats.FG_MAGENTA;
                        break;
                    }
                    case "((((": {
                        style = TextFormats.FG_RED;
                        break;
                    }
                    case "(((((": {
                        style = TextFormats.FG_WHITE;
                        break;
                    }
                    case "((((((": {
                        style = TextFormats.FG_BLACK;
                        break;
                    }
                    case "__": {
                        style = TextFormats.UNDERLINED;
                        break;
                    }
                    case "<<": {
                        style = TextFormats.REVERSED;
                        break;
                    }
                    case "'": {
                        style = TextFormats.BG_GREEN;
                        break;
                    }
                    case "\"": {
                        style = TextFormats.BG_GREEN;
                        break;
                    }
                    case "\"\"": {
                        style = TextFormats.BG_GREEN;
                        break;
                    }
                    case "\"\"\"": {
                        style = TextFormats.FG_GREEN;
                        text2 = "\"" + text2 + "\"";
                        break;
                    }
                    case "===": {
                        style = TextFormats.BG_RED;
                        break;
                    }
                    case "==": {
                        style = TextFormats.BG_BLUE;
                        break;
                    }
                    case "{{{": {
                        style = TextFormats.FG_YELLOW;
                        text2 = "{" + text2 + "}";
                        break;
                    }
                    case "{{": {
                        style = TextFormats.FG_YELLOW;
                        break;
                    }
                    case "***": {
                        style = TextFormats.FG_BLUE;
                        text2 = "*" + text2 + "*";
                        break;
                    }
                    case "**": {
                        style = TextFormats.FG_BLUE;
                        break;
                    }
                    case "@@@": {
                        style = TextFormats.FG_CYAN;
                        text2 = "@" + text2 + "@";
                        break;
                    }
                    case "@@": {
                        style = TextFormats.FG_CYAN;
                        break;
                    }
                    case "<{{": { //}} 
                        style = TextFormats.MOVE_LINE_START;
                        break;
                    }
                    default: {
                        style = null;
                        break;
                    }
                }

                found.add(new TextNodeStyled(style, p));
                index = endPos + stop.length();
            }
        }
        return found;
    }

    public String escapeText(String str) {
        return DefaultFormattedPrintStreamParser.INSTANCE.escapeText(str);
    }

}
