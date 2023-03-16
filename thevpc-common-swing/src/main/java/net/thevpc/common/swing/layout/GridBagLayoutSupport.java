/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
 *
 * <br>
 *
 * Copyright [2020] [thevpc] Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <br> ====================================================================
 */
package net.thevpc.common.swing.layout;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import net.thevpc.common.swing.util._IOUtils;

/**
 * @author thevpc Date: 12 janv. 2005 Time: 19:41:17
 */
public class GridBagLayoutSupport {

    public static GridBagLayoutSupport of(String pattern) {
        return new GridBagLayoutSupport(pattern);
    }

    private String pattern;
    private HashMap<String, GridBagConstraintsHolder> constraintsMap = new HashMap<String, GridBagConstraintsHolder>();

    private void runGlobalFunction(String name, Args args) {
        switch (name) {
            case "insets":
            case "i": {
                runGlobalFunctionInsets(args);
                return;
            }
            case "pads":
            case "pad":
            case "p": {
                runGlobalFunctionPads(args);
                return;
            }
        }
        throw new IllegalArgumentException("unknown global function " + name);
    }

    private void runFunction(String name, ConstrBuilder b, Args args) {
        switch (name) {
            case "insets":
            case "i": {
                runFunctionInsets(b, args);
                return;
            }
            case "label":
            case "l": {
                runFunctionLabel(b, args);
                return;
            }
            case "glue":
            case "g": {
                runFunctionGlue(b, args);
                return;
            }
            case "strut":
            case "s": {
                runFunctionStrut(b, args);
                return;
            }
            case "pads":
            case "pad":
            case "p": {
                runFunctionPads(b, args);
                return;
            }
        }
        throw new IllegalArgumentException("unknown function " + name);
    }

    private void runFunctionLabel(ConstrBuilder b, Args args) {
        switch (args.size()) {
            case 0: {
                b.component = new JLabel();
                return;
            }
            case 1: {
                if (args.get(0).isString()) {
                    b.component = new JLabel(args.get(0).getString());
                    return;
                }
            }
        }
        throw new IllegalArgumentException("label(name) : invalid arguments");
    }

    private void runFunctionGlue(ConstrBuilder b, Args args) {
        switch (args.size()) {
            case 0: {
                b.component = Box.createGlue();
                return;
            }
            case 1: {
                switch (args.get(0).getString().toLowerCase()) {
                    case "h":
                    case "horizontal": {
                        b.component = Box.createHorizontalGlue();
                        return;
                    }
                    case "v":
                    case "vertical": {
                        b.component = Box.createVerticalGlue();
                        return;
                    }
                }
                break;
            }
        }
        throw new IllegalArgumentException("glue expects only one argument");
    }

    private void runFunctionStrut(ConstrBuilder b, Args args) {
        switch (args.size()) {
            case 1: {
                if (args.get(0).isNumber()) {
                    b.component = Box.createHorizontalStrut(args.get(0).getInt());
                    return;
                }
                break;
            }
            case 2: {
                if (args.get(0).isNumber() && args.get(1).isNumber()) {
                    if (args.get(0).getInt() == 0) {
                        b.component = Box.createVerticalStrut(args.get(1).getInt());
                        return;
                    } else if (args.get(1).getInt() == 0) {
                        b.component = Box.createHorizontalStrut(args.get(0).getInt());
                        return;
                    } else {
                        b.component = Box.createRigidArea(new Dimension(args.get(0).getInt(), args.get(1).getInt()));
                        return;
                    }
                } else if (args.get(0).isNumber() && args.get(1).isString()) {
                    switch (args.get(1).getString().toLowerCase()) {
                        case "h":
                        case "horizontal": {
                            b.component = Box.createHorizontalStrut(args.get(0).getInt());
                            return;
                        }
                        case "v":
                        case "vertical": {
                            b.component = Box.createVerticalStrut(args.get(0).getInt());
                            return;
                        }
                    }
                } else if (args.get(1).isNumber() && args.get(0).isString()) {
                    switch (args.get(0).getString().toLowerCase()) {
                        case "h":
                        case "horizontal": {
                            b.component = Box.createHorizontalStrut(args.get(1).getInt());
                            return;
                        }
                        case "v":
                        case "vertical": {
                            b.component = Box.createVerticalStrut(args.get(1).getInt());
                            return;
                        }
                    }
                }
                break;
            }
        }
        throw new IllegalArgumentException("strut(width[,height]) |  strut(val,h/v) : invalid arguments:" + args);
    }

