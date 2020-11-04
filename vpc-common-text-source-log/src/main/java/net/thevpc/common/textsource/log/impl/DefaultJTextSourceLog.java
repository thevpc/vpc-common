package net.thevpc.common.textsource.log.impl;

import net.thevpc.common.textsource.JTextSourceToken;
import net.thevpc.common.textsource.log.JSourceMessage;
import net.thevpc.common.textsource.log.JTextSourceLog;

import java.io.PrintStream;
import java.util.*;
import java.util.logging.Level;

public class DefaultJTextSourceLog implements JTextSourceLog, Cloneable {

    public static final String DEFAULT_OPERATION_NAME = "Operation";
    private int maxMessages = 1024;
    private String operationName;
    private List<JSourceMessage> messages = new ArrayList<>();
    private Set<ErrorKey> visitedMessages = new LinkedHashSet<>();
    private PrintStream out = System.err;

    public DefaultJTextSourceLog(String operationName) {
        this.operationName = operationName == null ? DEFAULT_OPERATION_NAME : operationName;
    }

    public DefaultJTextSourceLog(String operationName, PrintStream out) {
        this.operationName = operationName == null ? DEFAULT_OPERATION_NAME : operationName;
        this.out = out;
    }

    public String getOperationName() {
        return operationName;
    }

    protected DefaultJTextSourceLog setOperationName(String operationName) {
        this.operationName = operationName == null ? DEFAULT_OPERATION_NAME : operationName;
        return this;
    }

    @Override
    public void info(String id, String group, String message, JTextSourceToken token) {
        add(JSourceMessage.info(id, group, message, token));
    }

    @Override
    public void error(String id, String group, String message, JTextSourceToken token) {
        add(JSourceMessage.error(id, group, message, token));
    }

    @Override
    public void warn(String id, String group, String message, JTextSourceToken token) {
        add(JSourceMessage.warning(id, group, message, token));
    }

    @Override
    public void add(JSourceMessage message) {
        if (messages.size() < maxMessages) {
            ErrorKey k = new ErrorKey(message);
            if (!messages.isEmpty()) {
                if (visitedMessages.contains(k)) {
                    return;
                }
            }
            visitedMessages.add(k);
            this.messages.add(message);
            if (out != null) {
                out.println(message);
            }
        }
    }

    @Override
    public int errorCountAtLine(int line) {
        int errors = 0;
        for (JSourceMessage message : messages) {
            if (message.getLevel().intValue() >= Level.SEVERE.intValue()) {
                JTextSourceToken token = message.getToken();
                int s0 = token.getStartLineNumber();
                int s1 = token.getEndLineNumber();
                if (s0 <= line && s1 > line + 1) {
                    errors++;
                }
            }
        }
        return errors;
    }

    @Override
    public int errorCount() {
        int errors = 0;
        for (JSourceMessage message : messages) {
            if (message.getLevel().intValue() >= Level.SEVERE.intValue()) {
                errors++;
            }
        }
        return errors;
    }

    @Override
    public int warningCount() {
        int warnings = 0;
        for (JSourceMessage message : messages) {
            if (message.getLevel().intValue() >= Level.WARNING.intValue()) {
                warnings++;
            }
        }
        return warnings;
    }

    @Override
    public JTextSourceLog clear() {
        messages.clear();
        return this;
    }

    @Override
    public JTextSourceLog copy() {
        DefaultJTextSourceLog c = null;
        try {
            c = (DefaultJTextSourceLog) clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException(e);
        }
        c.messages = new ArrayList<>();
        for (JSourceMessage message : messages) {
            c.add(message);
        }
        return c;
    }

    @Override
    public boolean isSuccessful() {
        return errorCount() == 0;
    }

    @Override
    public JSourceMessage[] toArray() {
        return messages.toArray(new JSourceMessage[0]);
    }

    @Override
    public List<JSourceMessage> toList() {
        return new ArrayList<>(messages);
    }

    @Override
    public int size() {
        return messages.size();
    }

    @Override
    public Iterator<JSourceMessage> iterator() {
        return messages.iterator();
    }

    public void print() {
        if (out != null) {
            print(out);
        }
    }

    public void print(PrintStream out) {
        int errors = 0;
        List<JSourceMessage> errorsList = new ArrayList<>();
        List<JSourceMessage> warningsList = new ArrayList<>();
        List<JSourceMessage> otherList = new ArrayList<>();
        for (JSourceMessage message : messages) {
            if (message.getLevel().intValue() >= Level.SEVERE.intValue()) {
                errors++;
                errorsList.add(message);
            } else if (message.getLevel().intValue() >= Level.WARNING.intValue()) {
                warningsList.add(message);
            } else {
                otherList.add(message);
            }
        }
        printFooter();
        for (JSourceMessage jSourceMessage : errorsList) {
            out.println(jSourceMessage);
        }
        for (JSourceMessage jSourceMessage : warningsList) {
            out.println(jSourceMessage);
        }
        for (JSourceMessage jSourceMessage : otherList) {
            out.println(jSourceMessage);
        }
        printFooter();
    }

    public void printFooter() {
        if (out != null) {
            printFooter(out);
        }
    }

    public void printFooter(PrintStream out) {
        int errors = 0;
        int warnings = 0;
        List<JSourceMessage> errorsList = new ArrayList<>();
        List<JSourceMessage> warningsList = new ArrayList<>();
        List<JSourceMessage> otherList = new ArrayList<>();
        for (JSourceMessage message : messages) {
            if (message.getLevel().intValue() >= Level.SEVERE.intValue()) {
                errors++;
                errorsList.add(message);
            } else if (message.getLevel().intValue() >= Level.WARNING.intValue()) {
                warnings++;
                warningsList.add(message);
            } else {
                otherList.add(message);
            }
        }
        out.println("-----------------------------------------------------------------------------------");
        if (operationName == null) {
            operationName = DEFAULT_OPERATION_NAME;
        }
        if (errors == 0) {
            out.printf("%s successful with %d errors and %d warnings.\n", operationName, errors, warnings);
        } else if (warnings == 0) {
            out.printf("%s failed with %d errors.\n", operationName, errors);
        } else {
            out.printf("%s failed with %d errors and %d warnings.\n", operationName, errors, warnings);
        }
    }

    @Override
    public void addAll(Iterable<JSourceMessage> messages) {
        if (messages != null) {
            for (JSourceMessage message : messages) {
                add(message);
            }
        }
    }

}
