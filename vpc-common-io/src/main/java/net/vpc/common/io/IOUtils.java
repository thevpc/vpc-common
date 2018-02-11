    /*
     * To change this license header, choose License Headers in Project Properties.
     *
     * and open the template in the editor.
     */
    package net.vpc.common.io;

    import javax.activation.MimetypesFileTypeMap;
    import java.io.*;
    import java.net.MalformedURLException;
    import java.net.URL;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.util.ArrayList;
    import java.util.Iterator;
    import java.util.List;
    import java.util.Properties;

    /**
     * @author taha.bensalah@gmail.com
     */
    public class IOUtils {

        public static String probeContentType(String fileName) {
            MimetypesFileTypeMap m = new MimetypesFileTypeMap();
            String contentType = null;
            try {
                contentType = m.getContentType(fileName);
            } catch (Exception ex) {
                //ignore
            }
            if (contentType == null || "application/octet-stream".equals(contentType)) {
                if (fileName.endsWith(".txt")) {
                    contentType = "text/plain";
                } else if (fileName.endsWith(".html")) {
                    contentType = "text/html";
                } else if (fileName.endsWith(".xml")) {
                    contentType = "text/xml";
                } else if (fileName.toLowerCase().endsWith(".gif")) {
                    contentType = "image/gif";
                } else if (fileName.toLowerCase().endsWith(".css")) {
                    contentType = "text/css";
                } else if (fileName.toLowerCase().endsWith(".js")) {
                    contentType = "text/javascript";
                } else {
                    contentType = "application/octet-stream";
                }
            }
            return contentType;
        }

        public static String probeContentType(File file) {
            String contentType = null;
            try {
                contentType = Files.probeContentType(file.toPath());
            } catch (IOException ex) {
                //ignore
            }
            if (contentType == null || "application/octet-stream".equals(contentType)) {
                return probeContentType(file.getName());
            }
            return contentType;
        }

        public static String probeContentType(URL url) {
            try {
                if (url.getProtocol().equals("file")) {
                    File f;
                    f = new File(url.toURI());
                    return probeContentType(f);
                }
                File temp = File.createTempFile("t", "r");
                Files.copy(url.openStream(), temp.toPath());
                String ct = Files.probeContentType(temp.toPath());
                temp.delete();
                return ct;
            } catch (Exception ex) {
                return null;
            }
        }

        public static byte[] toByteArray(InputStream is) throws IOException {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            return buffer.toByteArray();
        }

        public static Iterable<String> toStringIterable(InputStreamSource is,boolean trackChanges) throws IOException {
            if(!trackChanges) {
                return new InputStreamSourceStringIterable(is);
            }else{

                return new TextMonitor(is);
            }
        }

        public static Iterable<String> filterExceptions(InputStreamSource is,boolean trackChanges) throws IOException {
            if(!trackChanges) {
                return new InputStreamSourceStringIterable(is);
            }else{

                return new TextMonitor(is);
            }
        }

        public static String[] toStringArray(InputStreamSource is) throws IOException {
            try (BufferedReader r = new BufferedReader(new InputStreamReader(is.open()))) {
                List<String> lines = new ArrayList<>();
                String line = null;
                while ((line = r.readLine()) != null) {
                    lines.add(line);
                }
                return lines.toArray(new String[lines.size()]);
            }
        }

        public static Properties toProperties(InputStreamSource is) throws IOException {
            try (InputStream r = is.open()) {
                Properties p = new Properties();
                p.load(r);
                return p;
            }
        }

        public static Properties toPropertiesXML(InputStreamSource is) throws IOException {
            try (InputStream r = is.open()) {
                Properties p = new Properties();
                p.loadFromXML(r);
                return p;
            }
        }

        public static String toString(InputStreamSource is) throws IOException {
            return new String(toCharArray(is));
        }

        public static String toString(InputStream is) throws IOException {
            return new String(toCharArray(is));
        }

        public static char[] toCharArray(InputStreamSource is) throws IOException {
            try (InputStream in = is.open()) {
                return toCharArray(in);
            }
        }

        public static char[] toCharArray(InputStream is) throws IOException {
            return toCharArray(new InputStreamReader(is));
        }

        public static String toString(Reader r) throws IOException {
            return new String(toCharArray(r));
        }

        public static char[] toCharArray(Reader r) throws IOException {
            CharArrayWriter buffer = new CharArrayWriter();

            int nRead;
            char[] data = new char[16384];

            while ((nRead = r.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            return buffer.toCharArray();
        }

        @Deprecated
        public static byte[] read(InputStream in, int count) throws IOException {
            byte[] buffer = new byte[count];
            int i = in.read(buffer);
            byte[] buffer2 = new byte[i];
            System.arraycopy(buffer, 0, buffer2, 0, i);
            return buffer2;
        }

        public static byte[] readByteArray(InputStream in, int count) throws IOException {
            byte[] buffer = new byte[count];
            int i = in.read(buffer);
            byte[] buffer2 = new byte[i];
            System.arraycopy(buffer, 0, buffer2, 0, i);
            return buffer2;
        }

        public static char[] readCharArray(InputStream in, int count) throws IOException {
            return readCharArray(new InputStreamReader(in), count);
        }

        public static char[] readCharArray(Reader in, int count) throws IOException {
            char[] buffer = new char[count];
            int i = in.read(buffer);
            char[] buffer2 = new char[i];
            System.arraycopy(buffer, 0, buffer2, 0, i);
            return buffer2;
        }

        public static OutputStreamSource toOutputStreamSource(final File file) {
            return new OutputStreamSource() {
                @Override
                public OutputStream open() throws IOException {
                    return new FileOutputStream(file);
                }

                @Override
                public Object getSource() throws IOException {
                    return file;
                }

            };
        }

        public static InputStreamSource toInputStreamSource(byte[] bytes) {
            return new ByteArrayInputStreamSource(bytes);
        }

        public static InputStreamSource toInputStreamSource(String string) {
            return new StringInputStreamSource(string);
        }

        public static InputStreamSource toInputStreamSource(File file) {
            return new FileInputStreamSource(file);
        }

        public static InputStreamSource toInputStreamSource(URL url) {
            return new URLInputStreamSource(url);
        }

        public static InputStreamSource toInputStreamSource(InputStream stream) {
            return new SingleAccessInputStreamSource(stream);
        }

        public static boolean isURL(String url) {
            try {
                new URL(url);
                return true;
            } catch (MalformedURLException ex) {
                return false;
            }
        }

        public static File toFileLenient(String path) {
            if (path == null || path.length() == 0) {
                return null;
            }
            if (path.startsWith("/") || path.startsWith("\\")) {
                return new File(path);
            }
            if (path.toLowerCase().startsWith("file:")) {
                Path pa;
                try {
                    pa = Paths.get(new URL(path).toURI());
                } catch (Exception ex) {
                    return new File(path.substring("file:".length()));
                }
                return pa.toFile();
            }
            if (path.indexOf(':') == 1) {
                for (int i = 0; i < 26; i++) {
                    if (path.toLowerCase().startsWith(('a') + ":")) {
                        //windows drive letter
                        return new File(path);
                    }
                }
            }
            return null;
        }

        public static String getUrlProtocol(String path) {
            if (path == null) {
                return null;
            }
            int i = path.indexOf("://");
            if (i > 0) {
                String protocol = path.substring(0, i).trim();
                if (protocol.matches("[a-zA-Z0-9_-]+")) {
                    return protocol;
                }
            }
            return null;
        }

        public static String getUrlFile(String path) {
            if (path == null) {
                return null;
            }
            int i = path.indexOf("://");
            if (i > 0) {
                return path.substring(i + 3);
            }
            return path;
        }

        public static void createParents(File f) {
            if (f != null) {
                File pf = f.getParentFile();
                if (pf != null) {
                    pf.mkdirs();
                }
            }
        }

        private static class InputStreamSourceStringIterable implements Iterable<String> {
            private final InputStreamSource is;

            public InputStreamSourceStringIterable(InputStreamSource is) {
                this.is = is;
            }

            @Override
            public Iterator<String> iterator() {
                try {
                    return new InputStreamStringIterator(is.open());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