    private Pads createPads(Args args) {
        switch (args.size()) {
            case 0: {
                return new Pads(2, 2);
            }
            case 1: {
                if (args.get(0).isNumber()) {
                    int i = args.get(0).getInt();
                    return new Pads(i, i);
                }
                break;
            }
            case 2: {
                if (args.get(0).isNumber() && args.get(1).isNumber()) {
                    int i = args.get(0).getInt();
                    int j = args.get(1).getInt();
                    return new Pads(i, j);
                }
                break;
            }
        }
        throw new IllegalArgumentException("pad(w[,h]) : invalid arguments");

    }

    private Insets createInsets(Args args) {
        switch (args.size()) {
            case 0: {
                return new Insets(2, 2, 0, 0);
            }
            case 1: {
                if (args.get(0).isNumber()) {
                    int i = args.get(0).getInt();
                    return new Insets(i, i, 0, i);
                }
                break;
            }
            case 2: {
                if (args.get(0).isNumber() && args.get(1).isNumber()) {
                    int i = args.get(0).getInt();
                    int j = args.get(1).getInt();
                    return new Insets(i, j, i, j);
                }
                break;
            }
            case 3: {
                if (args.get(0).isNumber() && args.get(1).isNumber()) {
                    int i = args.get(0).getInt();
                    int j = args.get(1).getInt();
                    int k = args.get(2).getInt();
                    return new Insets(i, j, k, k);
                }
                break;
            }
            case 4: {
                if (args.get(0).isNumber() && args.get(1).isNumber() && args.get(2).isNumber() && args.get(3).isNumber()) {
                    int i = args.get(0).getInt();
                    int j = args.get(1).getInt();
                    int k = args.get(2).getInt();
                    int l = args.get(3).getInt();
                    return new Insets(i, j, k, l);
                }
                break;
            }
        }
        throw new IllegalArgumentException("insets(top[,left,bottom,right]) : invalid arguments");
    }

    private void runFunctionInsets(ConstrBuilder b, Args args) {
        try {
            b.constraints.insets = createInsets(args);
        } catch (Exception ex) {
            throw new IllegalArgumentException("insets(top[,left,bottom,right]) : invalid arguments");
        }
    }

    private void runFunctionPads(ConstrBuilder b, Args args) {
        try {
            Pads pads = createPads(args);
            b.constraints.ipadx = pads.x;
            b.constraints.ipady = pads.y;
        } catch (Exception ex) {
            throw new IllegalArgumentException("insets(top[,left,bottom,right]) : invalid arguments");
        }
    }

    private void runGlobalFunctionInsets(Args args) {
        java.util.List<String> names = new ArrayList<>();
        while (args.size() > 0 && args.get(0).isString()) {
            names.add(args.get(0).getString());
            args = args.removeFirst();
        }
        Insets i = createInsets(args);
        filterRequired(names.toArray(new String[0]))
                .forEach(x -> {
                    x.constraints.insets = i;
                });
    }

    private void runGlobalFunctionPads(Args args) {
        java.util.List<String> names = new ArrayList<>();
        while (args.size() > 0 && args.get(0).isString()) {
            names.add(args.get(0).getString());
            args = args.removeFirst();
        }
        Pads i = createPads(args);
        filterRequired(names.toArray(new String[0]))
                .forEach(x -> {
                    x.constraints.ipadx = i.x;
                    x.constraints.ipady = i.y;
                });
    }

    private void readComments(SReader r) {
        if (r.peek() == '#') {
            r.next();
            while (r.hasNext()) {
                if (r.peek() != '\n') {
                    r.next();
                }
            }
        } else if (r.peek("/*")) {
            r.next();
            r.next();
            while (r.hasNext()) {
                if (r.peek() == '*' && r.peek("*/")) {
                    r.next();
                    r.next();
                    break;
                }
            }
        } else {
            throw new IllegalArgumentException("unexpected '/'");
        }
    }

