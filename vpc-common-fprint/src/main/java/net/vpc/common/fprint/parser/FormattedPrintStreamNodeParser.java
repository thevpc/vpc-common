/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.fprint.parser;

import net.vpc.common.fprint.util.FormattedPrintStreamUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vpc
 */
public class FormattedPrintStreamNodeParser {

    public static final FormattedPrintStreamNodeParser INSTANCE = new FormattedPrintStreamNodeParser();

    private TokResult parse(char[] s, int index, String exit) {
        List<FDocNode> all = new ArrayList<FDocNode>();
        StringBuilder curr = new StringBuilder();
        for (int i = index; i < s.length; i++) {
            if (isValue(s, i, exit)) {
                consumePhrase(curr, all);
                FDocNode item = all.size() == 1 ? all.get(0) : new FDocNode.List(all.toArray(new FDocNode[all.size()]));
                return new TokResult(item, i + exit.length() - index);
            }
            char c = s[i];
            switch (c) {
                case '`':
                case '"':
                case '\'': {
                    consumePhrase(curr, all);
                    for (int cc = 4; cc >= 1; cc--) {
                        if (isSuite(s, i, c, cc, true, exit)) {
                            i = i + cc;
                            StringBuilder sb = new StringBuilder();
                            while (i < s.length) {
                                if (s[i] == '\\') {
                                    if (i + 1 < s.length) {
                                        sb.append(s[i + 1]);
                                        i++;
                                    } else {
                                        sb.append('\\');
                                        break;
                                    }
                                    i++;
                                } else if (isSuite(s, i, c, cc, false, null)) {
                                    i = i + cc - 1;
                                    break;
                                } else {
                                    sb.append(s[i]);
                                    i++;
                                }
                            }
                            String type = FormattedPrintStreamUtils.fillString(c, cc);
                            all.add(new FDocNode.Escaped(
                                    type,
                                    type,
                                    sb.toString()
                            ));
                            break;
                        }
                    }
                    break;
                }
                case '$':
                case '£':
                case '§':
                case '_':
                case '~':
                case '%':
                case '¤':
                case '@':
                case '^':
                case '#':
                case '¨':
                case '=':
                case '*':
                case '+':
                case '(':
                case '[':
                case '{':
                case '<': {
                    TokInfo d = info(c);
                    boolean okkay = false;
                    for (int cc = d.max; cc >= d.min; cc--) {
                        if (isSuite(s, i, c, cc, true, null)) {
                            consumePhrase(curr, all);
                            String start2 = FormattedPrintStreamUtils.fillString(c, cc);
                            String exit2 = FormattedPrintStreamUtils.fillString(d.end, cc);
                            TokResult v = parse(s, i + cc, exit2);
                            all.add(new FDocNode.Typed(start2, exit2, v.node));
                            i = i + cc + v.consumedCount - 1;
                            okkay = true;
                            break;
                        }
                    }
                    if (!okkay) {
                        curr.append(c);
                    }
                    break;
                }
                case '\\': {
                    if (s[i] == '\\') {
                        if (i + 1 < s.length) {
                            curr.append(s[i + 1]);
                            i++;
                        } else {
                            curr.append('\\');
                        }
                    }
                    break;
                }
                default: {
                    curr.append(c);
                }
            }
        }
        consumePhrase(curr, all);
        FDocNode item = all.size() == 1 ? all.get(0) : new FDocNode.List(all.toArray(new FDocNode[all.size()]));
        return new TokResult(item, s.length - index);
    }

    private boolean isValue(char[] line, int from, String exit) {
        if (exit == null) {
            return false;
        }
        for (int i = 0; i < exit.length(); i++) {
            if (i + from < line.length) {
                if (line[i + from] != exit.charAt(i)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean isSuite(char[] line, int from, char c, int count, boolean hard, String exit) {
        for (int i = 0; i < count; i++) {
            if (i + from < line.length) {
                if (line[i + from] != c) {
                    return false;
                }
            } else {
                return false;
            }
        }
        if (exit != null && exit.length() == count && new String(line, from, count).equals(exit)) {
            return false;
        }
        if (!hard) {
            return true;
        }
        if (hard && count + from < line.length) {
            if (line[count + from] != c) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    private void consumePhrase(StringBuilder curr, List<FDocNode> all) {
        if (curr.length() > 0) {
            FDocNode tok = new FDocNode.Plain(curr.toString());
            all.add(tok);
            curr.delete(0, curr.length());
        }
    }


    public FDocNode parse(String str) {
        return parse(str.toCharArray(), 0, null).node;
    }


    private TokInfo info(char c) {
        switch (c) {
            case '<':
                return new TokInfo(1, 4, '>');
            case '(':
                return new TokInfo(1, 4, ')');
            case '[':
                return new TokInfo(1, 4, ']');
            case '{':
                return new TokInfo(1, 4, '}');
            case '_':
            case '~':
            case '%':
            case '¤':
            case '@':
            case '^':
            case '#':
            case '¨':
            case '=':
            case '*':
            case '+':
            case '$':
            case '£':
            case '§': {
                return new TokInfo(2, 4, c);
            }
        }
        //unexpected!
        return new TokInfo(2, 4, c);
    }
}
