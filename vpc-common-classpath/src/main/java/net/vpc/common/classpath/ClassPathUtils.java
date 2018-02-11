/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.classpath;

import java.io.File;
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

    public static List<Class> resolveContextClassesList() {
        return iterableToList(resolveContextClasses((ClassPathFilter) null));
    }

    public static List<String> resolveContextClassNamesList() {
        return iterableToList(resolveContextClassNames((ClassPathFilter) null));
    }

    public static List<Class> resolveContextClassesList(ClassNameFilter filter) {
        return iterableToList(resolveContextClasses(filter));
    }

    public static List<Class> resolveContextClassesList(ClassFilter filter) {
        return iterableToList(resolveContextClasses(filter));
    }

    public static List<Class> resolveContextClassesList(ClassPathFilter filter) {
        return iterableToList(resolveContextClasses(filter));
    }

    public static List<String> resolveContextClassNamesList(ClassPathFilter filter) {
        return iterableToList(resolveContextClassNames(filter));
    }

    public static Iterable<Class> resolveContextClasses() {
        return resolveClasses(
                resolveContextLibraries(),
                (ClassPathFilter) null,
                Thread.currentThread().getContextClassLoader()

        );
    }

    public static Iterable<Class> resolveContextClasses(ClassFilter filter) {
        return resolveClasses(
                resolveContextLibraries(),
                filter,
                Thread.currentThread().getContextClassLoader()

        );
    }

    public static Iterable<Class> resolveContextClasses(ClassPathFilter filter) {
        return resolveClasses(
                resolveContextLibraries(),
                filter,
                Thread.currentThread().getContextClassLoader()

        );
    }

    public static Iterable<Class> resolveContextClasses(ClassNameFilter filter) {
        return resolveClasses(
                resolveContextLibraries(),
                classNameFilterToClassPathFilter(filter),
                Thread.currentThread().getContextClassLoader()

        );
    }

    public static Iterable<Class> resolveClasses(URL[] urls, ClassLoader classLoader) {
        return resolveClasses(urls,(ClassPathFilter) null,classLoader);
    }

    public static Iterable<Class> resolveClasses(URL[] urls) {
        return resolveClasses(urls,(ClassPathFilter) null,null);
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

    public static Iterable<String> resolveClassNames(URL[] urls, ClassPathFilter filter) {
        return new URLClassNameIterable(
                urls,
                filter

        );
    }

    public static Iterable<String> resolveContextClassNames() {
        return resolveContextClassNames((ClassPathFilter) null);
    }

    public static Iterable<String> resolveContextClassNames(ClassNameFilter filter) {
        return new URLClassNameIterable(
                resolveContextLibraries(),
                classNameFilterToClassPathFilter(filter)

        );
    }

    public static Iterable<String> resolveContextClassNames(ClassPathFilter filter) {
        return new URLClassNameIterable(
                resolveContextLibraries(),
                filter

        );
    }

    public static URL[] resolveContextLibraries() {
        return resolveClassPathLibs("META-INF/MANIFEST.MF");
    }

    public static URL[] resolveClassPathLibs(String referenceURL) {

        Set<URL> urls = new HashSet<URL>();
        ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();
        ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
        if (threadClassLoader == null) {
            //do nothing
            log.log(Level.SEVERE, "Unable to load Classpath Context. Class loader is null");
        } else {
            if (sysClassLoader == threadClassLoader) {
                log.log(Level.FINE, "SystemClassLoader detected. Assuming Standalone Application");
                //simple standalone application
                String javaHome = System.getProperty("java.home");
                for (String s1 : System.getProperty("java.class.path").split(System.getProperty("path.separator"))) {
                    if (s1.startsWith(javaHome + "/")) {
                        //ignore
                    } else {
                        try {
                            urls.add(new File(s1).toURI().toURL());
                        } catch (MalformedURLException e) {
                            log.log(Level.SEVERE, "Unable to load Classpath Context", e);
                        }
                    }
                }
            } else {
                try {
                    //Only Class Path Roots that define META-INF/upa.xml will be parsed
                    Enumeration<URL> upaXmls = threadClassLoader.getResources(referenceURL);
                    while (upaXmls.hasMoreElements()) {
                        URL url = upaXmls.nextElement();
                        urls.add(getClasspathRoot(url, referenceURL));
                    }
                    //now check all other urls
                    for (URL url : Collections.list(threadClassLoader.getResources("META-INF/MANIFEST.MF"))) {
                        urls.add(getClasspathRoot(url, "META-INF/MANIFEST.MF"));
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
        return urls.toArray(new URL[urls.size()]);
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

    public void visitContextAnnotations(AnnotationVisitor visitor, AnnotationFilter annotationFilter) {
        visitAnnotations(resolveContextClasses(),visitor,annotationFilter);
    }

    public void visitAnnotations(URL[] urls, AnnotationVisitor visitor, AnnotationFilter annotationFilter) {
        visitAnnotations(resolveClasses(urls),visitor,annotationFilter);
    }

    public void visitAnnotations(Iterable<Class> classes, AnnotationVisitor visitor, AnnotationFilter annotationFilter) {
        new AnnotationParser(classes, annotationFilter, visitor).parse();
    }
}
