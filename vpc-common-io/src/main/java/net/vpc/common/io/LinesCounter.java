package net.vpc.common.io;

class LinesCounter implements LineVisitor {
    long count;

    @Override
    public boolean nextLine(String line) {
        count++;
        return true;
    }

    public long getCount() {
        return count;
    }
}
