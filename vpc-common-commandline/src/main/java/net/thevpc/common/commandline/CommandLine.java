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
package net.thevpc.common.commandline;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * <pre>
 *         CommandLine args=new CommandLine(Arrays.asList("--!deleteLog","--deploy","/deploy/path","--deploy=/other-deploy/path","some-param"));
 *         Argument a;
 *         while (args.hasNext()) {
 *             if ((a = args.readBooleanOption("--deleteLog")) != null) {
 *                 deleteLog = a.getBooleanValue();
 *             } else if ((a = args.readStringOption("--deploy")) != null) {
 *                 apps.add(a.getStringValue());
 *             } else if ((a = args.readNonOption()) != null) {
 *                 name = a.getString();
 *             } else {
 *                 args.unexpectedArgument();
 *             }
 *         }
 * </pre>
 * Created by vpc on 12/7/16.
 */
public class CommandLine {

    private List<String> args;
    private int wordIndex = 0;
    private CommandAutoComplete autoComplete;
    private HashSet<String> visitedSequences = new HashSet<>();
    private String eq = "=";

    public CommandLine(CommandLineContext context) {
        setArgs(context.getArgs());
        setAutoComplete(context.getAutoComplete());
    }

    public CommandLine(String[] args, CommandAutoComplete autoComplete) {
        setArgs(args);
        setAutoComplete(autoComplete);
    }

    public CommandLine(String[] args) {
        setArgs(args);
    }

    public CommandLine(List<String> args, CommandAutoComplete autoComplete) {
        setArgs(args);
        setAutoComplete(autoComplete);
    }

    public CommandLine(List<String> args) {
        setArgs(args);
    }

    public CommandLine copy() {
        CommandLine c = new CommandLine(args.toArray(new String[0]), autoComplete);
        c.eq = this.eq;
        c.visitedSequences = new HashSet<>(this.visitedSequences);
        return c;
    }

    public void setArgs(List<String> args) {
        setArgs(args.toArray(new String[0]));
    }

    public void setArgs(String[] args) {
        this.args = new ArrayList<>();
        for (String arg : args) {
            if (arg.startsWith("--")) {
                this.args.add(arg);
            } else if (arg.startsWith("-!")) {
                char[] chars = arg.toCharArray();
                for (int i = 2; i < chars.length; i++) {
                    this.args.add("-!" + chars[i]);
                }
            } else if (arg.startsWith("-")) {
                char[] chars = arg.toCharArray();
                for (int i = 1; i < chars.length; i++) {
                    this.args.add("-" + chars[i]);
                }
            } else {
                this.args.add(arg);
            }
        }
    }

    public void setAutoComplete(CommandAutoComplete autoComplete) {
        this.autoComplete = autoComplete;
    }

    public boolean isExecMode() {
        return autoComplete == null;
    }

    public boolean isAutoCompleteMode() {
        return autoComplete != null;
    }

    public int skip() {
        return skip(1);
    }

    public int skip(int count) {
        if (count < 0) {
            count = 0;
        }
        if (count > args.size()) {
            count = args.size();
        }
        int x = 0;
        for (int i = 0; i < count; i++) {
            args.remove(0);
        }
        wordIndex += count;
        return count;
    }

    public boolean isOption(int index) {
        return (index < args.size() && args.get(index).startsWith("-"));
    }

    public boolean isNonOption(int index) {
        return (index < args.size() && !args.get(index).startsWith("-"));
    }

    public boolean isOption() {
        return isOption(0);
    }

    public boolean isNonOption() {
        return isNonOption(0);
    }

    public boolean isOption(String... options) {
        return isOption(0, options);
    }

