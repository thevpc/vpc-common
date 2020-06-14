package net.vpc.common.jeep;

import java.io.PrintStream;
import java.util.List;

public interface JCompilerLog extends Iterable<JCompilerMessage> {

    JCompilerMessage[] toArray();

    List<JCompilerMessage> toList();

    int size();

    /**
     * 
     * @param id id
     * @param group group or null
     * @param message message
     * @param token location
     */
    void error(String id, String group, String message, JToken token);

    void warn(String id, String group, String message, JToken token);

    void add(JCompilerMessage message);

    int errorCount();

    int warningCount();

    void print();

    void print(PrintStream out);

    void printFooter();

    void printFooter(PrintStream out);

    JCompilerLog clear();

    JCompilerLog copy();

    boolean isSuccessful();
}