    private void readFooterCommands(SReader r) {
        boolean wasExpr = false;
        while (r.hasNext()) {
            switch (r.peek()) {
                case ' ':
                case '\t': {
                    r.next();
                    skipSpaces(r);
                    break;
                }
                case '\n': {
                    r.next();
                    skipSpaces(r);
                    wasExpr = false;
                    break;
                }
                case ';': {
                    r.next();
                    skipSpaces(r);
                    wasExpr = false;
                    break;
                }
                case '/':
                case '#': {
                    readComments(r);
                    break;
                }
                default: {
                    if (Character.isLetter(r.peek())) {
                        if (wasExpr) {
                            throw new IllegalArgumentException("expected ';' or \\n");
                        }
                        String n = readName(r);
                        if (r.peek("(")) {
                            wasExpr = true;
                            runGlobalFunction(n, readParList(r));
                        } else {
                            throw new IllegalArgumentException("expected '('");
                        }
                    } else {
                        throw new IllegalArgumentException("expected function call");
                    }
                }
            }
        }
    }

    private static class GridBagConstraintsHolderFilter implements Predicate<GridBagConstraintsHolder> {

        private Set<Pattern> all = new LinkedHashSet<Pattern>();

        private GridBagConstraintsHolderFilter(String... commaSeparatedNames) {
            for (String commaSeparatedName : commaSeparatedNames) {
                if (commaSeparatedName != null && commaSeparatedName.length() > 0) {
                    for (String s : commaSeparatedName.split("[; \t]")) {
                        if (s.length() > 0) {
                            all.add(Pattern.compile(s));
                        }
                    }
                }
            }
        }

        @Override
        public boolean test(GridBagConstraintsHolder a) {
            if (a.id == null) {
                return false;
            }
            if (all.isEmpty()) {
                return true;
            }
            for (Pattern p : all) {
                if (p.matcher(a.id).matches()) {
                    return true;
                }
            }
            return false;

        }

    }

    private static class GridBagConstraintsHolder {

        String id;
        Component component;
        GridBagConstraints constraints;
    }

    public static GridBagLayoutSupport load(URL url) {
        if (url == null) {
            throw new NullPointerException();
        }
        return new GridBagLayoutSupport(_IOUtils.loadStreamAsString(url));
    }

    public GridBagLayoutSupport(String pattern) {
        setPattern(pattern);
    }

    protected GridBagConstraintsHolder getConstraintsHolder(String name) {
        GridBagConstraintsHolder constraints = constraintsMap.get(name);
        if (constraints == null) {
            throw new NoSuchElementException(name);
        }
        return constraints;
    }

    public GridBagConstraints getConstraints(String name) {
        GridBagConstraintsHolder constraints = constraintsMap.get(name);
        if (constraints == null) {
            throw new NoSuchElementException(name);
        }
        return constraints.constraints;
    }

    public String getPattern() {
        return pattern;
    }

    private static class ClassConf {

        GridBagConstraints constraints = new GridBagConstraints();
        String name;

    }

    private static class SReader {

        private String pattern;
        int x = 0;
        int y = 0;
        private int i = 0;

        public SReader(String pattern) {
            this.pattern = pattern;
        }

        public boolean isPeekDigit() {
            return hasNext() && Character.isDigit(peek());
        }

        public boolean isPeekLetterOrDigit() {
            return hasNext() && Character.isLetterOrDigit(peek());
        }

        public boolean isPeekLetter() {
            return hasNext() && Character.isLetter(peek());
        }

        public char next() {
            char c = pattern.charAt(i);
            i++;
            return c;
        }

        public char peek() {
            return pattern.charAt(i);
        }

        public boolean hasNext() {
            return i < pattern.length();
        }

        private boolean peek(String string) {
            int len = string.length();
            if (i + len >= pattern.length()) {
                return false;
            }
            for (int j = 0; j < len; j++) {
                if (pattern.charAt(i + j) != string.charAt(j)) {
                    return false;
                }
            }
            return true;
        }
    }

    public Args readParList(SReader r) {
        if (r.peek() != '(') {
            throw new IllegalArgumentException("expected '('");
        }
        skipSpaces(r);
        r.next();
        if (r.peek() == ')') {
            return new Args();
        }
        Args res = new Args();
        Arg a = readArg(r);
        if (a == null) {
            throw new IllegalArgumentException("expected argument");
        }
        res.add(a);
        while (true) {
            skipSpaces(r);
            if (r.hasNext()) {
                switch (r.peek()) {
                    case ')': {
                        r.next();
                        return res;
                    }
                    case ',': {
                        skipSpaces(r);
                        r.next();
                        a = readArg(r);
                        if (a == null) {
                            throw new IllegalArgumentException("expected argument");
                        }
                        res.add(a);
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("expected ','");
                    }
                }
            } else {
                throw new IllegalArgumentException("expected ')'");
            }
        }
    }

