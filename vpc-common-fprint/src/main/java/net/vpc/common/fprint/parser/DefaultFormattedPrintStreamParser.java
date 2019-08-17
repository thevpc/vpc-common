/**
 * ====================================================================
 * Nuts : Network Updatable Things Service
 * (universal package manager)
 * <p>
 * is a new Open Source Package Manager to help install packages
 * and libraries for runtime execution. Nuts is the ultimate companion for
 * maven (and other build managers) as it helps installing all package
 * dependencies at runtime. Nuts is not tied to java and is a good choice
 * to share shell scripts and other 'things' . Its based on an extensible
 * architecture to help supporting a large range of sub managers / repositories.
 * <p>
 * Copyright (C) 2016-2017 Taha BEN SALAH
 * <p>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.vpc.common.fprint.parser;

import net.vpc.common.fprint.TextFormat;
import net.vpc.common.fprint.FormattedPrintStreamParser;
import net.vpc.common.fprint.TextFormats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vpc on 5/23/17.
 */
public class DefaultFormattedPrintStreamParser implements FormattedPrintStreamParser {

    public static final DefaultFormattedPrintStreamParser INSTANCE = new DefaultFormattedPrintStreamParser();
    private static final Logger log = Logger.getLogger(DefaultFormattedPrintStreamParser.class.getName());

    private static TextNode convert(List<FDocNode> n) {
        if (n.size() == 1) {
            return convert(n.get(0));
        }
        List<TextNode> children = new ArrayList<>(n.size());
        for (FDocNode node : n) {
            children.add(convert(node));
        }
        return new TextNodeList(children.toArray(new TextNode[0]));
    }

