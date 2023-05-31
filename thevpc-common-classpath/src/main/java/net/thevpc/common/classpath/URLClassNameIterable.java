package net.thevpc.common.classpath;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * @author thevpc
 * %creationtime 12/16/12 1:00 PM
 */
public class URLClassNameIterable implements Iterable<String> {

    protected static final Logger log = Logger.getLogger(URLClassNameIterable.class.getName());
    public URLInfo[] urls;
    public ClassPathFilter configFilter;
    public ClassLoader contextClassLoader;
    private Map<URL, List<URLInfo>> cache = new HashMap<>();

    public static class URLInfo {
        public int index;
        public URL url;
        public URL parent;
        public String subPath;
        public File tempFile;
        public URL tempURL;
        public boolean temp;
        public Supplier<Iterator<ClassPathResource>> cpr;
    }

    public URLClassNameIterable(URL[] urls, ClassPathFilter configFilter) {
        int indexArr = 0;
        for (URL url : urls) {
            URLInfo ii = new URLInfo();
            ii.url = url;
            String z = url.toString();
            int i = z.indexOf(".jar!");
            if (i > 0) {
                String p = z.substring(0, i + 4);
                String k = z.substring(i + 5);
                ii.temp = true;
                try {
                    ii.parent = new URL(p);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                ii.subPath = k;
            } else {
                ii.temp = false;
                ii.tempURL = url;
                ii.cpr = () -> new ClassPathRoot(ii.tempURL).iterator();
            }
            ii.index = indexArr++;
            cache.computeIfAbsent(ii.parent, x -> new ArrayList<>())
                    .add(ii);
        }
        for (Map.Entry<URL, List<URLInfo>> e : cache.entrySet()) {
            if (e.getKey() != null) {
                Map<String, URLInfo> byPath = new HashMap<>();
                for (URLInfo urlInfo : e.getValue()) {
                    byPath.put(urlInfo.subPath, urlInfo);
                }
                URLClassPathRootIterator ii = null;
                try {
                    ii = new URLClassPathRootIterator(e.getKey());
                    while (ii.hasNext()) {
                        ClassPathResource r = ii.next();
                        String path = r.getPath();
                        if(!path.startsWith("/")){
                            path="/"+path;
                        }
                        URLInfo f = byPath.get(path);
                        if (f != null) {
                            File tempFile = File.createTempFile("url-", ".jar");
                            InputStream in = r.open();
                            Files.copy(in, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            f.tempURL = tempFile.toURI().toURL();
                            f.tempFile = tempFile;
                            f.cpr = () -> new ClassPathRoot(tempFile).iterator();
                        }
                    }
                    for (Map.Entry<String, URLInfo> ee : byPath.entrySet()) {
                        if(ee.getValue().tempFile==null){
                            if(ee.getValue().subPath.endsWith(".jar")) {
                                throw new RuntimeException("not found " + ee.getValue().subPath + " in " + ee.getValue().parent);
                            }else{
                                ee.getValue().cpr = new Supplier<Iterator<ClassPathResource>>() {
                                    @Override
                                    public Iterator<ClassPathResource> get() {
                                        try {
                                            String prefix = ee.getValue().subPath;
                                            URL url = ee.getValue().parent;
                                            return new PrefixURLClassPathRootIterator(url, prefix);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                };
                            }
                        }
                    }
                    ii.finalize();
                } catch (RuntimeException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (Throwable ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        List<URLInfo> urls0 = new ArrayList<>();
        for (List<URLInfo> value : cache.values()) {
            urls0.addAll(value);
        }
        urls0.sort(Comparator.comparing(x -> x.index));
//        for (URLInfo urlInfo : urls0) {
//            if(!urlInfo.temp){
//            }else if(urlInfo.tempFile!=null){
//            }else{
//
//            }
//        }
        this.urls = urls0.toArray(new URLInfo[0]);
        this.configFilter = configFilter;
        /**
         * @PortabilityHint(target="C#",name="suppress")
         */
        this.contextClassLoader = Thread.currentThread().getContextClassLoader();
    }

    public Iterator<String> iterator() {
        return new URLClassNameIterableIterator(this);
    }

    String configureClassURL(URL src, String path) throws ClassNotFoundException {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
//        URL url = classLoader.getResource(path);
        if (path.endsWith(".class")) {
            String cls = path.substring(0, path.length() - ".class".length()).replace('/', '.');
            String pck = null;
            int endIndex = cls.lastIndexOf('.');
            if (endIndex > 0) {
                pck = cls.substring(0, endIndex);
            }
            int dollar = cls.indexOf('$');
            boolean anonymousClass = false;
            boolean innerClass = false;
            if (dollar >= 0) {
                innerClass = true;
                //special class
                if (dollar + 1 < cls.length()) {
                    String subName = cls.substring(dollar + 1);
                    if (subName.length() > 0 && Character.isDigit(subName.charAt(0))) {
                        anonymousClass = true;
                    }
                }
            }
            if (configFilter == null || configFilter.acceptClassName(src, cls, anonymousClass)) {
                return cls;
            } else {
//                      System.out.println(path);
//                      System.out.println("\tSOURCE  " + src);
//                      System.out.println("\tSYS URL " + sysURL);
//                      System.out.println("\tAPP URL " + url);
//                      System.out.println("\tPKG   " + (pck==null?"":Package.getPackage(pck)));
//                      System.out.println("\tCLASS " + Class.forName(cls,false,classLoader));
            }
        }
        return null;
    }

    public void close() {
        for (URLInfo url : this.urls) {
            if (url.temp && url.tempFile != null) {
                url.tempFile.delete();
            }
        }
    }

}
