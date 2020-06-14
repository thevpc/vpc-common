package net.vpc.common.jeep;

import java.util.logging.Level;

public class JCompilerMessage {

    private String id;
    private String group;
    private String message;
    private JToken token;
    private Level level;

    public static JCompilerMessage error(String id, String message) {
        return new JCompilerMessage(id, message, Level.SEVERE, null);
    }

    public static JCompilerMessage warning(String id, String message) {
        return new JCompilerMessage(id, message, Level.WARNING, null);
    }

    public static JCompilerMessage error(String id, String group, String message, JToken token) {
        return new JCompilerMessage(id, message, Level.SEVERE, token);
    }

    public static JCompilerMessage warning(String id, String message, JToken token) {
        return new JCompilerMessage(id, message, Level.WARNING, token);
    }

    public JCompilerMessage(String id, String message, Level level, JToken token) {
        this.id = id;
        this.message = message;
        this.token = token == null ? null : token.copy();
        this.level = level;
    }

    public Level level() {
        return level;
    }

    public String id() {
        return id;
    }

    public String message() {
        return message;
    }

    public JToken token() {
        return token;
    }

    public JCompilerMessage id(String id) {
        this.id = id;
        return this;
    }

    public JCompilerMessage message(String message) {
        this.message = message;
        return this;
    }

    public JCompilerMessage token(JToken token) {
        this.token = token;
        return this;
    }

    public JCompilerMessage level(Level level) {
        this.level = level;
        return this;
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean  showCompilationSource) {
        StringBuilder s = new StringBuilder();
        JCompilationUnit compilationUnit = token == null ? null : token.compilationUnit;
        String compilationUnitSource = compilationUnit == null ? "" : token.compilationUnit.getSource().name();
        if (showCompilationSource) {
            s.append(String.format("%-10s ", compilationUnitSource));
        }
        if(token!=null) {
            s.append(String.format("[%4s,%3s] ",(token.startLineNumber + 1),(token.startColumnNumber + 1)));
        }else{
            s.append("           ");
        }
        s.append(String.format("%-6s [%-5s] : %s",
                level.intValue() == Level.SEVERE.intValue() ? "ERROR" : level.toString(), id == null ? "" : id, message
        ));
        if (token != null && compilationUnit != null) {
            s.append(toRangeString(token,compilationUnit.getSource(),false));
        }
        return s.toString();
    }

    public static String toRangeString(JToken token,JSource source,boolean includeSourceName){
        StringBuilder s=new StringBuilder();
        if (token != null && source != null) {
            if(includeSourceName){
                s.append(source.name()).append(":");
            }
            long cn = token.startCharacterNumber;
            int window = 80;
            JCharRange range = source.range((int) cn - window, (int) cn + window);
            JCharRange.JRangePointer windowString = range.trim(cn,window);
            s.append("\n   ").append(windowString.getText());
            s.append("\n   ");
            for (int i = 0; i < windowString.getOffset(); i++) {
                s.append(" ");
            }
            s.append("^^^ [Line:").append(token.startLineNumber + 1).append(",Column:").append(token.startColumnNumber + 1).append("]");
        }
        return s.toString();
    }
}
