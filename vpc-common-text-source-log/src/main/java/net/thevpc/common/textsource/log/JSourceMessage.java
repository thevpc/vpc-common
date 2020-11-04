package net.thevpc.common.textsource.log;

import net.thevpc.common.textsource.JTextSource;
import net.thevpc.common.textsource.JTextSourceRange;
import net.thevpc.common.textsource.JTextSourceToken;

import java.util.logging.Level;

public class JSourceMessage {

    private String id;
    private String group;
    private String message;
    private JTextSourceToken token;
    private Level level;

    public static JSourceMessage error(String id, String group, String message) {
        return new JSourceMessage(id, group, message, Level.SEVERE, null);
    }

    public static JSourceMessage warning(String id, String group, String message) {
        return new JSourceMessage(id, group, message, Level.WARNING, null);
    }

    public static JSourceMessage error(String id, String group, String message, JTextSourceToken token) {
        return new JSourceMessage(id, group, message, Level.SEVERE, token);
    }

    public static JSourceMessage info(String id, String group, String message, JTextSourceToken token) {
        return new JSourceMessage(id, group, message, Level.INFO, token);
    }

    public static JSourceMessage warning(String id, String group, String message, JTextSourceToken token) {
        return new JSourceMessage(id, group, message, Level.WARNING, token);
    }

    public JSourceMessage(String id, String group, String message, Level level, JTextSourceToken token) {
        this.id = id;
        this.group = group;
        this.message = message;
        this.token = token == null ? null : token.copy();
        this.level = level;
    }

    public String getGroup() {
        return group;
    }

    public Level getLevel() {
        return level;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public JTextSourceToken getToken() {
        return token;
    }

    public JSourceMessage setId(String id) {
        this.id = id;
        return this;
    }

    public JSourceMessage setMessage(String message) {
        this.message = message;
        return this;
    }

    public JSourceMessage setToken(JTextSourceToken token) {
        this.token = token;
        return this;
    }

    public JSourceMessage setLevel(Level level) {
        this.level = level;
        return this;
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean  showCompilationSource) {
        StringBuilder s = new StringBuilder();
        JTextSource compilationUnitSource0 = token == null ? null : token.getSource();
        String compilationUnitSource = compilationUnitSource0 == null ? "" : compilationUnitSource0.name();
        if (showCompilationSource) {
            s.append(String.format("%-10s ", compilationUnitSource));
        }
        if(token!=null) {
            s.append(String.format("[%4s,%3s] ",(token.getStartLineNumber() + 1),(token.getStartColumnNumber() + 1)));
        }else{
            s.append("           ");
        }
        s.append(String.format("%-6s [%-5s] : %s",
                level.intValue() == Level.SEVERE.intValue() ? "ERROR" : level.toString(), id == null ? "" : id, message
        ));
        if (token != null && compilationUnitSource0 != null) {
            s.append(toRangeString(token,compilationUnitSource0,false));
        }
        return s.toString();
    }

    public static String toRangeString(JTextSourceToken token, JTextSource source, boolean includeSourceName){
        StringBuilder s=new StringBuilder();
        if (token != null && source != null) {
            if(includeSourceName){
                s.append(source.name()).append(":");
            }
            long cn = token.getStartCharacterNumber();
            int window = 80;
            JTextSourceRange range = source.range((int) cn - window, (int) cn + window);
            JTextSourceRange.JRangePointer windowString = range.trim(cn,window);
            s.append("\n   ").append(windowString.getText());
            s.append("\n   ");
            for (int i = 0; i < windowString.getOffset(); i++) {
                s.append(" ");
            }
            s.append("^^^ [Line:").append(token.getStartLineNumber() + 1).append(",Column:").append(token.getStartColumnNumber() + 1).append("]");
        }
        return s.toString();
    }
}
