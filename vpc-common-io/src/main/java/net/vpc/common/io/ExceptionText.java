package net.vpc.common.io;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExceptionText {
    private int lineNumber;
    private List<String> lines = new ArrayList<String>();

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public void add(String line){
        lines.add(line);
    }
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.lines);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ExceptionText other = (ExceptionText) obj;
        if (!Objects.equals(this.lines, other.lines)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (lines.size() > 0) {
            sb.append(lines.get(0));
        }
        for (int i = 1; i < lines.size(); i++) {
            sb.append("\n");
            sb.append(lines.get(i));

        }
        return sb.toString();
    }

    public String getMessage() {
        return lines.size()>0?lines.get(0):"";
    }
}
