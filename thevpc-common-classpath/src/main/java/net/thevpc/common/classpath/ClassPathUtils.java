/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.thevpc.common.classpath;

import java.io.File;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author taha.bensalah@gmail.com
 */
public class ClassPathUtils {

    private static final Logger log = Logger.getLogger(ClassPathUtils.class.getName());

//    public static List<URL> getClasspathRoots(String resource) throws IOException {
//        List<URL> urls = new ArrayList<URL>();
//        Enumeration<URL> upaXmls = Thread.currentThread().getContextClassLoader().getResources("META-INF/upa.xml");
//        while (upaXmls.hasMoreElements()) {
//            URL url = upaXmls.nextElement();
//            urls.add(getClasspathRoot(url, "META-INF/upa.xml"));
//        }
//        return urls;
//    }

    public static DefaultClassPathFilter createDefaultClassPathFilter() {
        return new DefaultClassPathFilter();
    }

    private static ClassPathFilter classFilterToClassPathFilter(final ClassFilter it) {
        if (it == null) {
            return null;
        }
        return new ClassPathFilter() {
            @Override
            public boolean acceptLibrary(URL url) {
                return true;
            }

            @Override
            public boolean acceptClassName(URL url, String className, boolean anonymous) {
                return true;
            }

            @Override
            public boolean acceptClass(URL url, String className, boolean anonymous, Class type) {
                return it.accept(type);
            }
        };
    }

    private static ClassPathFilter classNameFilterToClassPathFilter(final ClassNameFilter it) {
        if (it == null) {
            return null;
        }
        return new ClassPathFilter() {
            @Override
            public boolean acceptLibrary(URL url) {
                return true;
            }

            @Override
            public boolean acceptClassName(URL url, String className, boolean anonymous) {
                return it.accept(className);
            }

            @Override
            public boolean acceptClass(URL url, String className, boolean anonymous, Class type) {
                return acceptClassName(url, className, anonymous);
            }
        };
    }

    private static <T> List<T> iterableToList(Iterable<T> it) {
        ArrayList<T> list = new ArrayList<>();
        for (T t : it) {
            list.add(t);
        }
        return list;
    }

    public static List<Class> resolveContextClassesList(boolean includeSDK) {
        return iterableToList(resolveContextClasses((ClassPathFilter) null, includeSDK));
    }

    public static List<String> resolveContextClassNamesList(boolean includeSDK) {
        return iterableToList(resolveContextClassNames((ClassPathFilter) null, includeSDK));
    }

    public static List<Class> resolveContextClassesList(ClassNameFilter filter, boolean includeSDK) {
        return iterableToList(resolveContextClasses(filter, includeSDK));
    }

    public static List<Class> resolveContextClassesList(ClassFilter filter, boolean includeSDK) {
        return iterableToList(resolveContextClasses(filter, includeSDK));
    }

    public static List<Class> resolveContextClassesList(ClassPathFilter filter, boolean includeSDK) {
        return iterableToList(resolveContextClasses(filter, includeSDK));
    }

    public static List<String> resolveContextClassNamesList(ClassPathFilter filter, boolean includeSDK) {
        return iterableToList(resolveContextClassNames(filter, includeSDK));
    }

    public static Iterable<Class> resolveContextClasses(boolean includeSDK) {
        return resolveClasses(
                resolveContextLibraries(includeSDK),
                (ClassPathFilter) null,
                Thread.currentThread().getContextClassLoader()

        );
    }

    public static Iterable<Class> resolveContextClasses(ClassFilter filter, boolean includeSDK) {
        return resolveClasses(
                resolveContextLibraries(includeSDK),
                filter,
                Thread.currentThread().getContextClassLoader()

        );
    }

    public static Iterable<Class> resolveContextClasses(ClassPathFilter filter, boolean includeSDK) {
        return resolveClasses(
                resolveContextLibraries(includeSDK),
                filter,
                Thread.currentThread().getContextClassLoader()

        );
    }

    public static Iterable<Class> resolveContextClasses(ClassNameFilter filter, boolean includeSDK) {
        return resolveClasses(
                resolveContextLibraries(includeSDK),
                classNameFilterToClassPathFilter(filter),
                Thread.currentThread().getContextClassLoader()

        );
    }

    public static Iterable<Class> resolveClasses(URL[] urls, ClassLoader classLoader) {
        return resolveClasses(urls, (ClassPathFilter) null, classLoader);
    }

    public static Iterable<Class> resolveClasses(URL[] urls) {
        return resolveClasses(urls, (ClassPathFilter) null, null);
    }

    public static Iterable<Class> resolveClasses(URL[] urls, ClassFilter filter, ClassLoader classLoader) {
        return resolveClasses(urls, classFilterToClassPathFilter(filter), classLoader);
    }

    public static Iterable<Class> resolveClasses(URL[] urls, ClassNameFilter filter, ClassLoader classLoader) {
        return resolveClasses(urls, classNameFilterToClassPathFilter(filter), classLoader);
    }

