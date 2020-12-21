package net.thevpc.jshell;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

public interface JShellFileContext {
    JShellContext getShellContext();

    String getServiceName();

    void setServiceName(String serviceName);
    void setArgs(String[] args);
    String getArg(int index);

    int getArgsCount();

    String[] getArgsArray();

    List<String> getArgsList();

    JShell getShell();

    JShellNode getRoot();

    JShellFileContext setRoot(JShellNode root);

    JShellNode getParent();

    JShellFileContext setParent(JShellNode parent);

    InputStream in();

    PrintStream out();

    PrintStream err();

    JShellVariables vars();

    JShellContext.Watcher bindStreams(InputStream out, InputStream err, OutputStream in);


    JShellFunctionManager functions();


    JShellFileContext setOut(PrintStream out);

    JShellFileContext setErr(PrintStream out);

    JShellFileContext setIn(InputStream in);


    JShellExecutionContext createCommandContext(JShellBuiltin command);

    List<JShellAutoCompleteCandidate> resolveAutoCompleteCandidates(String commandName, List<String> autoCompleteWords, int wordIndex, String autoCompleteLine);

    JShellFileContext setEnv(Map<String,String> env);

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
}
