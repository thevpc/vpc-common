package net.vpc.common.log;

import java.io.Serializable;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class StackTrace
        implements Serializable {

    public static synchronized String currentStackLine(Throwable throwable, int level) {
        writer.clear();
        writer.setDesiredLineNum(level);
        throwable.printStackTrace(writer);
        return writer.toString();
    }

    public StackTrace() {
        stackTrace = null;
        exception = null;
        classname = null;
        function = null;
        filename = null;
        lineNumber = 0;
        currentLevel = 0;
        classname = "";
        function = "";
        filename = "";
        lineNumber = -1;
        stackTrace = new Throwable();
        setLevel(0);
    }

    public StackTrace(int numLevelsUp) {
        stackTrace = null;
        exception = null;
        classname = null;
        function = null;
        filename = null;
        lineNumber = 0;
        currentLevel = 0;
        classname = "";
        function = "";
        filename = "";
        lineNumber = -1;
        stackTrace = new Throwable();
        setLevel(numLevelsUp);
    }

    public StackTrace(Exception exception) {
        stackTrace = null;
        this.exception = null;
        classname = null;
        function = null;
        filename = null;
        lineNumber = 0;
        currentLevel = 0;
        classname = "";
        function = "";
        filename = "";
        lineNumber = -1;
        stackTrace = new Throwable();
        this.exception = exception;
        setLevel(-1);
    }

    public StackTrace(Exception exception, int numLevelsUp) {
        stackTrace = null;
        this.exception = null;
        classname = null;
        function = null;
        filename = null;
        lineNumber = 0;
        currentLevel = 0;
        classname = "";
        function = "";
        filename = "";
        lineNumber = -1;
        stackTrace = new Throwable();
        this.exception = exception;
        setLevel(numLevelsUp - 1);
    }

    public void setLevel(int numLevelsUp) {
        if (currentLevel != numLevelsUp + 2) {
            currentLevel = numLevelsUp + 2;
            Throwable throwable = exception == null ? stackTrace : exception;
            String line = currentStackLine(throwable, currentLevel);
            if (line.equals(""))
                line = currentStackLine(stackTrace, 1);
            parse(line);
        }
    }

    public int getLevel() {
        return currentLevel - 2;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getClassname() {
        return classname;
    }

    public String getShortClassname() {
        try {
            return classname.substring(classname.lastIndexOf(46) + 1, classname.length());
        } catch (Exception _ex) {
            return classname;
        }
    }

    public String getFunction() {
        return function;
    }

    public String getFilename() {
        return filename;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getLineNumberString() {
        return lineNumber >= 0 ? String.valueOf(lineNumber) : "";
    }

    protected void parse(String raw) {
        lineNumber = 0;
        filename = "";
        function = "";
        classname = "";
        int startLineNumber = 0;
        try {
            startLineNumber = raw.lastIndexOf(58);
            String lineNumberString = raw.substring(startLineNumber + 1, raw.length() - 1);
            lineNumber = Integer.valueOf(lineNumberString).intValue();
        } catch (Exception _ex) {
        }
        int startFilename = 0;
        try {
            startFilename = raw.lastIndexOf(40);
            if (startLineNumber <= 0) {
                startLineNumber = raw.indexOf(41, startFilename);
                if (startLineNumber <= 0)
                    startLineNumber = raw.length() - 1;
            }
            filename = raw.substring(startFilename + 1, startLineNumber);
        } catch (Exception _ex) {
        }
        int startFunction = 0;
        try {
            startFunction = raw.lastIndexOf(46, startFilename);
            function = raw.substring(startFunction + 1, startFilename);
        } catch (Exception _ex) {
        }
        try {
            int startClassname = raw.lastIndexOf(32, startFunction);
            if (startClassname == -1)
                startClassname = 0;
            else
                startClassname++;
            classname = raw.substring(startClassname, startFunction);
        } catch (Exception _ex) {
        }
    }

    public String toString() {
        return "class=" + getClassname() + " function=" + getFunction() + " file=" + getFilename() + " line=" + getLineNumberString();
    }

    public Throwable getStackTrace() {
        return stackTrace;
    }

    public static String toString(Throwable t) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(b);
        t.printStackTrace(ps);
        return b.toString();
    }
    
    private static LogWriter writer = new LogWriter();
    private Throwable stackTrace;
    private Throwable exception;
    private String classname;
    private String function;
    private String filename;
    private int lineNumber;
    private int currentLevel;

}
