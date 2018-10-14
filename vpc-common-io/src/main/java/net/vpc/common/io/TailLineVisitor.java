package net.vpc.common.io;

import java.util.LinkedList;

class TailLineVisitor implements LineVisitor {
    private final int max;
    private LinkedList<String> lines;
    private int count;

    public TailLineVisitor(int max) {
        this.max = max;
        lines = new LinkedList<String>();
        count = 0;
    }

    @Override
    public boolean nextLine(String line) {
        lines.add(line);
        count++;
        if(count> max) {
            lines.remove();
        }
        return true;
    }

    public LinkedList<String> getLines() {
        return lines;
    }
}
