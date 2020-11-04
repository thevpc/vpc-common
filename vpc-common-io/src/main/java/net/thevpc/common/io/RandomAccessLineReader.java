package net.thevpc.common.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessLineReader implements AutoCloseable {

    private final File file0;
    private final RandomAccessFile file;
    private RandomAccessOrientation orientation = RandomAccessOrientation.FORWARD;
    private long lineNumber = -1;

    public RandomAccessLineReader(File file) throws FileNotFoundException {
        file0 = file;
        this.file = new RandomAccessFile(file, "r");
    }

    /**
     * read string at the given line number. orientation will not be altered
     * after this call even though offset may be &lte; getNextLineNumber()
     *
     * @param offset number of the line to read
     * @return read line
     * @throws IOException if i/O error
     */
    public String readLine(long offset) throws IOException {
        long n = getNextLineNumber();
        if (offset == n) {
            return readLine();
        }
        n = getLastLineNumber();
        if (offset < n) {
            RandomAccessOrientation o = getOrientation();
            setOrientation(RandomAccessOrientation.BACKWARD);
            skipLines(n - offset);
            String r = readLine();
            setOrientation(o);
            return r;
        } else if (offset > n) {
            RandomAccessOrientation o = getOrientation();
            setOrientation(RandomAccessOrientation.FORWARD);
            skipLines(offset - n - 1);
            String r = readLine();
            setOrientation(o);
            return r;
        } else {
            throw new IllegalArgumentException("Unsupported");
        }
    }

    public String readLine() throws IOException {
        switch (orientation) {
            case FORWARD:
                return nextForwardLine();
            case BACKWARD:
                return nextBackwardLine();
        }
        throw new IllegalArgumentException(String.valueOf(orientation));
    }

    /**
     * skip x lines in the current orientation
     *
     * @param x number of lines to skip. ignored if &lte; 0
     * @return number of lines read
     * @throws IOException when I/O error
     */
    public long skipLines(long x) throws IOException {
        String r = null;
        long c = 0;
        switch (this.orientation) {
            case FORWARD: {
                while (c < x) {
                    r = nextForwardLine();
                    if (r == null) {
                        break;
                    }
                    c++;
                }
                break;
            }
            case BACKWARD: {
                while (c < x) {
                    r = nextBackwardLine();
                    if (r == null) {
                        break;
                    }
                    c++;
                }
                break;
            }
        }
        return c;
    }

    public String nextForwardLine() throws IOException {
        String m = file.readLine();
        lineNumber++;
        return m;
    }

    public final String nextBackwardLine() throws IOException {
        StringBuilder input = new StringBuilder();
        int c = -1;
        boolean eol = false;

        while (!eol) {
            switch (c = readPrevious()) {
                case -1:
                case '\r':
                    eol = true;
                    break;
                case '\n':
                    eol = true;
                    long cur = file.getFilePointer();
                    if ((readPrevious()) != '\r') {
                        file.seek(cur);
                    }
                    break;
                default:
                    input.append((char) c);
                    break;
            }
        }
        if ((c == -1) && (input.length() == 0)) {
            return null;
        }
        lineNumber--;
        return input.reverse().toString();
    }

    protected final boolean skipPreviousNewLine() throws IOException {
        int c;
        switch (c = readPrevious()) {
            case -1: {
                return false;
            }
            case '\r':
                return true;
            case '\n':
                long cur = file.getFilePointer();
                if ((readPrevious()) != '\r') {
                    file.seek(cur);
                }
                return true;
        }
        return false;
    }

    protected final boolean skipNextNewLine() throws IOException {
        int c;
        switch (c = readNext()) {
            case -1: {
                return false;
            }
            case '\n':
                return true;
            case '\r':
                long cur = file.getFilePointer();
                if ((readNext()) != '\n') {
                    file.seek(cur);
                }
                return true;
        }
        return false;
    }

    public long getNextLineNumber() {
        switch (orientation) {
            case FORWARD:
                return lineNumber + 1;
            case BACKWARD:
                return lineNumber - 1;
        }
        return lineNumber;
    }

    public long getLastLineNumber() {
        return lineNumber;
    }

    protected int readNext() throws IOException {
        return file.read();
    }

    protected int readPrevious() throws IOException {
        long x = file.getFilePointer() - 1;
        if (x < 0) {
            return -1;
        }
        file.seek(x);
        int m = file.read();
        file.seek(x);
        return m;
    }

    @Override
    public void close() throws IOException {
        file.close();
    }

    public void forward() throws IOException {
        setOrientation(RandomAccessOrientation.FORWARD);
    }

    public void backward() throws IOException {
        setOrientation(RandomAccessOrientation.BACKWARD);
    }

    public void switchOrientation() throws IOException {
        switch (orientation) {
            case BACKWARD: {
                setOrientation(RandomAccessOrientation.FORWARD);
                break;
            }
            case FORWARD: {
                setOrientation(RandomAccessOrientation.BACKWARD);
                break;
            }
        }
    }

    public void setOrientation(RandomAccessOrientation o) throws IOException {
        if (o == null) {
            o = orientation;
        }
        if (o != orientation) {
            switch (orientation) {
                case BACKWARD: {
                    lineNumber--;
                    skipNextNewLine();
                    break;
                }
                case FORWARD: {
                    lineNumber++;
                    skipPreviousNewLine();
                    break;
                }
            }
            this.orientation = o;
        }
    }

    public void seekLine(long offset) throws IOException {
        long n = getNextLineNumber();
        if (offset == n) {
            return;
        }
        n = getLastLineNumber();
        if (offset < n) {
            RandomAccessOrientation o = getOrientation();
            setOrientation(RandomAccessOrientation.BACKWARD);
            skipLines(n - offset);
            if (o == RandomAccessOrientation.BACKWARD) {
                readLine();
            }
            setOrientation(o);
        } else if (offset > n) {
            RandomAccessOrientation o = getOrientation();
            setOrientation(RandomAccessOrientation.FORWARD);
            skipLines(offset - n - 1);
            if (o == RandomAccessOrientation.FORWARD) {
                readLine();
            }
            setOrientation(o);
        } else {
            throw new IllegalArgumentException("Unsupported");
        }
    }

    public void seekStart() throws IOException {
        file.seek(0);
        lineNumber = -1;
    }

    public void seekEnd() throws IOException {
        RandomAccessOrientation o = getOrientation();
        setOrientation(RandomAccessOrientation.FORWARD);
        skipLines(Long.MAX_VALUE);
        setOrientation(o);
    }

    public long fileLength() throws IOException {
        return file0.length();
    }

    public long countLines() throws IOException {
        long ll = lineNumber;
        RandomAccessOrientation o = this.orientation;
        this.orientation = RandomAccessOrientation.FORWARD;
        long po = file.getFilePointer();
        file.seek(0);
        long c = skipLines(Long.MAX_VALUE);
        file.seek(po);
        lineNumber = ll;
        this.orientation = o;
        return c;
    }

    public long countReadableLines() throws IOException {
        long ll = lineNumber;
        long po = file.getFilePointer();
        long c = skipLines(Long.MAX_VALUE);
        file.seek(po);
        lineNumber = ll;
        return c;
    }

    public RandomAccessOrientation getOrientation() {
        return orientation;
    }

}