    private Arg readArg(SReader r) {
        if (r.isPeekDigit()) {
            return Arg.forNumber(readNumber(r));
        } else if (r.isPeekLetter()) {
            return Arg.forName(readName(r));
        } else if (r.peek() == '\"') {
            return Arg.forDblQuoted(readDblQuotes(r));
        } else if (r.peek() == '\'') {
            return Arg.forSmpQuoted(readName(r));
        } else {
            return null;
        }
    }

    private void skipSpaces(SReader r) {
        while (r.hasNext()) {
            char c = r.peek();
            if (c == ' ' || c == '\t') {

            } else {
                break;
            }
        }
    }

    private String readName(SReader r) {
        StringBuilder sb = new StringBuilder();
        if (r.isPeekLetterOrDigit()) {
            sb.append(r.next());
            while (r.hasNext()) {
                char c = r.peek();
                if (Character.isLetterOrDigit(c)) {
                    sb.append(r.next());
                } else {
                    break;
                }
            }
            return sb.toString();
        }
        throw new IllegalArgumentException("expected letter");
    }

    private String readDblQuotes(SReader r) {
        StringBuilder sb = new StringBuilder();
        if (r.peek() == '"') {
            r.next();
            while (r.hasNext()) {
                char c = r.peek();
                if (c == '\\') {
                    r.next();
                    switch (r.peek()) {
                        case 'n': {
                            r.next();
                            sb.append("\n");
                            break;
                        }
                        case 't': {
                            r.next();
                            sb.append("\t");
                            break;
                        }
                        default: {
                            sb.append(r.next());
                        }
                    }
                } else if (c == '\"') {
                    break;
                }
            }
            return sb.toString();
        }
        throw new IllegalArgumentException("expected letter");
    }

    private String readSimpleQuotes(SReader r) {
        StringBuilder sb = new StringBuilder();
        if (r.peek() == '\'') {
            r.next();
            while (r.hasNext()) {
                char c = r.peek();
                if (c == '\\') {
                    r.next();
                    switch (r.peek()) {
                        case 'n': {
                            r.next();
                            sb.append("\n");
                            break;
                        }
                        case 't': {
                            r.next();
                            sb.append("\t");
                            break;
                        }
                        default: {
                            sb.append(r.next());
                        }
                    }
                } else if (c == '\'') {
                    break;
                }
            }
            return sb.toString();
        }
        throw new IllegalArgumentException("expected letter");
    }

    private int readNumber(SReader r) {
        StringBuilder sb = new StringBuilder();
        while (r.hasNext()) {
            char c = r.peek();
            if (Character.isDigit(c)) {
                sb.append(r.next());
            } else {
                break;
            }
        }
        return Integer.parseInt(sb.toString());
    }

    private static class Arg {

        private static final Arg ARG_INDEFINED = new Arg(null, null);

        String type;
        Object value;

        public Arg(String type, Object value) {
            this.type = type;
            this.value = value;
        }

        public static Arg forName(String s) {
            return (new Arg("name", s));
        }

        public static Arg forDblQuoted(String s) {
            return (new Arg("\"", s));
        }

        public static Arg forSmpQuoted(String s) {
            return (new Arg("\'", s));
        }

        public static Arg forNumber(Number s) {
            return (new Arg("number", s));
        }

        public boolean isUndefined() {
            return type == null;
        }

        public int getInt() {
            return getNumber().intValue();
        }

        public String getName() {
            if (isName()) {
                return getString();
            } else {
                throw new IllegalArgumentException("not a name");
            }
        }

        public boolean isString() {
            if (type != null) {
                switch (type) {
                    case "name":
                    case "\"":
                    case "'": {
                        return true;
                    }
                }
            }
            return false;
        }

        public String getString() {
            if (isString()) {
                return (String) value;
            }
            throw new IllegalArgumentException("not a string");

        }

        public Number getNumber() {
            if (isNumber()) {
                return ((Number) value);
            } else {
                throw new IllegalArgumentException("not a number");
            }
        }

        public boolean isNumber() {
            return "number".equals(type);
        }

        public boolean isName() {
            return "name".equals(type);
        }

        public boolean isDoubleQuoted() {
            return "\"".equals(type);
        }

        public boolean isSimpleQuoted() {
            return "\'".equals(type);
        }

        public boolean isQuoted() {
            return isDoubleQuoted() || isSimpleQuoted();
        }

    }

    private static class Args {

        java.util.List<Arg> list = new ArrayList<>();

