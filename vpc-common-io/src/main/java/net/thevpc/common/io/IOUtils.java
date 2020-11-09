/**
 * ====================================================================
 * vpc-common-io : common reusable library for
 * input/output
 * <br>
 * is a new Open Source Package Manager to help install packages and libraries
 * for runtime execution. Nuts is the ultimate companion for maven (and other
 * build managers) as it helps installing all package dependencies at runtime.
 * Nuts is not tied to java and is a good choice to share shell scripts and
 * other 'things' . Its based on an extensible architecture to help supporting a
 * large range of sub managers / repositories.
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/
package net.thevpc.common.io;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.*;

/**
 * @author taha.bensalah@gmail.com
 */
public class IOUtils {

    private static final Logger log = Logger.getLogger(IOUtils.class.getName());
    public static final OutputStream NULL_OUTPUT_STREAM = NullOutputStream.INSTANCE;
    public static final PrintStream NULL_PRINT_STREAM = NullPrintStream.INSTANCE;

    /**
     * taille par defaut du buffer de transfer
     */
    public static final int DEFAULT_BUFFER_SIZE = 1024;

    public static Iterable<String> toStringIterable(InputStreamSource is, boolean trackChanges) {
        if (!trackChanges) {
            return new InputStreamSourceStringIterable(is);
        } else {

            return new TextMonitor(is);
        }
    }

    public static Iterable<String> filterExceptions(InputStreamSource is, boolean trackChanges) {
        if (!trackChanges) {
            return new InputStreamSourceStringIterable(is);
        } else {

            return new TextMonitor(is);
        }
    }

