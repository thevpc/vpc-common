package net.vpc.common.textsource.log;

import net.vpc.common.textsource.JTextSourceToken;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

public interface JTextSourceLog extends JMessageList{
    int errorCountAtLine(int line);

    int errorCount();

    int warningCount();

    JTextSourceLog clear();

    JTextSourceLog copy();

    boolean isSuccessful();

    JSourceMessage[] toArray();

    List<JSourceMessage> toList();

    int size();

    void print();

    void print(PrintStream out);

    void printFooter();

    void printFooter(PrintStream out);
}
