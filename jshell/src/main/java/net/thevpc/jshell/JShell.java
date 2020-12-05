/**
 * ====================================================================
 * Doovos (Distributed Object Oriented Operating System)
 * <p>
 * Doovos is a new Open Source Distributed Object Oriented Operating System
 * Design and implementation based on the Java Platform. Actually, it is a try
 * for designing a distributed operation system in top of existing
 * centralized/network OS. Designed OS will follow the object oriented
 * architecture for redefining all OS resources (memory,process,file
 * system,device,...etc.) in a highly distributed context. Doovos is also a
 * distributed Java virtual machine that implements JVM specification on top the
 * distributed resources context.
 * <p>
 * Doovos BIN is a standard implementation for Doovos boot sequence, shell and
 * common application tools. These applications are running onDoovos guest JVM
 * (distributed jvm).
 * <br>
 * <p>
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
package net.thevpc.jshell;

import net.thevpc.jshell.parser.nodes.*;
import net.thevpc.jshell.parser2.JShellParser2;
import net.thevpc.jshell.parser2.Yaccer;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JShell {

    public static final String APP_TITLE = "JavaShell";
    public static final String APP_VERSION = "0.4";
    public static final int NEXT_STATEMENT = -2;
    public static final String NO_WAIT = "@nowait";
    public static final String NEW_TERM = "@newterm";
    public static final String STDOUT = "@out";
    public static final String STDIN = "@in";
    public static final String STDERR = "@err";
    public static final String ENV_PATH = "PATH";
    public static final String ENV_EXEC_PACKAGES = "EXEC_PKG";
    public static final String ENV_EXEC_EXTENSIONS = "EXEC_EXT";

    static {
//        if (DSystem.isDoovosEnabled()) {
//            DProcess pp = DProcess.getProcess();
//            String title = pp.getMetadata("title");
//            if (title == null) {
//                pp.setMetadata("title", APP_TITLE);
//                pp.setMetadata("version", APP_VERSION);
//                pp.setMetadata("author", "Taha BEN SALAH");
//                pp.setMetadata("date", "2010-12-08");
//                pp.setMetadata("history.1", "2009-04-12 : created");
//                pp.setMetadata("history.2", "2010-12-08 : update");
//            }
//        }
    }

    public String[] args;
    public boolean exitAfterProcessingLines = false;
    public String input = null;
    public boolean fallBackToMain = false;

    public Object shellInterpreter = null;
    private String version = "1.0.0";
    private String startupScript;
    private String shutdownScript;
    private JShellOptions options = new JShellOptions();
    private JShellWordEvaluator wordEvaluator;
    private JShellHistory history = new DefaultShellHistory();
    private JShellErrorHandler errorHandler;
    private JShellExternalExecutor externalExecutor;
    private JShellCommandTypeResolver commandTypeResolver;
    private JShellNodeEvaluator nodeEvaluator;
    private JShellVariables vars = new JShellVariables();
    private JShellContext context;
    private BufferedReader _in_reader = null;

    public JShell() {
        errorHandler = new DefaultJShellErrorHandler();
        commandTypeResolver = new DefaultJShellCommandTypeResolver();
        nodeEvaluator = new DefaultJShellNodeEvaluator();
        wordEvaluator = new DefaultJShellWordEvaluator();
    }

    public static String shellPatternToRegexp(String pattern) {
        String pathSeparator = "/";
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

    public JShellNodeEvaluator getNodeEvaluator() {
        return nodeEvaluator;
    }

    public void setNodeEvaluator(JShellNodeEvaluator nodeEvaluator) {
        this.nodeEvaluator = nodeEvaluator;
    }

    public JShellCommandTypeResolver getCommandTypeResolver() {
        return commandTypeResolver;
    }

    public void setCommandTypeResolver(JShellCommandTypeResolver whichResolver) {
        this.commandTypeResolver = whichResolver;
    }

    public JShellExternalExecutor getExternalExecutor() {
        return externalExecutor;
    }

    public void setExternalExecutor(JShellExternalExecutor externalExecutor) {
        this.externalExecutor = externalExecutor;
    }

    public JShellErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(JShellErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public String getStartupScript() {
        return startupScript;
    }

    public String getShutdownScript() {
        return shutdownScript;
    }

    //    public String[] expandPath2(String[] names, String basePath) {
//        File f = new File(basePath);
//        if (names.length == 0) {
//            return new String[]{basePath};
//        }
//        String[] names2 = new String[names.length - 1];
//        System.arraycopy(names, 1, names2, 0, names2.length);
//        String names0 = names[0];
//        //check if names0 is not generic
//        StringBuilder pattern = new StringBuilder();
//        boolean ispattern = false;
//        char[] charArray = names0.toCharArray();
//        for (int i = 0; i < charArray.length; i++) {
//            char c = charArray[i];
//            switch (c) {
//                case '\\': {
//                    ispattern = true;
//                    pattern.append('\\');
//                    pattern.append(charArray[i + 1]);
//                    i++;
//                    break;
//                }
//                case '.': {
//                    pattern.append('\\');
//                    pattern.append(c);
//                    break;
//                }
//                case '*': {
//                    ispattern = true;
//                    pattern.append(".*");
//                    break;
//                }
//                case '$': {
//                    pattern.append("\\$");
//                    break;
//                }
//                case '^': {
//                    pattern.append("\\^");
//                    break;
//                }
//                default: {
//                    pattern.append(c);
//                    break;
//                }
//            }
//        }
//        List<String> files = findFiles(ispattern ? pattern.toString() : names0, !ispattern, basePath);
//        List<String> all = new ArrayList<String>();
//        for (String newParent : files) {
//            all.addAll(Arrays.asList(expandPath2(names2, newParent)));
//        }
//        return all.toArray(new String[all.size()]);
//    }
    public JShellVariables vars() {
        return vars;
    }

    public List<String> findFiles(final String namePattern, boolean exact, String parent) {
        if (exact) {
            String[] all = new File(parent).list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return namePattern.equals(name);
                }
            });
            if (all == null) {
                all = new String[0];
            }
            return Arrays.asList(all);
        } else {
            final Pattern o = Pattern.compile(namePattern);
            String[] all = new File(parent).list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return o.matcher(name).matches();
                }
            });
            if (all == null) {
                all = new String[0];
            }
            return Arrays.asList(all);
        }
    }

    public void executeFile(String file, String[] args) {
        executeFile(file, createContext().setArgs(args), false);
    }

    public void setServiceName(String serviceName) {
        getRootContext().setServiceName(serviceName);
    }

    //    public String[] expandPath(String name) {
//        return expandPath(name, getCwd());
//    }
//
//    public String[] expandPath(String name, String basePath) {
//        List<String> nameParts = new ArrayList<String>();
//        StringBuilder b = new StringBuilder();
//        boolean isExpandable = false;
//
//        char[] charArray = name.toCharArray();
//        for (int i = 0; i < charArray.length; i++) {
//            char c = charArray[i];
//            switch (c) {
//                case '*':
//                case '?':
//                case '[': {
//                    isExpandable = true;
//                    b.append(charArray[i]);
//                    break;
//                }
//                case '/': {
//                    if (b.length() > 0) {
//                        String bs = b.toString();
//                        if (bs.equals(".")) {
//                            // do nothing;
//                        } else if (bs.equals("..")) {
//                            if (nameParts.size() > 0) {
//                                nameParts.remove(nameParts.size() - 1);
//                            } else {
//                                nameParts.add(bs);
//                            }
//                        } else {
//                            nameParts.add(bs);
//                        }
//                        b.delete(0, b.length());
//                    }
//                    break;
//                }
//                case '\\': {
//                    i++;
//                    b.append(charArray[i]);
////                    if (charArray[i] == '/') {
////                        b.append('/');
////                    } else {
////                        b.append('\\').append(charArray[i]);
////                    }
//                    break;
//                }
//                default: {
//                    b.append(charArray[i]);
//                }
//            }
//        }
//        if (b.length() > 0) {
//            String bs = b.toString();
//            if (bs.equals(".")) {
//                // do nothing;
//            } else if (bs.equals("..")) {
//                if (nameParts.size() > 0) {
//                    nameParts.remove(nameParts.size() - 1);
//                } else {
//                    nameParts.add(bs);
//                }
//            } else {
//                nameParts.add(bs);
//            }
//            b.delete(0, b.length());
//        }
//        if (isExpandable) {
//            while (nameParts.size() > 0 && nameParts.get(0).equals("..")) {
//                nameParts.remove(0);
//                basePath = basePath + "/..";
//            }
//            String[] strings = expandPath2(nameParts.toArray(new String[nameParts.size()]), basePath);
//            if (strings.length == 0) {
//                return new String[]{name};
//            }
//            return strings;
//        } else {
//            //just apply escapes!
//            b = new StringBuilder();
//            for (int i = 0; i < charArray.length; i++) {
//                char c = charArray[i];
//                switch (c) {
//                    case '\\': {
//                        i++;
//                        b.append(charArray[i]);
//                        break;
//                    }
//                    default: {
//                        b.append(charArray[i]);
//                    }
//                }
//            }
//            return new String[]{b.toString()};
//        }
//    }
    protected JShellContext createRootContext() {
        return new DefaultJShellContext(this);
    }

    public JShellContext createContext() {
        return createContext(getRootContext());
    }

    public JShellContext createContext(JShellContext parentContext) {
        return new DefaultJShellContext(parentContext);
    }

    public CommandNode createCommandNode(String[] args) {
        CommandNode n = new CommandNode();
        for (String arg : args) {
            CommandItemHolderNode n2 = new CommandItemHolderNode();
            n2.add(new WordNode(arg));
            n.add(n2);
        }
        return n;
    }

    //    public void executeArguments(String[] command, boolean parse, boolean addToHistory, JShellContext context) {
//        if (addToHistory) {
//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < command.length; i++) {
//                String arg = command[i];
//                if (i > 0) {
//                    sb.append(" ");
//                }
//                if (arg.contains(" ")) {
//                    sb.append("\"").append(arg).append("\"");
//                } else {
//                    sb.append(arg);
//                }
//            }
//            getHistory().add(sb.toString());
//        }
//        if (parse) {
//            CommandNode n = new CommandNode();
//            for (int i = 0; i < command.length; i++) {
//                JShellParser parser = new JShellParser();
//                Node node0 = null;
//                try {
//                    node0 = parser.parse(command[i]);
//                } catch (ParseException e) {
//                    throw new IllegalArgumentException(e);
//                }
//                if (node0 instanceof CommandItemHolderNode) {
//                    n.add((CommandItemHolderNode) node0);
//                } else if (node0 instanceof CommandNode) {
//                    for (CommandItemHolderNode item : ((CommandNode) node0).items) {
//                        n.add(item);
//                    }
//                    if (((CommandNode) node0).nowait) {
//                        n.nowait = true;
//                    }
//                } else {
//                    throw new ClassCastException();
//                }
//            }
//            n.eval(context);
//        } else {
//            createCommandNode(command).eval(context);
//        }
//    }
    public JShellContext getRootContext() {
        if (context == null) {
            context = createRootContext();
        }
        return context;
    }

    public void executeLine(String line, boolean storeResult, JShellContext context) {
        if (context == null) {
            context = getRootContext();
        }
        boolean success = false;
        if (line.trim().length() > 0 && !line.trim().startsWith("#")) {
            try {
                getHistory().add(line);
                InstructionNode nn = parseCommandLine(line);
                nn.eval(context);
                success = true;
            } catch (Throwable e) {
                if (storeResult) {
                    onResult(e, context);
                } else {
                    if (e instanceof RuntimeException) {
                        throw e;
                    }
                    if (e instanceof Error) {
                        throw e;
                    }
                    throw new RuntimeException(e);
                }
            }
            if (storeResult) {
                if (success) {
                    onResult(null, context);
                    try {
                        history.save();
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                }
            }
        }
    }

    public int onResult(Throwable th, JShellContext context) {
        if (th == null) {
            context.setLastResult(new JShellResult(0, null, null));
            return 0;
        }
        if (th instanceof JShellQuitException) {
            throw (JShellQuitException) th;
        }
        if (getErrorHandler().isRequireExit(th)) {
            if (th instanceof RuntimeException) {
                throw (RuntimeException) th;
            }
            throw new JShellQuitException(100, th);
        }

        if (th instanceof JShellException) {
            JShellException je = (JShellException) th;
            int errorCode = je.getResult();
            String lastErrorMessage = getErrorHandler().errorToMessage(th);
            context.setLastResult(new JShellResult(errorCode, lastErrorMessage, th));
            if (errorCode != 0) {
                getErrorHandler().onErrorImpl(lastErrorMessage, th, context);
            }
            return errorCode;
        }

        int errorCode = getErrorHandler().errorToCode(th);
        String lastErrorMessage = getErrorHandler().errorToMessage(th);
        context.setLastResult(new JShellResult(errorCode, lastErrorMessage, th));
        if (errorCode != 0) {
            getErrorHandler().onErrorImpl(lastErrorMessage, th, context);
        }
        return errorCode;
    }

    public int onResult(int errorCode, Throwable th, JShellContext context) {
        if (errorCode != 0) {
            if (th == null) {
                th = new RuntimeException("Error occurred. Error Code #" + errorCode);
            }
        } else {
            th = null;
        }
        String lastErrorMessage = th == null ? null : getErrorHandler().errorToMessage(th);
        context.setLastResult(new JShellResult(errorCode, lastErrorMessage, th));
        if (errorCode != 0) {
            getErrorHandler().onErrorImpl(lastErrorMessage, th, context);
        }
        return errorCode;
    }

    public void executeCommand(String[] command) {
        executeCommand(command, true, true, true, null);
    }

    public void executeCommand(String[] command,
                               boolean aliases, boolean builtins, boolean externals, JShellContext context) {
        if (context == null) {
            context = createContext();
        }
        context.setArgs(command);
        CommandNode n = null;
//        if (true) {
//            n = new CommandNode();
//            for (int i = 0; i < command.length; i++) {
//                JShellParser parser = new JShellParser();
//                Node node0 = null;
//                try {
//                    node0 = parser.parse(command[i]);
//                } catch (ParseException e) {
//                    throw new IllegalArgumentException(e);
//                }
//                if (node0 instanceof CommandItemHolderNode) {
//                    n.add((CommandItemHolderNode) node0);
//                } else if (node0 instanceof CommandNode) {
//                    for (CommandItemHolderNode item : ((CommandNode) node0).items) {
//                        n.add(item);
//                    }
//                    if (((CommandNode) node0).nowait) {
//                        n.nowait = true;
//                    }
//                } else {
//                    throw new ClassCastException();
//                }
//            }
//        } else {
        n = createCommandNode(command);
//        }
        n.aliases = aliases;
        n.builtins = builtins;
        n.external = externals;
        n.eval(context);
    }

    public void addToHistory(String[] command) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < command.length; i++) {
            String arg = command[i];
            if (i > 0) {
                sb.append(" ");
            }
            if (arg.contains(" ")) {
                sb.append("\"").append(arg).append("\"");
            } else {
                sb.append(arg);
            }
        }
        getHistory().add(sb.toString());
    }

    public void executePreparedCommand(String[] command,
                                       boolean considerAliases, boolean considerBuiltins, boolean considerExternal,
                                       JShellContext context
    ) {
        String cmdToken = command[0];
        if (cmdToken.indexOf('/') >= 0 || cmdToken.indexOf('\\') >= 0) {
            final JShellExternalExecutor externalExec = getExternalExecutor();
            if (externalExec == null) {
                throw new JShellException(101, "not found " + cmdToken);
            }
            externalExec.execExternalCommand(command, context);
            //this is a path!
        } else {
            List<String> cmds = new ArrayList<>(Arrays.asList(command));
            String a = considerAliases ? context.aliases().get(cmdToken) : null;
            if (a != null) {
                Node node0 = null;
                try {
//                    JShellParser parser = new JShellParser();
//                    node0 = parser.parse(a);

                    node0 = JShellParser2.fromString(a).parse();

                } catch (Exception ex) {
                    Logger.getLogger(CommandNode.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (node0 instanceof CommandNode) {
                    CommandNode nn = (CommandNode) node0;
                    ArrayList<CommandItemHolderNode> items = nn.items;
                    cmds.remove(0);
                    for (int i = nn.items.size() - 1; i >= 0; i--) {
                        CommandItemHolderNode n = items.get(i);
                        cmds.add(0, n.getPathString(context));
                    }
                } else {
                    throw new IllegalArgumentException("Invalid  alias " + a);
                }
            }
            JShellBuiltin shellCommand = considerBuiltins ? context.builtins().find(cmdToken) : null;
            if (shellCommand != null && shellCommand.isEnabled()) {
                ArrayList<String> arg2 = new ArrayList<String>(cmds);
                arg2.remove(0);
                shellCommand.exec(arg2.toArray(new String[0]), context.createCommandContext(shellCommand));
            } else {
                if (considerExternal) {
                    final JShellExternalExecutor externalExec = getExternalExecutor();
                    if (externalExec == null) {
                        throw new JShellException(101, "not found " + cmdToken);
                    }
                    externalExec.execExternalCommand(cmds.toArray(new String[0]), context);
                } else {
                    throw new JShellException(101, "not found " + cmdToken);
                }
            }
        }
    }

    /**
     * read options
     *
     * @param args
     */
    protected void prepareExecuteShell(String[] args) {
        PrintStream out = System.out;
        PrintStream err = System.err;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("-?".equals(arg)) {
                out.printf("Syntax : shell [<FILE>]\n");
                out.printf("    <FILE> : if present content will be processed as input\n");
                return;
            } else if ("--version".equals(arg)) {
                out.printf("v%s\n", APP_VERSION);
            } else if ("--verbose".equals(arg)) {
                getOptions().setVerbose(true);
            } else if ("-x".equals(arg)) {
                getOptions().setXtrace(true);
            } else if ("--startup-script".equals(arg)) {
                i++;
                startupScript = args[i];
            } else if ("--shutdown-script".equals(arg)) {
                i++;
                shutdownScript = args[i];
            } else if (arg.startsWith("-")) {
                err.printf("Syntax error\n");
                //return -1;
                throw new JShellException(1, "Syntax Error");
            } else {
                input = arg;
                exitAfterProcessingLines = true;
            }
        }
    }

    public void executeShell(String[] args) {
        PrintStream out = System.out;
        JShellContext context = createContext();
        context.setArgs(args);
        prepareContext(context);

        if (input == null) {
            System.out.printf("%s version %s\n", APP_TITLE, APP_VERSION);
        }

        executeFile(startupScript, context, true);

        if (input != null) {
            if (!new File(input).exists()) {
                throw new JShellException(1, "File Not Found " + input);
            }
        }

        if (input == null) {
            try {
                executeInteractive(out);
            } finally {
                executeFile(shutdownScript, context, true);
            }
        } else {
            executeFile(input, context, false);
            executeFile(shutdownScript, context, true);
        }

    }

    protected String readInteractiveLine(JShellContext context) {
        if (_in_reader == null) {
            _in_reader = new BufferedReader(new InputStreamReader(System.in));
        }
        try {
            return _in_reader.readLine();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    protected void printHeader(PrintStream out) {
        //
    }

    protected void executeInteractive(PrintStream out) {
        printHeader(out);

        while (true) {
            String line = null;
            try {
                line = readInteractiveLine(getRootContext());
            } catch (Exception ex) {
                onResult(ex, getRootContext());
                break;
            }
            if (line == null) {
                break;
            }
            if (line.trim().length() > 0) {
                try {
                    executeLine(line, true, null);
                } catch (JShellQuitException q) {
                    if (q.getResult() == 0) {
                        return;
                    }
                    onQuit(q);
                    return;
                }
            }
        }
        onQuit(new JShellQuitException(1, null));
    }

    protected void onQuit(JShellQuitException quitExcepion) {
        try {
            getHistory().save();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        throw quitExcepion;
    }

    public void executeFile(String file, JShellContext context, boolean ignoreIfNotFound) {
        if (file == null || !new File(file).isFile()) {
            if (ignoreIfNotFound) {
                return;
            }
            throw new JShellException(1, "shell file not found : " + file);
        }
        FileInputStream stream = null;
        try {
            try {
                stream = new FileInputStream(file);
                InstructionNode ii = parseCommand(stream);
                if (ii == null) {
                    return;
                }
                JShellContext c = createContext(context).setRoot(ii).setParent(null);
                ii.eval(c);
            } catch (IOException ex) {
                throw new JShellException(1, ex);
            }
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException ex) {
                throw new JShellException(1, ex);
            }
        }
    }

    public void executeString(String text) {
        executeString(text,getRootContext());
    }

    public void executeString(String text, JShellContext context) {
        if (text == null || text.trim().isEmpty()) {
            return;
        }
        try (InputStream stream=new ByteArrayInputStream(text.getBytes())) {
            InstructionNode ii = parseCommand(stream);
            if (ii == null) {
                return;
            }
            JShellContext c = createContext(context).setRoot(ii).setParent(null);
            ii.eval(c);
        } catch (IOException ex) {
            throw new JShellException(1, ex);
        }
    }

    public void uniformException(UnsafeRunnable r) throws JShellUniformException {
        try {
            r.run();
        } catch (JShellUniformException th) {
            throw th;
        } catch (Exception th) {
            if (getErrorHandler().isRequireExit(th)) {
                throw new JShellUniformException(getErrorHandler().errorToCode(th), true, th);
            }
            throw new JShellUniformException(getErrorHandler().errorToCode(th), true, th);
        }
    }

    public int safeEval(InstructionNode n, JShellContext context) {
        boolean success = false;
        try {
            n.eval(context);
            success = true;
        } catch (Exception ex2) {
            return onResult(ex2, context);
        }
        if (success) {
            return onResult(null, context);
        }
        throw new IllegalArgumentException("Unexpected behaviour");
    }

    //    public String getPromptString() {
//        return getPromptString(getRootContext());
//    }
    protected String getPromptString(JShellContext context) {

        String promptValue = context.vars().getAll().getProperty("PS1");
        if (promptValue == null) {
            promptValue = "\\u> ";
        }
        char[] promptChars = promptValue.toCharArray();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < promptChars.length; i++) {
            char c = promptChars[i];
            if (c == '\\' && i < (promptChars.length - 1)) {
                i++;
                c = promptChars[i];
                switch (c) {
                    case 'W': {
                        s.append(context.getCwd());
                        break;
                    }
                    case 'u': {
                        s.append(context.vars().getAll().getProperty("USER", "anonymous"));
                        break;
                    }
                    case 'h': {
                        String h = context.vars().getAll().getProperty("HOST", "nowhere");
                        if (h.contains(".")) {
                            h = h.substring(0, h.indexOf('.'));
                        }
                        s.append(h);
                        break;
                    }
                    case 'H': {
                        s.append(context.vars().getAll().getProperty("HOST", "nowhere"));
                        break;
                    }
                    default: {
                        s.append('\\').append(c);
                        break;
                    }
                }
            } else {
                s.append(c);
            }
        }
        return s.toString();

    }

    public void prepareContext(JShellContext context) {
//        try {
//            cwd = new File(".").getCanonicalPath();
//        } catch (IOException ex) {
//            cwd = new File(".").getAbsolutePath();
//        }
        vars.set(System.getenv());
        setUndefinedStartupEnv("USER", System.getProperty("user.name"));
        setUndefinedStartupEnv("LOGNAME", System.getProperty("user.name"));
        setUndefinedStartupEnv("PATH", ".");
        setUndefinedStartupEnv("PWD", System.getProperty("user.dir"));
        setUndefinedStartupEnv("HOME", System.getProperty("user.home"));
        setUndefinedStartupEnv("PS1", ">");
    }

    private void setUndefinedStartupEnv(String name, String defaultValue) {
        if (vars.get(name) == null) {
            vars.set(name, defaultValue);
        }
    }

    public String evalAsString(CommandItemNode param) {
        System.err.printf("FIX ME PLEASE evalAsString\n");
        return param.toString();
    }

    public String evalAsString(String param, JShellContext context) {
        Properties envs = new Properties();
        Properties processEnvs = context.vars().getAll();
        for (Entry<Object, Object> entry : processEnvs.entrySet()) {
            envs.put(entry.getKey(), entry.getValue());
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < param.length(); i++) {
            char c = param.charAt(i);
            if (c == '$') {
                StringBuilder var = new StringBuilder();
                i++;
                if (i < param.length()) {
                    if (param.charAt(i) != '{') {
                        while (i < param.length()
                                && ((param.charAt(i) >= 'a' && param.charAt(i) <= 'z')
                                || (param.charAt(i) >= 'A' && param.charAt(i) <= 'Z')
                                || (param.charAt(i) >= 'O' && param.charAt(i) <= '9')
                                || (param.charAt(i) == '_'))) {
                            var.append(param.charAt(i++));
                        }
                        i--;
                    } else {
                        i++;//ignore '{'
                        while (i < param.length() && (param.charAt(i) != '}')) {
                            var.append(param.charAt(i++));
                        }
                    }
                } else {
                    var.append('$');
                }
                Object obj = envs.get(var.toString());
                sb.append(obj == null ? "" : String.valueOf(obj));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public String[] findExecFilesInPath(String filePath, String[] classNames, JShellContext context) {
        ArrayList<String> found = new ArrayList<String>();
        File f = new File(filePath);
        if (!f.exists()) {
            return new String[0];
        }
        if (f.isDirectory()) {
            for (String ff : classNames) {
                File f2 = new File(f, ff);
                if (f2.exists()) {
                    found.add(f2.getPath());
                }
            }
        }
        return found.toArray(new String[found.size()]);
    }

    public String[] findClassesInPath(String filePath, String[] classNames, JShellContext context) {
        System.out.printf("findClassesInPath : path=%s should contain? %s\n", filePath, Arrays.asList(classNames).toString());
        ArrayList<String> found = new ArrayList<String>();
        String[] expanded = context.expandPaths(filePath/*, null*/);
        System.out.printf("path=%s expanded to %s\n", filePath, Arrays.asList(expanded));
        for (String fp : expanded) {
            System.out.printf("\tfindClassesInPath : path=%s should contain? %s\n", fp, Arrays.asList(classNames));
            File f = new File(fp);
            if (f.exists()) {
                String[] fileCls = new String[classNames.length];
                for (int i = 0; i < fileCls.length; i++) {
                    fileCls[i] = classNames[i].replace('.', '/') + ".class";

                }
                List<String> clsNames = Arrays.asList(fileCls);
                if (f.isDirectory()) {
                    for (String ff : fileCls) {
                        if (new File(f, ff).exists()) {
                            found.add(ff);
                        }
                    }
                } else {
                    ZipFile zipFile = null;
                    boolean fileFound = false;
                    try {
                        System.out.printf("lookup into %s for %s\n", fp, clsNames);
                        // open a zip file for reading
                        zipFile = new ZipFile(fp);
                        // get an enumeration of the ZIP file entries
                        Enumeration<? extends ZipEntry> e = zipFile.entries();
                        while (e.hasMoreElements()) {
                            ZipEntry entry = e.nextElement();
                            String entryName = entry.getName();
                            for (String ff : fileCls) {
                                if (entryName.equals(ff)) {
                                    found.add(ff);
                                    break;
                                }
                            }
                            if (found.size() == classNames.length) {
                                break;
                            }
                        }

                    } catch (IOException ioe) {
                        //return found;
                    } finally {
                        try {
                            if (zipFile != null) {
                                zipFile.close();
                            }
                        } catch (IOException ioe) {
                            System.err.printf("Error while closing zip file %s\n", ioe);
                        }
                    }
                }
            }
        }
        return found.toArray(new String[found.size()]);
    }

    public InstructionNode parseCommand(InputStream stream) {
        Node node0 = null;
        try {
            node0 = JShellParser2.fromInputStream(stream).parse();
            if (node0 == null) {
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(CommandNode.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (node0 instanceof InstructionNode) {
            return (InstructionNode) node0;
        }
        throw new IllegalArgumentException("Expected node " + node0);
    }

    public InstructionNode parseCommandLine(String line) {
        Node node0 = null;
        try {
            node0 = JShellParser2.fromString(line).parse();
            if (node0 == null) {
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(CommandNode.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (node0 instanceof InstructionNode) {
            return (InstructionNode) node0;
        }
        throw new IllegalArgumentException("Expected node " + line);
    }

    public String escapeString(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '\\':
                case '&':
                case '!':
                case '$':
                case '`':
                case '?':
                case '*':
                case '[':
                case ']': {
                    sb.append('\\');
                    sb.append(c);
                    break;
                }
                default: {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public String escapePath(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '?':
                case '*':
                case '[':
                case ']': {
                    sb.append('\\');
                    sb.append(c);
                    break;
                }
                default: {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
//    public String escapeStringForDoubleQuotes(String s) {
//        StringBuilder sb=new StringBuilder();
//        for (char c: s.toCharArray()) {
//            switch (c){
//                case '\\':
//                case '(':
//                case ')':
//                case '&':
//                case '|':
//                    {
//                    sb.append('\\');
//                    sb.append(c);
//                    break;
//                }
//                default:{
//                    sb.append(c);
//                }
//            }
//        }
//        return sb.toString();
//    }

    public JShellWordEvaluator getWordEvaluator() {
        return wordEvaluator;
    }

    public void setWordEvaluator(JShellWordEvaluator wordEvaluator) {
        this.wordEvaluator = wordEvaluator;
    }

    public void traceExecution(String msg) {
        if (getOptions().isXtrace()) {
            System.out.println("+ " + msg);
        }
    }

    public JShellOptions getOptions() {
        return options;
    }

    public JShellHistory getHistory() {
        return history;
    }

    public String getVersion() {
        return version;
    }

    protected void setVersion(String version) {
        this.version = version;
    }
}
