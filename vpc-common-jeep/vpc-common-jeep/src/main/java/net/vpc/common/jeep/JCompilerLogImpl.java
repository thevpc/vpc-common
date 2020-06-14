package net.vpc.common.jeep;

import java.io.PrintStream;
import java.util.*;
import java.util.logging.Level;

public class JCompilerLogImpl implements JCompilerLog {

    private int maxMessages = 1024;
    private List<JCompilerMessage> messages = new ArrayList<>();
    private Set<ErrorKey> visitedMessages = new LinkedHashSet<>();
    private PrintStream out = System.err;

    public JCompilerLogImpl() {
    }

    public JCompilerLogImpl(PrintStream out) {
        this.out = out;
    }

    @Override
    public void error(String id, String group, String message, JToken token) {
        add(JCompilerMessage.error(id, group, message, token));
    }

    @Override
    public void warn(String id, String group, String message, JToken token) {
        add(JCompilerMessage.warning(id, message, token));
    }

    @Override
    public void add(JCompilerMessage message) {
        if (messages.size() < maxMessages) {
            ErrorKey k = new ErrorKey(message);
            if (!messages.isEmpty()) {
                if (visitedMessages.contains(k)) {
                    return;
                }
            }
            visitedMessages.add(k);
            this.messages.add(message);
            if(out!=null) {
                out.println(message);
            }
        }
    }

    @Override
    public int errorCount() {
        int errors = 0;
        for (JCompilerMessage message : messages) {
            if (message.level().intValue() >= Level.SEVERE.intValue()) {
                errors++;
            }
        }
        return errors;
    }

    @Override
    public int warningCount() {
        int warnings = 0;
        for (JCompilerMessage message : messages) {
            if (message.level().intValue() >= Level.WARNING.intValue()) {
                warnings++;
            }
        }
        return warnings;
    }

    @Override
    public void print() {
        if(out!=null){
            print(out);
        }
    }

    @Override
    public void print(PrintStream out) {
        int errors = 0;
        List<JCompilerMessage> errorsList = new ArrayList<>();
        List<JCompilerMessage> warningsList = new ArrayList<>();
        List<JCompilerMessage> otherList = new ArrayList<>();
        for (JCompilerMessage message : messages) {
            if (message.level().intValue() >= Level.SEVERE.intValue()) {
                errors++;
                errorsList.add(message);
            } else if (message.level().intValue() >= Level.WARNING.intValue()) {
                warningsList.add(message);
            } else {
                otherList.add(message);
            }
        }
        printFooter();
        for (JCompilerMessage jCompilerMessage : errorsList) {
            out.println(jCompilerMessage);
        }
        for (JCompilerMessage jCompilerMessage : warningsList) {
            out.println(jCompilerMessage);
        }
        for (JCompilerMessage jCompilerMessage : otherList) {
            out.println(jCompilerMessage);
        }
        printFooter();
    }

    @Override
    public void printFooter() {
        if(out!=null){
            printFooter(out);
        }
    }
    @Override
    public void printFooter(PrintStream out) {
        int errors = 0;
        int warnings = 0;
        List<JCompilerMessage> errorsList = new ArrayList<>();
        List<JCompilerMessage> warningsList = new ArrayList<>();
        List<JCompilerMessage> otherList = new ArrayList<>();
        for (JCompilerMessage message : messages) {
            if (message.level().intValue() >= Level.SEVERE.intValue()) {
                errors++;
                errorsList.add(message);
            } else if (message.level().intValue() >= Level.WARNING.intValue()) {
                warnings++;
                warningsList.add(message);
            } else {
                otherList.add(message);
            }
        }
        out.println("-----------------------------------------------------------------------------------");
        if (errors == 0) {
            out.printf("Compilation successful with %d errors and %d warnings.\n", errors, warnings);
        } else if (warnings == 0) {
            out.printf("Compilation failed with %d errors.\n", errors);
        } else {
            out.printf("Compilation failed with %d errors and %d warnings.\n", errors, warnings);
        }
    }

    @Override
    public JCompilerLog clear() {
        messages.clear();
        return this;
    }

    @Override
    public JCompilerLog copy() {
        JCompilerLogImpl c = new JCompilerLogImpl();
        c.messages.addAll(messages);
        return c;
    }

    @Override
    public boolean isSuccessful() {
        return errorCount() == 0;
    }

    @Override
    public JCompilerMessage[] toArray() {
        return messages.toArray(new JCompilerMessage[0]);
    }

    @Override
    public List<JCompilerMessage> toList() {
        return new ArrayList<>(messages);
    }

    @Override
    public int size() {
        return messages.size();
    }

    @Override
    public Iterator<JCompilerMessage> iterator() {
        return messages.iterator();
    }

    private static class ErrorKey {

        String id;
        long tokenId;
        String compilationUnit;

        public ErrorKey(JCompilerMessage m) {
            String id = m.id();
            if (id == null) {
                id = m.message();
            }
            this.id = id + "::" + m.message();
            JToken token = m.token();
            this.tokenId = token == null ? -1 : token.tokenNumber;
            this.compilationUnit = (token == null || token.compilationUnit == null || token.compilationUnit.getSource() == null) ? ""
                    : token.compilationUnit.getSource().name();
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, tokenId, compilationUnit);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ErrorKey errorKey = (ErrorKey) o;
            return tokenId == errorKey.tokenId
                    && Objects.equals(id, errorKey.id)
                    && Objects.equals(compilationUnit, errorKey.compilationUnit);
        }
    }
}