    public boolean isOption(int index, String... options) {
        for (String s : options) {
            if (s != null) {
                checkOptionString(s);
            }
        }
        if (index >= 0 && index < args.size()) {
            Argument v = get(index);
            if (v.isOption()) {
                if (v.isKeyVal()) {
                    for (String s : options) {
                        if (s != null) {
                            if (v.getKey().equals(s)) {
                                return true;
                            }
                        }
                    }
                } else {
                    for (String s : options) {
                        if (s != null) {
                            if (v.getExpression().equals(s)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public Argument readBooleanOption(String... names) {
        return readOption(OptionType.BOOLEAN, names);
    }

    public Argument readStringOption(String... names) {
        return readOption(OptionType.STRING, names);
    }

    public Argument readImmediateStringOption(String... names) {
        return readOption(OptionType.IMMEDIATE_STRING, names);
    }

    public Argument readOption(String... names) {
        return readVoidOption(names);
    }

    public Argument readVoidOption(String... names) {
        return readOption(OptionType.VOID, names);
    }

    public Argument readOption(OptionType expectValue, String... names) {
        for (String name : names) {
            checkOptionString(name);
            if (isAutoCompleteMode() && getWordIndex() == autoComplete.getCurrentWordIndex()) {
                autoComplete.addCandidate(new DefaultArgumentCandidate(name));
            }
            if (isOption()) {
                Argument p = get(0);
                switch (expectValue) {
                    case VOID: {
                        if (p.getExpression().equals(name)) {
                            skip();
                            return p;
                        }
                        break;
                    }
                    case STRING: {
                        if (p.getName().equals(name)) {
                            if (p.isKeyVal()) {
                                skip();
                                return p;
                            } else {
                                if (isAutoCompleteMode() && getWordIndex() + 1 == autoComplete.getCurrentWordIndex()) {
                                    autoComplete.addCandidate(new DefaultArgumentCandidate("<StringValueFor" + name + ">"));
                                }
                                Argument r2 = get(1);
                                if (r2 != null && !r2.isOption()) {
                                    skip(2);
                                    return new Argument(p.getExpression() + eq + r2.getExpression());
                                }
                            }
                        }
                        break;
                    }
                    case IMMEDIATE_STRING: {
                        if (p.getName().equals(name)) {
                            if (p.isKeyVal()) {
                                skip();
                                return p;
                            }
                        }
                        break;
                    }
                    case BOOLEAN: {
                        if (p.getName().equals(name)) {
                            if (p.isNegated()) {
                                if (p.isKeyVal()) {
                                    //should not happen
                                    boolean x = p.getBoolean();
                                    skip();
                                    return new Argument(p.getName() + eq + (!x));
                                } else {
                                    skip();
                                    return new Argument(p.getName() + eq + (false));
                                }
                            } else if (p.isKeyVal()) {
                                skip();
                                return p;
                            } else {
                                skip();
                                return new Argument(p.getName() + eq + (true));
                            }
                        }
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Unsupported " + expectValue);
                    }
                }
            }
        }
        return null;
    }

    public CommandLine requiredNonOption() {
        if (!isNonOption()) {
            throw new IllegalArgumentException("Expected value");
        }
        return this;
    }

    public void skipAll() {
        skip(args.size());
    }

    public enum OptionType {
        VOID,
        STRING,
        IMMEDIATE_STRING,
        BOOLEAN,
    }

    public Argument readNonOption(boolean expectValue, String name) {
        if (isAutoCompleteMode() && getWordIndex() == autoComplete.getCurrentWordIndex()) {
            autoComplete.addCandidate(new DefaultArgumentCandidate(name));
        }
        checkNonOptionString(name);
        if (isNonOption()) {
            Argument p = get(0);
            if (expectValue) {
                if (p.isKeyVal()) {
                    skip();
                    return p;
                } else {
                    Argument r2 = get(1);
                    if (r2 != null && !r2.isOption()) {
                        skip(2);
                        return new Argument(p.getExpression() + "=" + r2.getExpression());
                    }
                }
            } else {
                skip();
                return p;
            }
        }
        return null;
    }

    public Argument readRequiredOption(String name) {
        Argument o = readVoidOption(name);
        if (o == null) {
            throw new IllegalArgumentException("mMissing argument " + name);
        }
        return o;
    }

    public Argument readRequiredNonOption() {
        return readRequiredNonOption(DefaultNonOption.VALUE);
    }

    public Argument readRequiredNonOption(NonOption name) {
        return readNonOption(name, true);
    }

    public Argument readNonOption() {
        return readNonOption(DefaultNonOption.VALUE);
    }

    public Argument readNonOption(String... names) {
        if (names.length == 0) {
            throw new IllegalArgumentException("missing non option Name");
        }
        Argument a = get(0);
        if (a != null) {
            for (String name : names) {
                if (!a.isOption() && a.getExpression().equals(name)) {
                    skip();
                    return a;
                }
            }
        }
        if (isAutoComplete()) {
            for (String name : names) {
                autoComplete.addCandidate(new DefaultArgumentCandidate(name));
            }
        }
        return null;
    }

    public Argument readRequiredNonOption(String name) {
        Argument a = readNonOption(name);
        if (a != null) {
            return a;
        }
        throw new IllegalArgumentException("Expected " + name);
    }

    public Argument readNonOption(NonOption name) {
        return readNonOption(name, false);
    }

    public Argument readNonOption(NonOption name, boolean error) {
        if (args.size() > 0 && !isOption()) {
            if (isAutoComplete()) {
                List<ArgumentCandidate> values = name.getValues();
                if (values == null || values.isEmpty()) {
                    autoComplete.addExpectedTypedValue(null, name.getName());
                } else {
                    for (ArgumentCandidate value : name.getValues()) {
                        autoComplete.addCandidate(value);
                    }
                }
            }
            String r = args.get(0);
            skip();
            return new Argument(r);
        } else {
            if (autoComplete != null) {
                if (isAutoComplete()) {
                    List<ArgumentCandidate> values = name.getValues();
                    if (values == null || values.isEmpty()) {
                        autoComplete.addExpectedTypedValue(null, name.getName());
                    } else {
                        for (ArgumentCandidate value : name.getValues()) {
                            autoComplete.addCandidate(value);
                        }
                    }
                }
                return new Argument("");
            }
            if (!error) {
                return null;//return new Argument("");
            }
            if (args.size() > 0 && isOption()) {
                throw new IllegalArgumentException("Unexpected option " + get(0));
            }
            throw new IllegalArgumentException("missing argument " + name);
        }
    }

    public Argument read() {
        Argument val = get(0);
        skip();
        return val;
    }

    private boolean isAutoCompleteContext() {
        return autoComplete != null;
    }

    private boolean isAutoComplete() {
        if (autoComplete != null && getWordIndex() == autoComplete.getCurrentWordIndex()) {
            return true;
        }
        return false;
    }

    public boolean readAll(boolean acceptDuplicates, String... vals) {
        String[][] vals2 = new String[vals.length][];
        for (int i = 0; i < vals2.length; i++) {
            vals2[i] = _Utils.split(vals[i], " ");
        }
        return readAll(acceptDuplicates, vals2);
    }

    public boolean readAll(String... vals) {
        return readAll(true, vals);
    }

    public boolean readAllOnce(String... vals) {
        return readAll(false, vals);
    }

    private boolean readAll(boolean acceptDuplicates, String[]... vals) {
        if (autoComplete != null) {
            for (String[] val : vals) {
                if ((acceptDuplicates || !isVisitedSequence(val))) {
                    if (acceptSequence(0, val)) {
                        setVisitedSequence(val);
                        skip(val.length);
                        return true;
                    } else {
                        setVisitedSequence(val);
                        for (int i = 0; i < val.length; i++) {
                            String v = val[i];
                            if (getWordIndex() + i == autoComplete.getCurrentWordIndex()) {
                                autoComplete.addCandidate(new DefaultArgumentCandidate(v));
                            } else if (get(i)==null || !get(i).getExpression("").equals(v)) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        for (String[] val : vals) {
            if ((acceptDuplicates || !isVisitedSequence(val)) && acceptSequence(0, val)) {
                setVisitedSequence(val);
                skip(val.length);
                return true;
            }
        }
        return false;
    }

    public boolean acceptSequence(int pos, String... vals) {
        for (int i = 0; i < vals.length; i++) {
            Argument argument = get(pos + i);
            if (argument == null) {
                return false;
            }
            if (!argument.getExpression("").equals(vals[i])) {
                return false;
            }
        }
        return true;
    }

    public Argument findOption(String option) {
        int index = indexOfOption(option);
        if (index >= 0) {
            return get(index + 1);
        }
        return null;
    }

    public Argument get() {
        return get(0);
    }

    public Argument get(int i) {
        if (i >= 0 && i < args.size()) {
            return new Argument(args.get(i));
        }
        return null;
    }

    public boolean containOption(String name) {
        return indexOfOption(name) >= 0;
    }

    public int indexOfOption(String name) {
        if (name.startsWith("-") || name.startsWith("--")) {
            for (int i = 0; i < args.size(); i++) {
                if (args.get(i).equals(name)) {
                    return i;
                }
            }
        } else {
            throw new IllegalArgumentException("Not an option " + name);
        }
        return -1;
    }

    public int length() {
        return args.size();
    }

    public void requireEmpty() {
        if (!isEmpty()) {
            if (autoComplete != null) {
                args.clear();
                return;
            }
            throw new IllegalArgumentException("Too Many arguments");
        }
    }

    public void unexpectedArgument() {
        if (!isEmpty()) {
            if (autoComplete != null) {
                args.clear();
                return;
            }
            throw new IllegalArgumentException("Unexpected Argument " + get());
        }
    }

    public void unexpectedArgument(String commandName) {
        if (!isEmpty()) {
            if (autoComplete != null) {
                args.clear();
                return;
            }
            if (commandName == null) {
                throw new IllegalArgumentException("Unexpected Argument " + get());
            } else {
                throw new IllegalArgumentException(commandName + ": Unexpected Argument " + get());
            }
        }
    }

    public void requireNonEmpty() {
        if (isEmpty()) {
            if (autoComplete != null) {
                args.clear();
                return;
            }
            throw new IllegalArgumentException("missing arguments");
        }
    }

    public boolean hasNext() {
        return !args.isEmpty();
    }

    public boolean isEmpty() {
        return args.isEmpty();
    }

    public String[] toArray() {
        return args.toArray(new String[args.size()]);
    }

    @Override
    public String toString() {
        return "CommmandLine{"
                + (args)
                + '}';
    }

    public int getWordIndex() {
        return wordIndex;
    }

    private boolean isVisitedSequence(String[] aaa) {
        return visitedSequences.contains(flattenSequence(aaa));
    }

    private boolean setVisitedSequence(String[] aaa) {
        return visitedSequences.add(flattenSequence(aaa));
    }

    private String flattenSequence(String[] aaa) {
        StringBuilder sb = new StringBuilder();
        sb.append(aaa[0]);
        for (int i = 1; i < aaa.length; i++) {
            sb.append("\n").append(aaa[i]);
        }
        return sb.toString();
    }

    public CommandAutoComplete getAutoComplete() {
        return autoComplete;
    }

    private static void checkOptionString(String s) {
        if (!isOptionString(s)) {
            throw new IllegalArgumentException("Option must start with - but got " + s);
        }
    }

    private static void checkNonOptionString(String s) {
        if (isOptionString(s)) {
            throw new IllegalArgumentException("Option unexpected " + s);
        }
    }

    private static boolean isOptionString(String s) {
        return (s != null && s.startsWith("-"));
    }

    public void reset() {
        visitedSequences.clear();
    }

    public ArgumentReader reader() {
        return new ArgumentReader();
    }

    public class ArgumentReader {
        private boolean repeatable = true;

        public boolean isRepeatable() {
            return repeatable;
        }

        public ArgumentReader setRepeatable(boolean repeatable) {
            this.repeatable = repeatable;
            return this;
        }

        public boolean readAll(String... vals) {
            return CommandLine.this.readAll(repeatable, vals);
        }

        private boolean readAll(String[]... vals) {
            return CommandLine.this.readAll(repeatable, vals);
        }
    }
}
