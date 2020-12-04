package net.thevpc.jshell;

import net.thevpc.jshell.parser.nodes.Node;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

/**
 * Created by vpc on 11/4/16.
 */
public interface JShellContext {

    String getServiceName();

    JShellContext setServiceName(String serviceName);

    String[] getArgsArray();

    List<String> getArgsList();

    JShell getShell();

    Node getRoot();

    JShellContext setRoot(Node root);

    Node getParent();

    JShellContext setParent(Node parent);

    InputStream in();

    PrintStream out();

    PrintStream err();

    JShellVariables vars();

    Watcher bindStreams(InputStream out, InputStream err, OutputStream in);


    JShellFunctionManager functions();

    JShellContext setVars(JShellVariables env);

    JShellContext setOut(PrintStream out);

    JShellContext setErr(PrintStream out);

    JShellContext setIn(InputStream in);

    JShellContext setArgs(String[] args);

    JShellExecutionContext createCommandContext(JShellBuiltin command);

    void setShell(JShell console);

    List<AutoCompleteCandidate> resolveAutoCompleteCandidates(String commandName, List<String> autoCompleteWords, int wordIndex, String autoCompleteLine);

    JShellContext setEnv(Map<String,String> env);

    Map<String, Object> getUserProperties();

    String getCwd();

    JShellFileSystem getFileSystem();

    void setCwd(String cwd);

    void setFileSystem(JShellFileSystem fileSystem);
    
    String getAbsolutePath(String path);

    String[] expandPaths(String path);

    JShellAliasManager aliases();

    void setBuiltins(JShellBuiltinManager commandManager);

    JShellBuiltinManager builtins();

    JShellResult getLastResult();

    void setLastResult(JShellResult result);

    void setAliases(JShellAliasManager aliasManager);

    void copyFrom(JShellContext other);

    JShellContext copy() ;
    interface Watcher{
        void stop();
        boolean isStopped();
    }

}
