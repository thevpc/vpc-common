package net.vpc.common.classpath;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Taha BEN SALAH <taha.bensalah@gmail.com>
 * @creationdate 12/16/12 1:00 PM
 */
public class AnnotationParser {

    private static final Logger log = Logger.getLogger(AnnotationParser.class.getName());
    public Iterable<Class> urls;
    public int nameStrategyModelConfigOrder = Integer.MIN_VALUE;
    public ClassLoader contextClassLoader;
    private AnnotationVisitor annotationVisitor;
    private AnnotationFilter annotationFilter;

    public AnnotationParser(Iterable<Class> urls, AnnotationFilter annotationFilter, AnnotationVisitor annotationVisitor) {
        this.urls = urls;
        this.annotationFilter = annotationFilter;
        this.annotationVisitor = annotationVisitor;
        this.contextClassLoader = Thread.currentThread().getContextClassLoader();
    }

//    private void configureFolder(File rootFolder, File folder, ClassPathFilter typeFilter) throws MalformedURLException, ClassNotFoundException {
//        File[] files = folder.listFiles();
//        URL src = rootFolder.toURI().toURL();
//        if (files != null) {
//            for (File file : files) {
//                if (file.isDirectory()) {
//                    configureFolder(rootFolder, file, typeFilter);
//                } else if (file.isFile()) {
//                    String path = file.getPath().substring(rootFolder.getPath().length());
//                    configureClassURL(src, path);
//                }
//            }
//        }
//    }

    public void visit(Class type, AnnotationFilter decorationFilter) {
        boolean types = decorationFilter.isSupportedTypeDecoration();
        boolean methods = decorationFilter.isSupportedMethodDecoration();
        boolean fields = decorationFilter.isSupportedFieldDecoration();
//        boolean tree = (kind & DecorationFilter.HIERARCHICAL) != 0;
//        boolean someType = false;
        if (types) {
            Annotation[] annotations = null;
            try {
                annotations = type.getAnnotations();
            } catch (Throwable e) {
                log.log(Level.FINE, "Ignored type {0} : {1}", new Object[]{type.getName(), e.toString()});
                //ignore
            }
            if (annotations != null) {
                int pos = 0;
                for (Annotation a : annotations) {
                    if (decorationFilter.acceptTypeDecoration(a.annotationType().getName(), type.getName(), type)) {
                        annotationVisitor.visitTypeAnnotation(a, type);
//                    someType = true;
                    }
                    pos++;
                }
            }
        }
        if (methods /*&& (!tree || someType)*/) {
            Method[] declaredMethods = null;
            try {
                declaredMethods = type.getDeclaredMethods();
            } catch (Throwable e) {
                log.log(Level.FINE, "Ignored type {0} : {1}", new Object[]{type.getName(), e.toString()});
                //ignore
            }
            if (declaredMethods != null) {
                for (Method method : declaredMethods) {
                    int pos = 0;
                    for (Annotation a : method.getAnnotations()) {
                        String methodSig = getMethodSignature(method);
                        if (decorationFilter.acceptMethodDecoration(a.annotationType().getName(), methodSig, type.getName(), method)) {
                            annotationVisitor.visitMethodAnnotation(a, method);
                        }
                        pos++;
                    }
                }
            }
        }
        if (fields /*&& (!tree || someType)*/) {
            Field[] declaredFields = null;
            try {
                declaredFields = type.getDeclaredFields();
            } catch (Throwable e) {
                log.log(Level.FINE, "Ignored type {0} : {1}", new Object[]{type.getName(), e.toString()});
                //ignore
            }
            if (declaredFields != null) {
                for (Field field : declaredFields) {
                    int pos = 0;
                    for (Annotation a : field.getAnnotations()) {
                        if (decorationFilter.acceptFieldDecoration(a.annotationType().getName(), field.getName(), type.getName(), field)) {
                            annotationVisitor.visitFieldAnnotation(a, field);
                        }
                        pos++;
                    }
                }
            }
        }
    }

//    public Decoration getDecoration(Class type, Class annotationClass) {
//        return UPAReflector.getDecoration(type, annotationClass, persistenceGroupName, persistenceUnitName, decorationRepository);
//    }
    public void parse() throws IOException, ClassNotFoundException, URISyntaxException {
        for (Class type : urls) {
            visit(type, annotationFilter);
        }
    }

    public static String getMethodSignature(Method method) {
        StringBuilder types = new StringBuilder();
        for (Class<?> parameterType : method.getParameterTypes()) {
            if (types.length() > 0) {
                types.append(",");
            }
            types.append(parameterType.getName());
        }
        return method.getName() + "(" + types + ")";
    }
}