    public static TextNode convert(FDocNode n) {
        if (n != null) {
            if (n instanceof FDocNode.Plain) {
                FDocNode.Plain p = (FDocNode.Plain) n;
                return new TextNodePlain(p.getValue());
            }
            if (n instanceof FDocNode.Escaped) {
                FDocNode.Escaped p = (FDocNode.Escaped) n;
                switch (p.getStart()) {
                    case "\"":
                    case "\"\"":
                    case "\"\"\"":
                    case "'":
                    case "''":
                    case "'''": {
                        return wrap(convert(new FDocNode.Plain(p.getValue())), p.getStart(), p.getEnd(), TextFormats.FG_GREEN);
                    }
                    case "``": {
                        // this a plain text!
                        return new TextNodePlain(p.getValue());
                    }
                    case "```": {
                        //this is a comment ?
                        return wrap(convert(new FDocNode.Plain(p.getValue())), p.getStart(), p.getEnd(), TextFormats.FG_GREEN);
                    }
                    case "`": {
                        //this a command !!
                        //should be interpreted as
                        String v = p.getValue().trim();
                        List<TextNode> nodes = new ArrayList<TextNode>();
                        for (String cmd : v.split(";")) {
                            if ("move-line-start".endsWith(cmd)) {
                                nodes.add(new TextNodeCommand(TextFormats.MOVE_LINE_START));
                            } else if ("move-up".endsWith(cmd)) {
                                nodes.add(new TextNodeCommand(TextFormats.MOVE_UP));
                            }
                        }
                        return new TextNodeList(nodes.toArray(new TextNode[nodes.size()]));
                    }
                }

                return new TextNodePlain(p.getValue());
            }
            if (n instanceof FDocNode.List) {
                FDocNode.List p = (FDocNode.List) n;
                FDocNode[] children = p.getValues();
                if (children.length == 1) {
                    return convert(children[0]);
                }
                return convert(Arrays.asList(children));
            }
            if (n instanceof FDocNode.Typed) {
                FDocNode.Typed p = (FDocNode.Typed) n;
                switch (p.getStart()) {
                    case "(": {
                        return wrap(convert(p.getNode()), "(", ")", null);
                    }
                    case "[": {
                        return wrap(convert(p.getNode()), "[", "]", null);
                    }
                    case "{": {
                        return wrap(convert(p.getNode()), "{", "}", null);
                    }
                    case "__":
                    case "___":
                    case "____": {
                        return new TextNodeStyled(TextFormats.UNDERLINED, convert(p.getNode()));
                    }
                    case "//":
                    case "///":
                    case "////": {
                        return new TextNodeStyled(TextFormats.ITALIC, convert(p.getNode()));
                    }
                    case "~~":
                    case "~~~":
                    case "~~~~": {
                        return new TextNodeStyled(TextFormats.STRIKED, convert(p.getNode()));
                    }
                    case "%%":
                    case "%%%":
                    case "%%%%": {
                        return new TextNodeStyled(TextFormats.REVERSED, convert(p.getNode()));
                    }
                    case "==": {
                        return new TextNodeStyled(TextFormats.FG_BLUE, convert(p.getNode()));
                    }
                    case "===": {
                        return new TextNodeStyled(TextFormats.BG_BLUE, convert(p.getNode()));
                    }
                    case "====": {
                        return new TextNodeStyled(TextFormats.FG_BLUE, convert(p.getNode()));
                    }
                    case "**":
                    case "***":
                    case "****": {
                        return new TextNodeStyled(TextFormats.FG_CYAN, convert(p.getNode()));
                    }
                    case "##": {
                        return new TextNodeStyled(TextFormats.FG_GREEN, convert(p.getNode()));
                    }
                    case "###": {
                        return new TextNodeStyled(TextFormats.BG_GREEN, convert(p.getNode()));
                    }
                    case "####": {
                        return new TextNodeStyled(TextFormats.FG_GREEN, convert(p.getNode()));
                    }
                    case "@@": {
                        return new TextNodeStyled(TextFormats.FG_RED, convert(p.getNode()));
                    }
                    case "@@@": {
                        return new TextNodeStyled(TextFormats.BG_RED, convert(p.getNode()));
                    }
                    case "@@@@": {
                        return new TextNodeStyled(TextFormats.FG_RED, convert(p.getNode()));
                    }
                    case "[[": {
                        return new TextNodeStyled(TextFormats.FG_MAGENTA, convert(p.getNode()));
                    }
                    case "[[[": {
                        return new TextNodeStyled(TextFormats.BG_MAGENTA, convert(p.getNode()));
                    }
                    case "[[[[": {
                        return new TextNodeStyled(TextFormats.FG_MAGENTA, convert(p.getNode()));
                    }

                    case "{{": {
                        return new TextNodeStyled(TextFormats.FG_YELLOW, convert(p.getNode()));
                    }
                    case "{{{": {
                        return new TextNodeStyled(TextFormats.BG_YELLOW, convert(p.getNode()));
                    }
                    case "{{{{": {
                        return new TextNodeStyled(TextFormats.FG_YELLOW, convert(p.getNode()));
                    }

                    case "++":
                    case "+++":
                    case "++++": {
                        return new TextNodeStyled(TextFormats.BG_GREEN, convert(p.getNode()));
                    }

                    case "^^": {
                        return new TextNodeStyled(TextFormats.FG_BLUE, convert(p.getNode()));
                    }
                    case "^^^": {
                        return new TextNodeStyled(TextFormats.BG_BLUE, convert(p.getNode()));
                    }
                    case "^^^^": {
                        return new TextNodeStyled(TextFormats.FG_BLUE, convert(p.getNode()));
                    }
                    case "((": {
                        return new TextNodeStyled(TextFormats.FG_BLUE, convert(p.getNode()));
                    }
                    case "(((": {
                        return new TextNodeStyled(TextFormats.BG_CYAN, convert(p.getNode()));
                    }
                    case "((((": {
                        return new TextNodeStyled(TextFormats.FG_BLUE, convert(p.getNode()));
                    }
                    case "<<": {
                        return new TextNodeStyled(TextFormats.FG_GREY, convert(p.getNode()));
                    }
                    case "<<<": {
                        return new TextNodeStyled(TextFormats.BG_GREY, convert(p.getNode()));
                    }
                    case "<<<<": {
                        return new TextNodeStyled(TextFormats.FG_GREY, convert(p.getNode()));
                    }
                    case "$$": {
                        return new TextNodeStyled(TextFormats.FG_MAGENTA, convert(p.getNode()));
                    }
                    case "$$$": {
                        return new TextNodeStyled(TextFormats.BG_MAGENTA, convert(p.getNode()));
                    }
                    case "$$$$": {
                        return new TextNodeStyled(TextFormats.BG_MAGENTA, convert(p.getNode()));
                    }
                    case "££": {
                        return new TextNodeStyled(TextFormats.FG_RED, convert(p.getNode()));
                    }
                    case "£££": {
                        return new TextNodeStyled(TextFormats.BG_RED, convert(p.getNode()));
                    }
                    case "££££": {
                        return new TextNodeStyled(TextFormats.FG_RED, convert(p.getNode()));
                    }
                    case "§§": {
                        return new TextNodeStyled(TextFormats.FG_WHITE, convert(p.getNode()));
                    }
                    case "§§§": {
                        return new TextNodeStyled(TextFormats.BG_WHITE, convert(p.getNode()));
                    }
                    case "§§§§": {
                        return new TextNodeStyled(TextFormats.FG_WHITE, convert(p.getNode()));
                    }
                }
                TextNode convert = convert(p.getNode());
                if (convert instanceof TextNodePlain) {
                    return new TextNodePlain(((TextNodePlain) convert).getValue());
                } else {
                    return new TextNodePlain(convert.toString());
                }
//                return new TextNodePlain(String.valueOf(n.toString()));
            }
        }
        return new TextNodePlain(String.valueOf(n == null ? null : n.toString()));
    }

