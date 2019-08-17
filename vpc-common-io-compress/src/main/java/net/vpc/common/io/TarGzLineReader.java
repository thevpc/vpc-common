package net.vpc.common.io;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.*;
import java.util.Iterator;

public class TarGzLineReader implements Iterable<String>, LineSource {

    private File file;

    public TarGzLineReader(File file) {
        this.file = file;
    }

    public void read(LineVisitor visitor) throws IOException {
        TarArchiveInputStream tarInput = new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(file)));
        TarArchiveEntry currentEntry = tarInput.getNextTarEntry();
        BufferedReader br = null;
        while (currentEntry != null) {
            br = new BufferedReader(new InputStreamReader(tarInput)); // Read directly from tarInput
//            System.out.println("For File = " + currentEntry.getName());
            String line;
            while ((line = br.readLine()) != null) {
                if (!visitor.nextLine(line)) {
                    break;
                }
            }
            currentEntry = tarInput.getNextTarEntry(); // You forgot to iterate to the next file
            break;//only one file!!
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new TarGzLineReaderIterator();
    }

    private class TarGzLineReaderIterator implements Iterator<String>, Closeable {

        TarArchiveInputStream tarInput;
        TarArchiveEntry currentEntry;
        BufferedReader br = null;
        int index = 0;
        String line;

        {
            try {
                tarInput = new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(file)));
                currentEntry = tarInput.getNextTarEntry();
                int i = 0;
                while (currentEntry != null) {
                    if (index == i) {
                        br = new BufferedReader(new InputStreamReader(tarInput)); // Read directly from tarInput
//            System.out.println("For File = " + currentEntry.getName());
                        break;
                    }
                    currentEntry = tarInput.getNextTarEntry(); // You forgot to iterate to the next file
                    i++;

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        @Override
        public void remove() {

        }

        @Override
        public boolean hasNext() {
            try {
                boolean b = (line = br.readLine()) != null;
                if (!b) {
                    close();
                }
                return b;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override
        public String next() {
            return line;
        }

        @Override
        public void close() throws IOException {
            if (br != null) {
                br.close();
            }
        }
    }
}