        public void add(Arg s) {
            list.add(s);
        }

        public Arg get(int i) {
            return i < size() ? list.get(i) : Arg.ARG_INDEFINED;
        }

        public int size() {
            return list.size();
        }

        public Args removeFirst() {
            Args a = new Args();
            a.list.addAll(list.subList(1, list.size()));
            return a;
        }
    }

    private class ConstrBuilder {

        Component component;
        GridBagConstraints constraints = new GridBagConstraints();
        String name = null;
        boolean lastConfig = true;
        boolean config = false;
        boolean finished = false;
    }

    private void readItem(SReader r) {
        if (r.peek() != '[') {
            throw new IllegalArgumentException("Expected '['");
        }
        ConstrBuilder b = new ConstrBuilder();
        b.constraints.gridx = r.x;
        b.constraints.gridy = r.y;
        r.x++;

        r.next();//skip '['
        while (!b.finished && r.hasNext()) {
            char c = r.peek();
            switch (c) {
                case ']': {
                    b.finished = true;
                    r.next();
                    break;
                }
                case '-': {
                    b.config = true;
                    b.lastConfig = true;
                    b.constraints.fill = GridBagConstraints.HORIZONTAL;
                    r.next();
                    break;
                }
                case '|': {
                    r.next();
                    b.config = true;
                    b.lastConfig = true;
                    b.constraints.fill = GridBagConstraints.VERTICAL;
                    break;
                }
                case '+': {
                    b.config = true;
                    b.lastConfig = true;
                    b.constraints.fill = GridBagConstraints.BOTH;
                    r.next();
                    break;
                }
                case ':': {
                    b.config = true;
                    b.lastConfig = true;
                    b.constraints.gridwidth++;
                    r.x++;
                    r.next();
                    break;
                }
                case '.': {
                    b.config = true;
                    b.lastConfig = true;
                    b.constraints.gridwidth = GridBagConstraints.REMAINDER;
                    r.next();
                    break;
                }
                case '<': {
                    b.config = true;
                    b.lastConfig = true;
                    if (b.constraints.anchor == GridBagConstraints.PAGE_START) {
                        b.constraints.anchor = GridBagConstraints.FIRST_LINE_START;
                    } else if (b.constraints.anchor == GridBagConstraints.PAGE_END) {
                        b.constraints.anchor = GridBagConstraints.LAST_LINE_START;
                    } else {
                        b.constraints.anchor = GridBagConstraints.LINE_START;
                    }
                    r.next();
                    break;
                }
                case '>': {
                    b.config = true;
                    b.lastConfig = true;
                    if (b.constraints.anchor == GridBagConstraints.PAGE_START) {
                        b.constraints.anchor = GridBagConstraints.FIRST_LINE_END;
                    } else if (b.constraints.anchor == GridBagConstraints.PAGE_END) {
                        b.constraints.anchor = GridBagConstraints.LAST_LINE_END;
                    } else {
                        b.constraints.anchor = GridBagConstraints.LINE_END;
                    }
                    r.next();
                    break;
                }
                case '_': {
                    b.config = true;
                    b.lastConfig = true;
                    if (b.constraints.anchor == GridBagConstraints.LINE_START) {
                        b.constraints.anchor = GridBagConstraints.LAST_LINE_START;
                    } else if (b.constraints.anchor == GridBagConstraints.LINE_END) {
                        b.constraints.anchor = GridBagConstraints.LAST_LINE_END;
                    } else {
                        b.constraints.anchor = GridBagConstraints.PAGE_END;
                    }
                    r.next();
                    break;
                }
                case '^': {
                    b.config = true;
                    b.lastConfig = true;
                    if (b.constraints.anchor == GridBagConstraints.LINE_START) {
                        b.constraints.anchor = GridBagConstraints.FIRST_LINE_START;
                    } else if (b.constraints.anchor == GridBagConstraints.LINE_END) {
                        b.constraints.anchor = GridBagConstraints.FIRST_LINE_END;
                    } else {
                        b.constraints.anchor = GridBagConstraints.PAGE_START;
                    }
                    r.next();
                    break;
                }
                case '=': {
                    b.config = true;
                    b.lastConfig = true;
                    b.constraints.weightx++;
                    r.next();
                    break;
                }
                case '$': {
                    b.config = true;
                    b.lastConfig = true;
                    b.constraints.weighty++;
                    r.next();
                    break;
                }
                case ' ':
                case '\t': {
                    //do nothing just allowed for formatting
                    b.lastConfig = true;
                    r.next();
                    break;
                }
                default: {
                    if (Character.isLetter(c) || Character.isDigit(c)) {
                        String n2 = readName(r);
                        if (r.hasNext() && r.peek() == '(') {
                            runFunction(n2, b, readParList(r));
                        } else {
                            if (b.name == null) {
                                b.name = n2;
                            } else {
                                throw new IllegalArgumentException("name is already specified : got '" + c + "' , name is '" + b.name.toString() + "'");
                            }
                        }
                    } else {
                        throw new IllegalArgumentException("Unexpected token " + c);
                    }
                    b.lastConfig = false;
                    break;
                }

            }
        }
        if (!b.finished) {
            throw new IllegalArgumentException("expected ']'");
        }
        if (b.name == null) {
            if (b.component == null) {
                throw new IllegalArgumentException("expected component name");
            } else {
                b.name = UUID.randomUUID().toString();
            }
        }
        String key = b.name;
        GridBagConstraintsHolder h = constraintsMap.get(key);
        if (h != null) {
            GridBagConstraints oldConstraints = h.constraints;
            if (b.config) {
                throw new IllegalArgumentException("you cannot override constraints for " + b.name);
            } else {
                oldConstraints.gridwidth = b.constraints.gridx - oldConstraints.gridx + 1;
                oldConstraints.gridheight = b.constraints.gridy - oldConstraints.gridy + 1;
            }
        } else {
            h = new GridBagConstraintsHolder();
            h.id = key;
            h.constraints = b.constraints;
            h.component = b.component;
            constraintsMap.put(key, h);
        }

    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
        constraintsMap.clear();
        SReader r = new SReader(pattern);
        while (r.hasNext()) {
            char c = r.peek();
            switch (c) {
                case '[': {
                    readItem(r);
                    break;
                }
                case '\n': {
                    r.next();
                    r.y++;
                    r.x = 0;
                    break;
                }
                case ' ':
                case '\t': {
                    r.next();
                    break;
                }
                case ':': {
                    r.next();
                    break;
                }
                case '#':
                case '/': {
                    readComments(r);
                    break;
                }
                case '.':
                case ';': {
                    r.next();
                    readFooterCommands(r);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Expected [");
            }
        }
    }

    public java.util.List<GridBagConstraintsHolder> filterRequired(String... commaSeparatedNames) {
        java.util.List<GridBagConstraintsHolder> a = filter(commaSeparatedNames);
        if (a.isEmpty()) {
            throw new IllegalArgumentException("not match: " + Arrays.asList(commaSeparatedNames));
        }
        return a;
    }

    public java.util.List<GridBagConstraintsHolder> filter(String... commaSeparatedNames) {
        return constraintsMap.values().stream().filter(new GridBagConstraintsHolderFilter(commaSeparatedNames))
                .collect(Collectors.toList());
    }

    public GridBagLayoutSupport setInsets(String commaSeparatedNames, Insets insets) {
        filterRequired(commaSeparatedNames)
                .forEach(x -> x.constraints.insets = insets);
        return this;
    }

    public GridBagLayoutSupport setIpad(String commaSeparatedNames, Pads pads) {
        filterRequired(commaSeparatedNames)
                .forEach(x -> {
                    x.constraints.ipadx = pads.x;
                    x.constraints.ipady = pads.y;
                });
        return this;
    }

    public GridBagLayoutSupport setIpadx(String commaSeparatedNames, int ipadx) {
        filterRequired(commaSeparatedNames)
                .forEach(x -> x.constraints.ipadx = ipadx);
        return this;
    }

    public GridBagLayoutSupport setIpady(String commaSeparatedNames, int ipady) {
        filterRequired(commaSeparatedNames)
                .forEach(x -> x.constraints.ipady = ipady);
        return this;
    }

    public GridBagLayoutSupport bind(String name, JComponent component) {
        getConstraintsHolder(name).component = component;
        return this;
    }

    public <T extends Container> T apply(T cont) {
        for (GridBagConstraintsHolder value : constraintsMap.values()) {
            if (value.component == null) {
                throw new IllegalArgumentException("missing binding for " + value.id);
            }
        }
        cont.setLayout(new GridBagLayout());
        for (GridBagConstraintsHolder value : constraintsMap.values()) {
            cont.add(value.component, getConstraints(value.id));
        }
        return cont;
    }

    private static class Pads {

        int x;
        int y;

        public Pads(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }
}