    private static TextNode wrap(TextNode t, String prefix, String suffix, TextFormat format) {
        TextNodeList y = new TextNodeList(
                new TextNodePlain(prefix),
                t,
                new TextNodePlain(suffix)
        );
        if (format == null) {
            return y;
        }
        return new TextNodeStyled(format, y);
    }

    FDocNode parseTextNode(String text) {
        return FormattedPrintStreamNodeParser.INSTANCE.parse(text);
    }

    private void escape(FDocNode tn, StringBuilder sb) {
        if (tn instanceof FDocNode.Plain) {
            sb.append(((FDocNode.Plain) tn).getValue());
        } else if (tn instanceof FDocNode.List) {
            for (FDocNode fDocNode : ((FDocNode.List) tn).getValues()) {
                escape(fDocNode, sb);
            }
        } else if (tn instanceof FDocNode.Typed) {
            escape(((FDocNode.Typed) tn).getNode(), sb);
        } else if (tn instanceof FDocNode.Escaped) {
            sb.append(((FDocNode.Escaped) tn).getValue());
        } else {
            throw new IllegalArgumentException("Unsupported");
        }
    }

    public String filterText(String text) {
        if (text == null) {
            text = "";
        }
        StringBuilder sb = new StringBuilder();
        try {
            FDocNode tn = FormattedPrintStreamNodeParser.INSTANCE.parse(text);
            escape(tn, sb);
            return sb.toString();
        } catch (Exception ex) {
            log.log(Level.FINEST, "Error parsing : \n" + text, ex);
            return text;
        }
    }

    @Override
    public TextNode parse(String text) {
        try {

            FDocNode tn = FormattedPrintStreamNodeParser.INSTANCE.parse(text);
            return convert(tn);
        } catch (Exception ex) {
            log.log(Level.FINEST, "Error parsing : \n" + text, ex);
            return new TextNodePlain(text);
        }
    }

    @Override
    public String escapeText(String str) {
        if (str == null) {
            return str;
        }
        StringBuilder sb=new StringBuilder(str.length());
        for (char c : str.toCharArray()) {
            switch (c){
                case '\"':
                case '\'':
                case '`':
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
                case '<':
                case '\\':{
                    sb.append('\\').append(c);
                    break;
                }
                default:{
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
}
