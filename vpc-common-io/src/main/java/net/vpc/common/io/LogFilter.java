/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author vpc
 */
public class LogFilter implements Iterable<ExceptionText> {

    public static final Pattern COMPILED_AT = Pattern.compile("\tat [A-Za-z][.A-Za-z0-9_$<>]*\\(.*\\).*");
    public static final Pattern COMPILED_CAUSED_BY = Pattern.compile("Caused by:.*");
    public static final Pattern COMPILED_DASHES = Pattern.compile("--- .*");
    public static final Pattern COMPILED_ORA = Pattern.compile("ORA-.*: .*");
    public static final Pattern COMPILED_MORE = Pattern.compile("\\.\\.\\. .* more.*");
    public static final Pattern COMPILED_EXCEPTION = Pattern.compile(".*Exception:.*");
    private List<ExceptionTextListener> listeners = new ArrayList<>();
    private Iterable<String> lines;
    private List<Pattern> prefixes = new ArrayList<>();
    private Map<String, String> messageReplacements = new HashMap<>();

    public LogFilter(Iterable<String> lines) {
        this.lines = lines;
    }



    private static StringLocation locationOfRegexp(String value, Pattern regexp) {
        Matcher matcher = regexp.matcher(value);
        if (matcher.find()) {
            return new StringLocation(matcher.start(), matcher.end());
        }
        return null;
    }

    public void addMessageReplacement(String regexp) {
        addMessageReplacementExact(".*"+regexp+".*");
    }

