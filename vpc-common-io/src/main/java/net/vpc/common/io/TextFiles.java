package net.vpc.common.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class TextFiles {

    private static List<LineSourceFactory> factories = null;

    public static LineSourceFactory getBest(String fileName, String mimetype) {
        int x = -1;
        LineSourceFactory b = null;
        for (LineSourceFactory factory : getLineSourceFactories()) {
            int v = factory.getSupport(fileName, mimetype);
            if (v >= 0 && (v > x || b == null)) {
                x = v;
                b = factory;
            }
        }
        if (b == null) {
            throw new IllegalArgumentException("No such LineSourceFactory for " + fileName + " , " + mimetype);
        }
        return b;
    }

    public static List<LineSourceFactory> getLineSourceFactories() {
        if (factories == null) {
            factories = new ArrayList<>();
            ServiceLoader<LineSourceFactory> serviceLoader = ServiceLoader.load(LineSourceFactory.class, Thread.currentThread().getContextClassLoader());
            for (LineSourceFactory lineSourceFactory : serviceLoader) {
                factories.add(lineSourceFactory);
            }
        }
        return factories;
    }

    public static LineSource create(String file) {
        return getBest(file, null).createLineSource(new File(file));
    }

    public static LineSource create(Path file) {
        return getBest(file.toString(), null).createLineSource(file);
    }

    public static LineSource create(InputStream file, String type) {
        return getBest(null, type).createLineSource(file, type);
    }

    public static long countLines(String src) throws IOException {
        return countLines(create(src));
    }

    public static long countLines(LineSource src) throws IOException {
        LinesCounter visitor = new LinesCounter();
        src.read(visitor);
        return visitor.getCount();
    }

    public static void tail(String src, int max, PrintStream out) throws IOException {
        tail(create(src), max, out);
    }

    public static void tail(LineSource src, int max, PrintStream out) throws IOException {
        if (max <= 0) {
            max = 100;
        }
        TailLineVisitor visitor = new TailLineVisitor(max);
        src.read(visitor);
        for (String o : visitor.getLines()) {
            out.println(o);
        }
    }

    public static void head(String src, int max, PrintStream out) throws IOException {
        head(create(src), max, out);
    }

    public static void head(LineSource src, int max, PrintStream out) throws IOException {
        if (max <= 0) {
            max = 100;
        }
        HeadLineVisitor visitor = new HeadLineVisitor(max);
        src.read(visitor);
        for (String o : visitor.getLines()) {
            out.println(o);
        }
    }

    public static boolean contains(String lineSource, LineFilter visitor) throws IOException {
        return contains(create(lineSource), visitor);
    }

    public static boolean contains(LineSource lineSource, LineFilter visitor) throws IOException {
        FoundOccurrenceLineFilter t = new FoundOccurrenceLineFilter(visitor);
        lineSource.read(t);
        return t.found;
    }

    public static long countLines(String lineSource, LineFilter visitor) throws IOException {
        return countLines(create(lineSource), visitor);
    }

    public static long countLines(LineSource lineSource, LineFilter visitor) throws IOException {
        CountOccurrenceLineFilter t = new CountOccurrenceLineFilter(visitor);
        lineSource.read(t);
        return t.count;
    }

    public static void visit(LineSource lineSource, LineVisitor visitor) throws IOException {
        lineSource.read(visitor);
    }

    private static class FoundOccurrenceLineFilter implements LineVisitor {

        boolean found = false;
        LineFilter filter;

        public FoundOccurrenceLineFilter(LineFilter filter) {
            this.filter = filter;
        }

        @Override
        public boolean nextLine(String line) {
            if (filter.accept(line)) {
                found = true;
                return false;
            }
            return true;
        }
    }

    private static class CountOccurrenceLineFilter implements LineVisitor {

        long count = 0;
        LineFilter filter;

        public CountOccurrenceLineFilter(LineFilter filter) {
            this.filter = filter;
        }

        @Override
        public boolean nextLine(String line) {
            if (filter.accept(line)) {
                count++;
            }
            return true;
        }
    }
}