    public static String[] loadStringArray(InputStreamSource is) throws IOException {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(is.open()))) {
            List<String> lines = new ArrayList<>();
            String line = null;
            while ((line = r.readLine()) != null) {
                lines.add(line);
            }
            return lines.toArray(new String[lines.size()]);
        }
    }

    public static Properties loadProperties(InputStreamSource is) throws IOException {
        try (InputStream r = is.open()) {
            Properties p = new Properties();
            p.load(r);
            return p;
        }
    }

    public static Properties loadProperties(Path is) throws IOException {
        try (Reader r = Files.newBufferedReader(is)) {
            Properties p = new Properties();
            p.load(r);
            return p;
        }
    }

    public static Properties loadXMLProperties(InputStreamSource is) throws IOException {
        try (InputStream r = is.open()) {
            Properties p = new Properties();
            p.loadFromXML(r);
            return p;
        }
    }

    public static String loadString(File is) throws IOException {
        return loadString(toInputStreamSource(is));
    }

    public static String loadString(InputStreamSource is) throws IOException {
        return new String(loadCharArray(is));
    }

    public static char[] loadCharArray(InputStreamSource is) throws IOException {
        try (InputStream in = is.open()) {
            return loadCharArray(in);
        }
    }

    public static char[] loadCharArray(InputStream is) throws IOException {
        return loadCharArray(new InputStreamReader(is));
    }

    public static String loadString(Reader r) throws IOException {
        return new String(loadCharArray(r));
    }

    public static char[] loadCharArray(Reader r) throws IOException {
        CharArrayWriter buffer = new CharArrayWriter();

        int nRead;
        char[] data = new char[16384];

        while ((nRead = r.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toCharArray();
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

    public static InputStreamSource toInputStreamSource(byte[] bytes, String name) {
        return new ByteArrayInputStreamSource(bytes, name, bytes);
    }

    public static InputStreamSource toInputStreamSource(Path path) {
        return new PathInputStreamSource(path);
    }

    public static InputStreamSource toInputStreamSource(byte[] bytes) {
        return new ByteArrayInputStreamSource(bytes, null, bytes);
    }

    public static InputStreamSource toInputStreamSource(String string) {
        return new StringInputStreamSource(string, null);
    }

    public static InputStreamSource toInputStreamSource(File file) {
        return new FileInputStreamSource(file);
    }

    public static InputStreamSource toInputStreamSource(URL url) {
        return new URLInputStreamSource(url);
    }

    public static InputStreamSource toInputStreamSource(InputStream stream) {
        return new SingleAccessInputStreamSource(stream, null);
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
            } catch (IOException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    }

    public static Object loadObject2(String physicalName) {
        physicalName = FileUtils.expandPath(physicalName);
        try {
            File f = new File(physicalName);
            if (f.isFile()) {
                return loadObject(physicalName);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Object loadObject(String physicalName) throws IOException, ClassNotFoundException {
        physicalName = FileUtils.expandPath(physicalName);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(physicalName));
            return ois.readObject();
        } finally {
            if (ois != null) {
                ois.close();
            }
        }
    }

    public static Object loadZippedObject(String physicalName) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new GZIPInputStream(new FileInputStream(FileUtils.expandPath(physicalName))));
            return ois.readObject();
        } finally {
            if (ois != null) {
                ois.close();
            }
        }
    }

    public static String loadString(URL url) throws IOException {
        return new String(loadByteArray(url));
    }

    public static String loadString(InputStream r) throws IOException {
        return new String(loadByteArray(r));
    }

    public static byte[] loadByteArray(URL url) throws IOException {
        InputStream r = null;
        try {
            return loadByteArray(r = url.openStream());
        } finally {
            if (r != null) {
                r.close();
            }
        }
    }

    public static void saveXMLProperties(Properties p, String comments, File file) throws IOException {
        FileUtils.createParents(file);
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            p.storeToXML(os, comments);
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    public static void saveString(String message, File file) throws IOException {
        FileUtils.createParents(file);

        try (PrintWriter w = new PrintWriter(file)) {
            w.print(message);
        }

    }

    public static void saveProperties(Properties p, String comments, File file) throws IOException {
        FileUtils.createParents(file);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            p.store(os, comments);
        } finally {
            if (os != null) {
                os.close();
            }
        }

    }

    public static void saveProperties(Properties p, String comments, Path file) throws IOException {
        Files.createDirectories(file.getParent());
        Writer os = null;

        try {
            os = Files.newBufferedWriter(file);
            p.store(os, comments);
        } finally {
            if (os != null) {
                os.close();
            }
        }

    }

    public static Properties loadXMLProperties(File file) throws IOException {
        Properties p = new Properties();
        FileInputStream is = null;

        try {
            is = new FileInputStream(file);
            p.loadFromXML(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return p;
    }

    public static Properties loadProperties(File file) throws IOException {
        return loadProperties(file, false);
    }

    public static Properties loadProperties(File file, boolean silent) throws IOException {
        Properties props = new Properties();
        InputStream inputStream = null;
        try {
            try {
                if (file != null && file.isFile()) {
                    inputStream = new FileInputStream(file);
                    props.load(inputStream);
                }
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (IOException e) {
            if (!silent) {
                throw e;
            }
        } catch (Exception e) {
            if (!silent) {
                throw new IOException(e.getMessage());
            }
        }
        return props;
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(InputStream in, OutputStream out) throws IOException {
        copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int len;

        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }

    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(File in, File out) throws IOException {
        if (in.isDirectory()) {
            out.mkdirs();
            File[] children = in.listFiles();
            if (children != null) {
                for (File child : children) {
                    copy(child, new File(out, child.getName()));
                }
            }
        } else {
            FileUtils.createParents(out);
            Files.copy(in.toPath(), out.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        }
//        copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(File in, File out, int bufferSize) throws IOException {
        FileUtils.createParents(out);
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(in);
            fos = new FileOutputStream(out);
            copy(fis, fos, bufferSize);
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }

    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(File in, OutputStream out) throws IOException {
        copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(File in, OutputStream out, int bufferSize) throws IOException {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(in);
            copy(fis, out, bufferSize);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(InputStream in, File out) throws IOException {
        copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    public static void copy(URL url, File out) throws IOException {
        InputStream in = null;

        try {
            in = url.openStream();
            IOUtils.copy(in, out);
        } finally {
            if (in != null) {
                in.close();
            }
        }

    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(InputStream in, File out, int bufferSize) throws IOException {
        FileUtils.createParents(out);
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(out);
            copy(in, fos, bufferSize);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }

    }

    public static void saveObject2(String physicalName, Object object) {
        try {
            saveObject(physicalName, object);
        } catch (Exception e) {
            e.printStackTrace();
            //ignore
        }
    }

    public static void saveObject(String physicalName, Object object) throws IOException {
        physicalName = FileUtils.expandPath(physicalName);

        ObjectOutputStream oos = null;
        try {
            File f = new File(physicalName);
            if (f.getParentFile() != null) {
                f.getParentFile().mkdirs();
            }
            oos = new ObjectOutputStream(new FileOutputStream(physicalName));
            oos.writeObject(object);
            oos.close();
        } finally {
            if (oos != null) {
                oos.close();
            }
        }

    }

    /**
     * writeString writeToFile
     *
     * @param string
     * @param physicalName
     * @throws IOException
     */
    public static void saveString(String string, String physicalName) throws IOException {
        physicalName = FileUtils.expandPath(physicalName);

        PrintStream oos = null;
        try {
            File f = new File(physicalName);
            if (f.getParentFile() != null) {
                f.getParentFile().mkdirs();
            }
            oos = new PrintStream(new File(physicalName));
            if (string == null) {
                string = "";
            }
            oos.print(string);
            oos.close();
        } finally {
            if (oos != null) {
                oos.close();
            }
        }

    }

    public static String computeCRC(File file) throws IOException {
        FileOutputStream f = null;
        try {
            f = new FileOutputStream(file);
            return computeCRC(file);
        } finally {
            if (f != null) {
                f.close();
            }
        }
    }

    public static String computeCRC(byte[] bytes) throws IOException {
        InputStream f = null;
        try {
            f = new ByteArrayInputStream(bytes);
            return computeCRC(f);
        } finally {
            if (f != null) {
                f.close();
            }
        }
    }

    public static String computeCRC(InputStream is) throws IOException {
        CheckedInputStream cis = null;
        try {
            cis = new CheckedInputStream(is, new CRC32());
            byte[] buf = new byte[1024];
            int c;
            while ((c = cis.read(buf)) >= 0) {
                //
            }
            return Long.toString(cis.getChecksum().getValue(), 32).toUpperCase();
        } finally {
            if (cis != null) {
                cis.close();
            }
        }
    }

    public static String computeCRC(Reader reader) throws IOException {
        try {
            char[] buf = new char[1024];
            int c;
            CRC32 crc = new CRC32();
            while ((c = reader.read(buf)) > 0) {
                crc.update(new String(buf, 0, c).getBytes());
                //
            }
            return Long.toString(crc.getValue(), 32).toUpperCase();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public static InputStreamSource toInputStreamSource(Object anyObject, String variant, String optionalName, File cwd) throws IOException {
        if (anyObject instanceof URL) {
            return toInputStreamSource((URL) anyObject);
        }
        if (anyObject instanceof File) {
            return toInputStreamSource((File) anyObject);
        }
        if (anyObject instanceof byte[]) {
            return toInputStreamSource((byte[]) anyObject, optionalName);
        }
        if (anyObject instanceof InputStream) {
            return toInputStreamSource((InputStream) anyObject, optionalName);
        }
        if (anyObject instanceof String) {
            return toInputStreamSource((String) anyObject, variant, optionalName, cwd);
        }
        throw new RuntimeException("Unexpected stream source");
    }

    public static InputStreamSource toInputStreamSource(String path, String variant, String name, File cwd) throws IOException {
        if ("path".equals(variant)) {
            if (path.contains("://")) {
                return toInputStreamSource(new URL(path));
            }
            return toInputStreamSource(new File(path));
        }
        if ("url".equals(variant)) {
            return toInputStreamSource(new URL(path));
        }
        if ("file".equals(variant)) {
            return toInputStreamSource(new URL(path));
        }
        if (variant == null || variant.isEmpty()) {
            if (path.contains("://")) {
                return toInputStreamSource(new URL(path));
            }
            return toInputStreamSource(new URL(path));
        }
        throw new IllegalArgumentException("Unsupported variant " + variant);
    }

    public static InputStreamSource toInputStreamSource(InputStream inputStream, String name) throws IOException {
        byte[] bytes = loadByteArray(inputStream, true);
        return new ByteArrayInputStreamSource(bytes, name, inputStream);
    }

//    public static InputStreamSource toInputStreamSource(File filecwd) {
//        return new FileInputStreamSource(filecwd);
//    }
    public static byte[] loadByteArray(File stream) throws IOException {
        return loadByteArray(new FileInputStream(stream), true);
    }

    public static byte[] loadByteArray(InputStream stream, int maxSize, boolean close) throws IOException {
        try {
            if (maxSize > 0) {
                ByteArrayOutputStream to = new ByteArrayOutputStream();
                byte[] bytes = new byte[Math.max(maxSize, 10240)];
                int count;
                int all = 0;
                while ((count = stream.read(bytes)) > 0) {
                    if (all + count < maxSize) {
                        to.write(bytes, 0, count);
                        all += count;
                    } else {
                        int count2 = maxSize - all;
                        to.write(bytes, 0, count2);
                        all += count2;
                        break;
                    }
                }
                return to.toByteArray();
            } else {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                copy(stream, os, close, true);
                return os.toByteArray();
            }
        } finally {
            if (close) {
                stream.close();
            }
        }
    }

    public static void copy(URL from, File to, boolean mkdirs) throws IOException {
        ReadableByteChannel readableByteChannel = Channels.newChannel(from.openStream());
        if (mkdirs) {
            FileUtils.createParents(to);
        }
        FileOutputStream fileOutputStream = new FileOutputStream(to);
        FileChannel fileChannel = fileOutputStream.getChannel();
        fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        //copy(from.openStream(), to, mkdirs, true);
    }

    public static void copy(File from, OutputStream to, boolean closeOutput) throws IOException {
        copy(new FileInputStream(from), to, true, closeOutput);
    }

    public static void copy(Reader from, OutputStream to, boolean closeInput, boolean closeOutput) throws IOException {
        char[] bytes = new char[10240];
        int count;
        try {
            try {
                to.flush();
                OutputStreamWriter ps = new OutputStreamWriter(to);
                while ((count = from.read(bytes)) > 0) {
                    ps.write(bytes, 0, count);
                }
                ps.flush();
            } finally {
                if (closeInput) {
                    from.close();
                }
            }
        } finally {
            if (closeOutput) {
                to.close();
            }
        }
    }

    public static void copy(String what, File to, boolean mkdirs) throws IOException {
        if (what == null) {
            what = "";
        }
        copy(new ByteArrayInputStream(what.getBytes()), to, mkdirs, true);
    }

    public static void copy(File from, File to, boolean mkdirs) throws IOException {
        if (from.equals(to)) {
            return;
        }
        copy(new FileInputStream(from), to, mkdirs, true);
    }

    public static void copy(InputStream from, File to, boolean mkdirs, boolean closeInput) throws IOException {
        copy(from, to, mkdirs, closeInput, null);
    }

    public static void copy(InputStream from, File to, boolean mkdirs, boolean closeInput, FileValidator validator) throws IOException {
        try {
            File parentFile = to.getParentFile();
            if (mkdirs && parentFile != null) {
                parentFile.mkdirs();
            }
            File temp = new File(to.getPath() + "~");
            try {
                Files.copy(from, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
                if (validator != null) {
                    try {
                        validator.validateFile(temp);
                    } catch (Exception ex) {
                        if (ex instanceof FileValidationException) {
                            throw ex;
                        }
                        throw new FileValidationException("Validate file " + temp.getPath() + " failed", ex);
                    }
                }
                Files.move(temp.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } finally {
                temp.delete();
            }
        } finally {
            if (closeInput) {
                from.close();
            }
        }
    }

    public static long copy(InputStream from, OutputStream to, boolean closeInput, boolean closeOutput) throws IOException {
        byte[] bytes = new byte[1024];//
        int count;
        long all = 0;
        try {
            try {
                while ((count = from.read(bytes)) > 0) {
                    to.write(bytes, 0, count);
                    all += count;
                }
                return all;
            } finally {
                if (closeInput) {
                    from.close();
                }
            }
        } finally {
            if (closeOutput) {
                to.close();
            }
        }
    }

    public static long copy(InputStream from, OutputStream to, boolean closeInput, boolean closeOutput, StopMonitor monitor) throws IOException {
        byte[] bytes = new byte[10240];
        int count;
        long all = 0;
        try {
            try {
                while (true) {
                    if (monitor.shouldStop()) {
                        break;
                    }
                    if (from.available() > 0) {
                        count = from.read(bytes);
                        if (count > 0) {
                            to.write(bytes, 0, count);
                            all += count;
                        }
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            //
                        }
                        if (from.available() > 0) {
                            count = from.read(bytes);
                            if (count > 0) {
                                to.write(bytes, 0, count);
                                all += count;
                            }
                        }
                    }
                }
                return all;
            } finally {
                if (closeInput) {
                    from.close();
                }
            }
        } finally {
            if (closeOutput) {
                to.close();
            }
        }
    }

    public static long copy(NonBlockingInputStream from, OutputStream to, boolean closeInput, boolean closeOutput, StopMonitor monitor) throws IOException {
        byte[] bytes = new byte[10240];
        int count;
        long all = 0;
        try {
            try {
                while (true) {
                    if (monitor.shouldStop()) {
                        break;
                    }
                    if (from.hasMoreBytes()) {
                        count = from.readNonBlocking(bytes, 500);
                        all += count;
                        to.write(bytes, 0, count);
//                        System.out.println("push "+count);
                    } else {
                        break;
                    }
                }
                return all;
            } finally {
                if (closeInput) {
                    from.close();
                }
            }
        } finally {
            if (closeOutput) {
                to.close();
            }
        }
    }

    public static String loadString(InputStream stream, boolean close) throws IOException {
        return new String(loadByteArray(stream, close));
    }

    public static byte[] loadByteArray(InputStream stream, boolean close) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        copy(stream, os, close, true);
        return os.toByteArray();
    }

    //TODO
    public static Properties loadProperties(URL url) throws IOException {
        Properties props = new Properties();
        InputStream inputStream = null;
        try {
            try {
                if (url != null) {
                    inputStream = url.openStream();
                    props.load(inputStream);
                }
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return props;
    }

    public static boolean isValidInputStreamSource(Class type) {
        return URL.class.isAssignableFrom(type)
                || File.class.isAssignableFrom(type) || byte[].class.isAssignableFrom(type)
                || InputStream.class.isAssignableFrom(type)
                || String.class.isAssignableFrom(type);
    }

    public static byte[] loadByteArray(File stream, int maxSize) throws IOException {
        return loadByteArray(new FileInputStream(stream), maxSize, true);
    }

    public static PipeThread pipe(String name, final NonBlockingInputStream in, final OutputStream out) {
        PipeThread p = new PipeThread(name, in, out);
        p.start();
        return p;
    }

    public static InputStream monitor(URL from, InputStreamMonitor monitor) throws IOException {
        return monitor(from.openStream(), from, URLUtils.getURLName(from), URLUtils.getURLHeader(from).getContentLength(), monitor);
    }

    public static InputStream monitor(InputStream from, Object source, String sourceName, long length, InputStreamMonitor monitor) {
        return new MonitoredInputStream(from, source, sourceName, length, monitor);
    }

    public static Properties loadURLProperties(String url) {
        try {
            if (url != null) {
                return loadURLProperties(new URL(url));
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new Properties();
    }

    public static Properties loadURLProperties(URL url) {
        Properties props = new Properties();
        InputStream inputStream = null;
        try {
            try {
                if (url != null) {
                    inputStream = url.openStream();
                    props.load(inputStream);
                }
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return props;
    }

//    public static Properties loadFileProperties(String file) {
//        try {
//            if (file != null) {
//                return loadProperties(new File(file));
//            }
//        } catch (Exception e) {
//            //e.printStackTrace();
//        }
//        return new Properties();
//    }
    public static DeleteResult delete(File file) throws IOException {
        return delete(file.toPath());
    }

    public static DeleteResult delete(Path file) throws IOException {
        if (!Files.exists(file)) {
            return new DeleteResult(0, 0, 0);
        }
        if (Files.isRegularFile(file)) {
            try {
                Files.delete(file);
                return new DeleteResult(1, 0, 0);
            } catch (IOException e) {
                return new DeleteResult(0, 0, 1);
            }
        }
        final int[] deleted = new int[]{0, 0, 0};

        Files.walkFileTree(file, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                try {
                    Files.delete(file);
                    log.log(Level.FINEST, "Delete file " + file);
                    deleted[0]++;
                } catch (IOException e) {
                    log.log(Level.FINEST, "Delete file Failed : " + file);
                    deleted[2]++;
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                try {
                    Files.delete(dir);
                    log.log(Level.FINEST, "Delete folder " + dir);
                    deleted[1]++;
                } catch (IOException e) {
                    log.log(Level.FINEST, "Delete folder Failed : " + dir);
                    deleted[2]++;
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return new DeleteResult(
                deleted[0],
                deleted[1],
                deleted[2]
        );
    }

    public static byte[] loadByteArray(InputStream r) throws IOException {
        ByteArrayOutputStream out = null;

        try {
            out = new ByteArrayOutputStream();
            copy(r, out);
            out.flush();
            return out.toByteArray();
        } finally {
            if (out != null) {
                out.close();
            }
        }

    }

    public static PathInfo getPathInfo(String path) {
        return PathInfo.create(path);
    }

    public static PathInfo getPathInfo(File path) {
        return PathInfo.create(path);
    }

    public static PathInfo getPathInfo(URL path) {
        return PathInfo.create(path);
    }

    public static String concatPath(char separator, String... paths) {
        StringBuilder sb = new StringBuilder();
        for (String path : paths) {
            if (sb.length() > 0 && sb.charAt(sb.length() - 1) != separator) {
                sb.append(separator);
            }
            sb.append(path);
        }
        return sb.toString();
    }
//    public static byte[] toByteArray(InputStream is) throws UncheckedIOException {
//        try {
//            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//
//            int nRead;
//            byte[] data = new byte[16384];
//
//            while ((nRead = is.read(data, 0, data.length)) != -1) {
//                buffer.write(data, 0, nRead);
//            }
//
//            buffer.flush();
//
//            return buffer.toByteArray();
//        } catch (IOException e) {
//            throw new UncheckedIOException(e);
//        }
//    }
}