    public void addMessageReplacementExact(String regexp) {
        StringBuilder r=new StringBuilder();
        String replaced = regexp.replace("[^ ]*", "<?>");
        if(replaced.startsWith(".*")){
            replaced=replaced.substring(2);
        }
        if(replaced.endsWith(".*")){
            replaced=replaced.substring(0,replaced.length()-2);
        }
        char[] chars = replaced.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(i+1<chars.length && chars[i]=='.' && chars[i+1]=='*') {
                i++;
                r.append("<?>");
            }else if(i+1<chars.length && chars[i]=='.' && chars[i+1]=='+'){
                i++;
                r.append("<?>");
            }else if(i<chars.length && chars[i]=='\\'){
                //ignore
            }else{
                r.append(chars[i]);
            }
        }
        messageReplacements.put(regexp, r.toString());
    }

    public void addLinePrefix(String prefix) {
        prefixes.add(Pattern.compile(prefix));
    }

    public void addListener(ExceptionTextListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(ExceptionTextListener listener) {
        this.listeners.remove(listener);
    }

    public Iterator<ExceptionText> iterator() {
        return new Iterator<ExceptionText>() {
            Iterator<String> currentLines = lines.iterator();
            String lastLine = null;
            String line = null;
            ExceptionText e = null;
            ExceptionText ok = null;
//            List<String> all = new ArrayList<String>();
            private int lineNumber=0;
            private boolean hintAcceptNextEmpty=false;
            @Override
            public boolean hasNext() {
                boolean was_hintAcceptNextEmpty;
                while (currentLines.hasNext()) {
                    was_hintAcceptNextEmpty=hintAcceptNextEmpty;
                    hintAcceptNextEmpty=false;
                    lineNumber++;
                    line = currentLines.next();
//                    all.add(line);
                    //System.out.println("process " + line);
                    StringLocation loc = locationOfRegexp(line, COMPILED_AT);
                    if (loc == null) {
                        loc = locationOfRegexp(line, COMPILED_CAUSED_BY);
                    }
                    if (loc == null) {
                        loc = locationOfRegexp(line, COMPILED_DASHES);
                        if(loc!=null) {
                            String x = filterFirstLine(line).trim();
                            if (x.startsWith("---")) {
                                System.out.print("");
                                if(e!=null){
                                    //okkay
                                }else if(x.startsWith("--- The error occurred")){
                                    //okkay
                                }else{
                                    loc=null;
                                }
                            } else {
                                loc = null;
                            }
                        }
                    }
                    if (loc == null) {
                        loc = locationOfRegexp(line, COMPILED_ORA);
                        if(loc!=null){
                            String x = filterFirstLine(line).trim();
                            if (x.startsWith("ORA-")) {
                                hintAcceptNextEmpty=true;
//                                System.out.print("");
                            }
                        }
                    }
                    if (loc == null) {
                        loc = locationOfRegexp(line, COMPILED_MORE);
                        if(loc!=null){
                            if(e==null){
                                //should not be the very first line
                                loc=null;
                            }
                        }
                    }
                    if (loc == null) {
                        loc = locationOfRegexp(line, COMPILED_EXCEPTION);
                        if(loc!=null) {
                            if(e !=null) {
                                ok = e;
                                for (ExceptionTextListener listener : listeners) {
                                    listener.onExceptionText(new ExceptionTextEvent(LogFilter.this.lines, ok));
                                }

                                e = new ExceptionText();
                                e.setLineNumber(lineNumber);
                                e.add(filterFirstLine(loc.substring(line)).trim());
                                lastLine = null;//line;
                                continue;
//                                return true;
                            }else {
                                e = new ExceptionText();
                                e.setLineNumber(lineNumber);
                                e.add(filterFirstLine(loc.substring(line)).trim());
                                lastLine = null;//line;
                                continue;
                            }
                        }
                    }
                    if (loc != null) {
                        if (e == null) {
                            e = new ExceptionText();
                            if (lastLine != null) {
                                e.setLineNumber(lineNumber-1);
                                String s=filterFirstLine(lastLine);
                                if(s.trim().length()>0) {
                                    e.add(filterFirstLine(lastLine).trim());
                                }
                            }else{
                                e.setLineNumber(lineNumber);
                            }
                        }
                        e.add(loc.substring(line).trim());
                        lastLine=line;
                    } else if (e != null) {
                        if(line.isEmpty() && was_hintAcceptNextEmpty){
                            e.add(line);
                            lastLine = line;
                        }else {
                            ok = e;
                            for (ExceptionTextListener listener : listeners) {
                                listener.onExceptionText(new ExceptionTextEvent(LogFilter.this.lines, ok));
                            }
                            e = null;
                            lastLine = line;
                            return true;
                        }
                    }else{
//                        System.err.println("IGNORE :::: "+line);
                        lastLine = line;
                    }
                }
                if (e != null) {
                    ok = e;
                    for (ExceptionTextListener listener : listeners) {
                        listener.onExceptionText(new ExceptionTextEvent(LogFilter.this.lines, ok));
                    }
                    e = null;
                    return true;
                }
                return false;
            }

            @Override
            public ExceptionText next() {
                return ok;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }

    private String filterFirstLine(String name) {
        while (true) {
            int resolved = 0;
            for (Pattern prefix : prefixes) {
                name = name.trim();
                StringLocation loc = locationOfRegexp(name, prefix);
                if (loc != null && loc.getStart() == 0) {
//                    String name0 = loc.extractFrom(name);
//                    if(name0.equals(".") || name0.isEmpty()){
//                        //if(name.contains("Check the statement")) {
//                            System.out.println("Why " + name);
//                        //}
//                    }
                    name = loc.extractFrom(name);
                    resolved++;
                }
            }
            if (resolved <= 0) {
                break;
            }
        }
        name=name.replaceAll("@[a-z0-9]{8}","@<?>");
        for (Map.Entry<String, String> entry : messageReplacements.entrySet()) {
            if (name.matches(entry.getKey())) {
                return entry.getValue();
            }
        }
        return name;
    }

    private static class StringLocation {

        private final int start;
        private final int end;

        public StringLocation(int start, int end) {
            if (start < 0 || end < 0) {
                throw new IllegalArgumentException("Why");
            }
            this.start = start;
            this.end = end;
        }

        public int length() {
            return end - start;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public String substring(String line) {
            return line.substring(start, end);
        }

        public String extractFrom(String line) {
            return (start <= 0 ? "" : line.substring(0, start)) + (end >= line.length() ? "" : line.substring(end));
        }

        public String substringFrom(String line) {
            return line.substring(start);
        }

    }
}
