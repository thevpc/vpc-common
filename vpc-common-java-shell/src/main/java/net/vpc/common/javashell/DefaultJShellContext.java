/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.javashell;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

import net.vpc.common.javashell.parser.nodes.Node;
import net.vpc.common.javashell.util.ShellUtils;

/**
 *
 * @author vpc
 */
public class DefaultJShellContext extends AbstractJShellContext {

    private static final JShellResult OK_RESULT = new JShellResult(0, null, null);
    private JShell shell;
    private JShellVariables vars = new JShellVariables();
    private Node root;
    private Node parent;
    private InputStream in = System.in;
    private PrintStream out = System.out;
    private PrintStream err = System.err;
    private List<String> args = new ArrayList<String>();
    private Map<String, Object> userProperties = new HashMap<>();
    private JShellFunctionManager functionManager = new DefaultJShellFunctionManager();
    private JShellAliasManager aliasManager = new DefaultAliasManager();
    private JShellBuiltinManager builtinsManager;
    private String cwd;
    private JShellFileSystem fileSystem;
    public String oldCommandLine = null;
    public String serviceName = null;
    public int commandLineIndex = -1;
    public JShellResult lastResult = OK_RESULT;

    public DefaultJShellContext() {
        this.vars = new JShellVariables();
        setFileSystem(new DefaultJShellFileSystem());
    }

    public DefaultJShellContext(JShell shell) {
        setShell(shell);
        setFileSystem(new DefaultJShellFileSystem());
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public JShellContext setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    @Override
    public JShellBuiltinManager builtins() {
        return builtinsManager;
    }

    @Override
    public void setBuiltins(JShellBuiltinManager builtinsManager) {
        this.builtinsManager = builtinsManager;
    }

//    public DefaultJShellContext(JShell shell, JShellFunctionManager functionManager, JShellAliasManager aliasManager,JShellVariables env, Node root, Node parent, InputStream in, PrintStream out, PrintStream err, String... args) {
//        setShell(shell);
//        setVars(env);
//        setAliases(aliasManager);
//        setFunctionManager(functionManager);
//        setRoot(root);
//        setParent(parent);
//        setIn(in);
//        setOut(out);
//        setErr(err);
//        setArgs(args);
//    }
    public DefaultJShellContext(JShellContext other) {
        copyFrom(other);
    }

    @Override
    public JShellAliasManager aliases() {
        return aliasManager;
    }

    public void setAliases(JShellAliasManager aliasManager) {
        this.aliasManager = aliasManager == null ? new DefaultAliasManager() : aliasManager;
    }

    public void copyFrom(JShellContext other) {
        if (other != null) {
            this.shell = other.getShell();
            this.vars = other.vars();
            this.functionManager = other.functions();
            this.aliasManager = other.aliases();
            this.builtinsManager = other.builtins();
            this.root = other.getRoot();
            this.parent = other.getParent();
            this.in = other.in();
            this.out = other.out();
            this.err = other.err();
            this.args = new ArrayList<String>(other.getArgsList());
            this.userProperties = new HashMap<>();
            this.userProperties.putAll(other.getUserProperties());
            setFileSystem(other.getFileSystem());
            this.cwd = other.getCwd();
        }
    }

    public JShellContext copy() {
        DefaultJShellContext c = new DefaultJShellContext();
        c.copyFrom(this);
        return c;
    }

    @Override
    public void setFileSystem(JShellFileSystem fileSystem) {
        this.fileSystem = fileSystem;
        setCwd(this.fileSystem.getDefaultWorkingDir());
    }

    @Override
    public JShellFunctionManager functions() {
        return functionManager;
    }

    @Override
    public Map<String, Object> getUserProperties() {
        return userProperties;
    }

    @Override
    public String[] getArgsArray() {
        return getArgsList().toArray(new String[getArgsList().size()]);
    }

    @Override
    public List<String> getArgsList() {
        return args;
    }

    @Override
    public JShell getShell() {
        return shell;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public Node getParent() {
        return parent;
    }

    @Override
    public InputStream in() {
        return in;
    }

    @Override
    public PrintStream out() {
        return out;
    }

    @Override
    public PrintStream err() {
        return err;
    }

    @Override
    public JShellVariables vars() {
        return vars;
    }

    public JShellContext setVars(JShellVariables vars) {
        this.vars = vars == null ? new JShellVariables() : vars;
        return this;
    }

    public JShellContext setRoot(Node root) {
        this.root = root;
        return this;
    }

    @Override
    public JShellContext setParent(Node parent) {
        this.parent = parent;
        return this;
    }

    public JShellContext setIn(InputStream in) {
        this.in = in == null ? System.in : in;
        return this;
    }

    public JShellContext setOut(PrintStream out) {
        this.out = out == null ? System.out : out;
        return this;
    }

    public JShellContext setErr(PrintStream err) {
        this.err = err == null ? System.err : err;
        return this;
    }

    public JShellContext setArgs(List<String> args) {
        this.args = args;
        return this;
    }

    public JShellContext setArgs(String[] args) {
        this.args = args == null ? new ArrayList<String>() : new ArrayList<String>(Arrays.asList(args));
        return this;
    }

    public JShellExecutionContext createCommandContext(JShellBuiltin command) {
        return new DefaultJShellExecutionContext(this);
    }

    @Override
    public void setShell(JShell shell) {
        this.shell = shell;
        vars.setParent(shell.vars());
    }

    @Override
    public List<AutoCompleteCandidate> resolveAutoCompleteCandidates(String commandName, List<String> autoCompleteWords, int wordIndex, String autoCompleteLine) {
        return new ArrayList<>();
    }

    @Override
    public JShellContext setEnv(Map<String, String> env) {
        this.vars = new JShellVariables();
        if (env != null) {
            this.vars.set(env);
        }
        return this;
    }

    public void setFunctionManager(JShellFunctionManager functionManager) {
        this.functionManager = functionManager == null ? new DefaultJShellFunctionManager() : functionManager;
    }

    @Override
    public void setCwd(String cwd) {
        this.cwd = getFileSystem().normalizeWorkingDir(cwd);
    }

    @Override
    public String getCwd() {
        return cwd;
    }

    @Override
    public JShellFileSystem getFileSystem() {
        return fileSystem;
    }

    @Override
    public String getAbsolutePath(String path) {
        if (new File(path).isAbsolute()) {
            return getFileSystem().getAbsolutePath(path);
        }
        return getFileSystem().getAbsolutePath(getCwd() + "/" + path);
    }

    @Override
    public String[] expandPaths(String path) {
        return ShellUtils.expandPath(path, new File(getCwd()));
    }

    @Override
    public JShellResult getLastResult() {
        return lastResult;
    }

    @Override
    public void setLastResult(JShellResult lastResult) {
        this.lastResult = lastResult == null ? OK_RESULT : lastResult;
    }

}