    public static Iterable<Class> resolveClasses(URL[] urls, ClassPathFilter filter, ClassLoader classLoader) {
        return new URLClassIterable(
                urls,
                filter,
                classLoader
        );
    }


    public static Iterable<Class> resolveClasses(URL[] urls, ClassFilter filter) {
        return resolveClasses(urls, classFilterToClassPathFilter(filter), null);
    }

    public static Iterable<Class> resolveClasses(URL[] urls, ClassNameFilter filter) {
        return resolveClasses(urls, classNameFilterToClassPathFilter(filter), null);
    }

    public static Iterable<Class> resolveClasses(URL[] urls, ClassPathFilter filter) {
        return new URLClassIterable(
                urls,
                filter,
                null
        );
    }

    public static Iterable<String> resolveClassNames(URL[] urls, ClassNameFilter filter) {
        return new URLClassNameIterable(
                urls,
                classNameFilterToClassPathFilter(filter)

        );
    }

    private static URL[] toURLs(String[] paths) {
        List<URL> u = new ArrayList<>();
        try {
            for (String path : paths) {
                if (path.startsWith("file://") || path.startsWith("http://")) {
                    u.add(new URL(path));
                }else{
                    u.add(new File(path).toURI().toURL());
                }
            }
            return u.toArray(new URL[0]);
        } catch (MalformedURLException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Iterable<String> resolveClassNames(String[] paths, ClassPathFilter filter) {
        return resolveClassNames(toURLs(paths),filter);
    }

    public static Iterable<String> resolveClassNames(String[] paths, ClassNameFilter filter) {
        return resolveClassNames(toURLs(paths),filter);
    }
    public static Iterable<String> resolveClassNames(String[] paths) {
        return resolveClassNames(toURLs(paths),(ClassPathFilter) null);
    }

    public static Iterable<String> resolveClassNames(URL[] urls) {
        return resolveClassNames(urls,(ClassPathFilter) null);
    }

    public static Iterable<String> resolveClassNames(URL[] urls, ClassPathFilter filter) {
        return new URLClassNameIterable(
                urls,
                filter

        );
    }

    public static Iterable<String> resolveSDKClassNames() {
        return new URLClassNameIterable(
                resolveContextLibraries(true,false),
                null
        );
    }

    public static Iterable<String> resolveContextClassNames(boolean includeSDK) {
        return resolveContextClassNames((ClassPathFilter) null, includeSDK);
    }

    public static Iterable<String> resolveContextClassNames(ClassNameFilter filter, boolean includeSDK) {
        return new URLClassNameIterable(
                resolveContextLibraries(includeSDK),
                classNameFilterToClassPathFilter(filter)

        );
    }

    public static Iterable<String> resolveContextClassNames(ClassPathFilter filter, boolean includeSDK) {
        return new URLClassNameIterable(
                resolveContextLibraries(includeSDK),
                filter

        );
    }

    public static Iterable<ClassPathResource> resolveResources(URL[] libraries, ClassPathResourceFilter filter) {

        return new URLResourceIterable(
                libraries == null ? new URL[0] : libraries,
                filter
        );
    }

    public static Iterable<ClassPathResource> resolveContextResources(ClassPathResourceFilter filter, boolean includeSDK) {
        return new URLResourceIterable(
                resolveContextLibraries(includeSDK),
                filter
        );
    }

    public static URL[] resolveContextLibraries() {
        return resolveContextLibraries(false);
    }

    public static URL[] resolveContextLibraries(boolean includeSDK) {
        return resolveClassPathLibs("META-INF/MANIFEST.MF", includeSDK,true);
    }

    public static URL[] resolveContextLibraries(boolean includeSDK, boolean includeNonNonSDK) {
        return resolveClassPathLibs("META-INF/MANIFEST.MF", includeSDK,includeNonNonSDK);
    }

    public static URL[] resolveClassPathLibs(String referenceURL, boolean includeSDK, boolean includeNonSDK) {

        Set<URL> urls = new HashSet<URL>();
        ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();
        ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
        if(includeSDK){
            //sdk is found only in sysClassLoader
            String javaHome = System.getProperty("java.home");
            for (String s1 : System.getProperty("java.class.path").split(System.getProperty("path.separator"))) {
                if (s1.startsWith(javaHome + "/")) {
                    try {
                        File file = new File(s1);
                        if(file.exists()) {
                            urls.add(file.toURI().toURL());
                        }
                    } catch (MalformedURLException e) {
                        log.log(Level.SEVERE, "Unable to load Classpath Context. invalid path "+s1, e);
                    }
                }
            }
            //this works only on JDK<9 !!
            //should see this :: https://stackoverflow.com/questions/41932635/scanning-classpath-modulepath-in-runtime-in-java-9
            String sun_boot_class_path = System.getProperty("sun.boot.class.path");
            if(sun_boot_class_path!=null) {
                for (String s1 : sun_boot_class_path.split(System.getProperty("path.separator"))) {
                    //if (s1.startsWith(javaHome + "/")) {
                        try {
                            File file = new File(s1);
                            if(file.exists()) {
                                urls.add(file.toURI().toURL());
                            }
                        } catch (MalformedURLException e) {
                            log.log(Level.SEVERE, "Unable to load Classpath Context. invalid path "+s1, e);
                        }
                    //}
                }
            }
        }
        if(!includeNonSDK){
            return urls.toArray(new URL[0]);
        }
        if (threadClassLoader == null) {
            //do nothing
            log.log(Level.SEVERE, "Unable to load Classpath Context. Class loader is null");
        } else {
            if (sysClassLoader == threadClassLoader) {
                log.log(Level.FINE, "SystemClassLoader detected. Assuming Standalone Application");
                //simple standalone application
                String javaHome = System.getProperty("java.home");
                for (String s1 : System.getProperty("java.class.path").split(System.getProperty("path.separator"))) {
                    if ((!s1.startsWith(javaHome + "/"))) {
                        try {
                            File file = new File(s1);
                            if(file.exists()) {
                                urls.add(file.toURI().toURL());
                            }
                        } catch (MalformedURLException e) {
                            log.log(Level.SEVERE, "Unable to load Classpath Context", e);
                        }
                    }
                }
            } else {
                try {
                    //Only Class Path Roots that define META-INF/upa.xml will be parsed
                    Enumeration<URL> referenceURLPaths = threadClassLoader.getResources(referenceURL);
                    while (referenceURLPaths.hasMoreElements()) {
                        URL url = referenceURLPaths.nextElement();
                        urls.add(getClasspathRoot(url, referenceURL));
                    }
                    //now check all other urls
                    if(!"META-INF/MANIFEST.MF".equals(referenceURL)) {
                        for (URL url : Collections.list(threadClassLoader.getResources("META-INF/MANIFEST.MF"))) {
                            urls.add(getClasspathRoot(url, "META-INF/MANIFEST.MF"));
                        }
                    }
                    //for some reason some jar do not have META-INF files
                    for (URL url : Collections.list(threadClassLoader.getResources("/"))) {
                        urls.add(getClasspathRoot(url, "/"));
                    }
                    //for other bizarre reason some class folders are not listed too
                    for (URL url : Collections.list(threadClassLoader.getResources(""))) {
                        urls.add(getClasspathRoot(url, ""));
                    }

                } catch (Exception e) {
                    log.log(Level.SEVERE, "Unable to load UPA Context", e);
                }
            }
        }
        return urls.toArray(new URL[0]);
    }

    public static URL getClasspathRoot(URL url, String resource) throws MalformedURLException {
        URL root;
        String file = url.getFile();
        int resourceLength = resource.length();
        file = file.substring(0, file.length() - (resource.startsWith("/") ? resourceLength : (resourceLength + 1)));
        if (file.endsWith("!")) {
            file = file.substring(0, file.length() - 1);
        }
        String protocol = url.getProtocol();
        boolean unescaped = file.indexOf('%') == -1 && file.indexOf(' ') != -1;
        if ("file".equals(protocol)) {
            root = new File(file).toURI().toURL();
        } else if ("jar".equals(protocol) || "wsjar".equals(protocol)) {
            root = new URL(file);
            if ("file".equals(root.getProtocol())) {
                if (unescaped) {
                    root = new File(root.getFile()).toURI().toURL();
                }
            }
        } else if ("zip".equals(protocol)
                || "code-source".equals(url.getProtocol())) {
            if (unescaped) {
                root = new File(file).toURI().toURL();
            } else {
                root = new File(file).toURI().toURL();//.toURL() ?? why ?
            }
        } else {
            root = url;
        }
        return root;
    }

    public void visitClasses(URL[] urls, ClassVisitor visitor, ClassNameFilter filter) {
        for (Class clazz : resolveClasses(urls, filter)) {
            visitor.visitClass(clazz);
        }
    }

    public void visitClasses(URL[] urls, ClassVisitor visitor, ClassFilter filter) {
        for (Class clazz : resolveClasses(urls, filter)) {
            visitor.visitClass(clazz);
        }
    }

    public void visitClasses(URL[] urls, ClassVisitor visitor, ClassPathFilter filter) {
        for (Class clazz : resolveClasses(urls, filter)) {
            visitor.visitClass(clazz);
        }
    }

    public void visitContextAnnotations(AnnotationVisitor visitor, AnnotationFilter annotationFilter, boolean includeSDK) {
        visitAnnotations(resolveContextClasses(includeSDK), visitor, annotationFilter);
    }

    public void visitAnnotations(URL[] urls, AnnotationVisitor visitor, AnnotationFilter annotationFilter) {
        visitAnnotations(resolveClasses(urls), visitor, annotationFilter);
    }

    public void visitAnnotations(Iterable<Class> classes, AnnotationVisitor visitor, AnnotationFilter annotationFilter) {
        new AnnotationParser(classes, annotationFilter, visitor).parse();
    }
}
