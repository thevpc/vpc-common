package net.vpc.common.io;

import java.io.*;
import java.nio.file.Path;

public class DefaultLineSourceFactory implements LineSourceFactory {

    @Override
    public int getSupport(String fileName, String mimetype) {
        return 0;
    }

    @Override
    public LineSource createLineSource(InputStream stream, String mimeType) {
        return new MyLineSource(stream);
    }

    @Override
    public LineSource createLineSource(Path file) {
        return createLineSource(file.toFile());
    }

    @Override
    public LineSource createLineSource(File file) {
        return new MyLineSource(file);
    }

    private static class MyLineSource implements LineSource {

        InputStream is;
        File file;

        public MyLineSource(InputStream is) {
            this.is = is;
        }

        public MyLineSource(File file) {
            this.file = file;
        }

        @Override
        public void read(LineVisitor visitor) throws IOException {
            if (file != null) {
                BufferedReader r = null;
                try {
                    r = new BufferedReader(new FileReader(file));
                    String line = null;
                    while ((line = r.readLine()) != null) {
                        visitor.nextLine(line);
                    }
                } finally {
                    if (r != null) {
                        r.close();
                    }
                }
            } else if (is != null) {
                BufferedReader r = null;
                try {
                    r = new BufferedReader(new InputStreamReader(is));
                    String line = null;
                    while ((line = r.readLine()) != null) {
                        visitor.nextLine(line);
                    }
                } finally {
                    is = null;
                    if (r != null) {
                        r.close();
                    }
                }
            } else {
                throw new IllegalArgumentException("Missing source");
            }
        }
    }
}
