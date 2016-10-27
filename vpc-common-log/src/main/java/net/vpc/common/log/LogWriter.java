package net.vpc.common.log;

import java.io.PrintWriter;
import java.io.StringWriter;

class LogWriter extends PrintWriter {

    public LogWriter() {
        super(new StringWriter());
        lineNum = 0;
        line = null;
        desiredLineNum = 0;
        line = "";
        desiredLineNum = 3;
    }

    protected boolean partOfCallStack(String s) {
        int i;
        for (i = 0; i < s.length() && !Character.isLetter(s.charAt(i)); i++) ;
        return i < s.length() - 1 && s.charAt(i) == 'a' && s.charAt(i + 1) == 't';
    }

    protected boolean partOfCallStack(char[] s) {
        int i;
        for (i = 0; i < s.length && !Character.isLetter(s[i]); i++) ;
        return i < s.length - 1 && s[i] == 'a' && s[i + 1] == 't';
    }

    public void print(char[] s) {
        if (partOfCallStack(s) && ++lineNum == desiredLineNum)
            line = new String(s);
    }

    public void println(char[] s) {
        print(s);
    }

    public void write(char[] s) {
        print(s);
    }

    public void write(char[] s, int offset, int length) {
        print(String.copyValueOf(s, offset, length));
    }

    public void print(Object o) {
        print(o.toString());
    }

    public void println(Object o) {
        print(o.toString());
    }

    public void print(String s) {
        if (partOfCallStack(s) && ++lineNum == desiredLineNum)
            line = s;
    }

    public void println(String s) {
        print(s);
    }

    public void write(String s) {
        print(s);
    }

    public void write(String s, int offset, int length) {
        print(s.substring(offset, offset + length));
    }

    public void print(boolean b) {
        System.out.println("print(boolean) unhandled");
    }

    public void println(boolean b) {
        print(b);
    }

    public void print(char c) {
        System.out.println("print(char) unhandled");
    }

    public void println(char c) {
        print(c);
    }

    public void println() {
        System.out.println("println() unhandled");
    }

    public String toString() {
        return line.trim();
    }

    public void setDesiredLineNum(int line) {
        desiredLineNum = line;
    }

    public StackTrace result() {
        StackTrace position = new StackTrace();
        position.parse(toString());
        return position;
    }

    public void clear() {
        lineNum = 0;
        line = "";
    }

    private int lineNum;
    private String line;
    private int desiredLineNum;
}
