/**
 * ====================================================================
 *                        vpc-commons library
 *
 * Description: <start><end>
 *
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
package net.thevpc.common.prs.softreflect;

import java.io.File;

import net.thevpc.common.prs.softreflect.classloader.SoftClassLoader;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import net.thevpc.common.prs.util.PRSPrivateIOUtils;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 3 janv. 2006 21:40:14
 */
public class SoftReflector {

    /**
     * reflector look after all fields in the class and its super classes
     */
    public static final int FIELDS = 1;
    /**
     * reflector look after all fields in the class and its super classes
     */
    public static final int METHODS = 2;
    public static final int OVERRIDDEN_METHODS = 4;
    public static final int PARENT_CLASSES = 8;
    public static final int PARENT_INTERFACES = 16;
    public static final int DEFAULT = FIELDS | METHODS | PARENT_CLASSES | PARENT_INTERFACES;
    public static final int ALL = FIELDS | METHODS | OVERRIDDEN_METHODS | PARENT_CLASSES | PARENT_INTERFACES;

    private SoftReflector() {
    }
    private static SoftClassBuilderASM b = new SoftClassBuilderASM();

    public static SoftClassBuilder getClassBuilder() {
        return b;
    }

    public static SoftClassInfo getClassInfo(SoftClass clz, SoftFieldFilter fieldsFilter, SoftMethodFilter methodsFilter, int flags) {
        boolean findFields = (flags & FIELDS) != 0;
        boolean findMethods = ((flags & METHODS) != 0 || (flags & OVERRIDDEN_METHODS) != 0);
        boolean findParentClasses = (flags & PARENT_CLASSES) != 0;
        boolean findInterfaces = (flags & PARENT_INTERFACES) != 0;
        boolean deeperMethod = (flags & OVERRIDDEN_METHODS) != 0;
        if (((flags & METHODS) != 0 && (flags & OVERRIDDEN_METHODS) != 0)) {
            throw new IllegalArgumentException("Invalid flags : METHODS and OVERRIDDEN_METHODS are exclusives");
        }
        if ((flags & (ALL)) != flags) {
            throw new IllegalArgumentException("Invalid flags");
        }
        return getClassInfo(clz, fieldsFilter, methodsFilter, findFields, findMethods, findParentClasses, findInterfaces, deeperMethod);
    }

    public static SoftClassInfo getClassInfo(SoftClass clz, SoftFieldFilter fieldsFilter, SoftMethodFilter methodsFilter, boolean findFields, boolean findMethods, boolean findParentClasses, boolean findInterfaces, boolean deeperMethod) {
        SoftClassInfo ret = new SoftClassInfo();
        ret.setFields(findFields ? new ArrayList<SoftField>() : null);
        ret.setMethods(findMethods ? new ArrayList<SoftMethod>() : null);
        ret.setParentClasses(findParentClasses ? new ArrayList<SoftClass>() : null);
        ret.setParentInterfaces(findInterfaces ? new ArrayList<SoftClass>() : null);
        searchFields(clz, ret.getFields(), ret.getMethods(), ret.getParentClasses(), ret.getParentInterfaces(), fieldsFilter, methodsFilter, new HashSet<SoftClass>(), true, new HashMap<String, SoftMethod>(), deeperMethod);
        return ret;
    }

    /**
     * @param clz           class to introspect
     * @param fieldsList    fields list to fill, if null, fields will not be looked after
     * @param methodsList   methods list to fill, if null, methods will not be looked after
     * @param fieldsFilter  fields filter
     * @param methodsFilter methods filter
     * @param deeperMethod  if true, only virtual methods implementation will be returned
     */
    public static void searchFields(SoftClass clz, List<SoftField> fieldsList, List<SoftMethod> methodsList, List<SoftClass> parentClasses, List<SoftClass> interfaces, SoftFieldFilter fieldsFilter, SoftMethodFilter methodsFilter, boolean deeperMethod) {
        searchFields(clz, fieldsList, methodsList, parentClasses, interfaces, fieldsFilter, methodsFilter, new HashSet<SoftClass>(), true, new HashMap<String, SoftMethod>(), deeperMethod);
    }

    private static void searchFields(SoftClass clz, List<SoftField> fieldsList, List<SoftMethod> methodsList, List<SoftClass> classes, List<SoftClass> interfaces, SoftFieldFilter fieldsFilter, SoftMethodFilter methodsFilter, Set<SoftClass> visitedClasses, boolean addFirst, Map<String, SoftMethod> visitedMethods, boolean deeperMethod) {
        if (fieldsFilter == null) {
            fieldsFilter = SoftFieldFilter.NONE;
        }
        if (methodsFilter == null) {
            methodsFilter = SoftMethodFilter.NONE;
        }
        if (visitedClasses.contains(clz)) {
            return;
        }
        if (clz.isInterface()) {
            if (interfaces != null) {
                interfaces.add(clz);
            }
        } else {
            if (classes != null) {
                classes.add(clz);
            }
        }
        visitedClasses.add(clz);
        int index = 0;
        if (fieldsList != null) {
            for (SoftField field : clz.getDeclaredFields()) {
                if (fieldsFilter.accept(field)) {
                    if (addFirst) {
                        fieldsList.add(index, field);
                        index++;
                    } else {
                        fieldsList.add(field);
                    }
                }
            }
        }
        if (methodsList != null) {
            index = 0;
            for (SoftMethod method : clz.getDeclaredMethods()) {
                if (methodsFilter.accept(method)) {
                    boolean addMethod = true;
                    if (deeperMethod && !Modifier.isPrivate(method.getModifiers())) {
                        SoftMethod mm = visitedMethods.get(getMethodSignature(method));
                        if (mm != null) {
                            if (isAssignableFrom(method.getDeclaringClass(), mm.getDeclaringClass())) {
                                visitedMethods.put(getMethodSignature(method), method);
                                methodsList.remove(mm);
                            } else {
                                addMethod = false;
                            }
                        } else {
                            visitedMethods.put(getMethodSignature(method), method);
                        }
                    }
                    if (addMethod) {
                        if (addFirst) {
                            methodsList.add(index, method);
                            index++;
                        } else {
                            methodsList.add(method);
                        }
                    }
                }
            }
        }

        for (String interfaze : clz.getInterfaces()) {
            searchFields(clz.getClassLoader().findClass(interfaze), fieldsList, methodsList, classes, interfaces, fieldsFilter, methodsFilter, visitedClasses, addFirst, visitedMethods, deeperMethod);
        }
        String sclazz = clz.getSuperclass();
        if (sclazz != null) {
            SoftClass findClass = null;
            try {
                findClass = clz.getClassLoader().findClass(sclazz);
            } catch (Exception e) {
                findClass = clz.getClassLoader().findClass(sclazz);
            }
            searchFields(findClass, fieldsList, methodsList, classes, interfaces, fieldsFilter, methodsFilter, visitedClasses, addFirst, visitedMethods, deeperMethod);
        }
    }

    private static String getMethodSignature(SoftMethod method) {
        StringBuilder b = new StringBuilder(method.getName());
        b.append("(");
        boolean first = true;

        for (String p : method.getParameterTypes()) {
            if (first) {
                first = false;
            } else {
                b.append(",");
            }
            b.append(p);
        }
        b.append(")");
        return b.toString();
    }

    public static boolean isAssignableFrom(SoftClass a, SoftClass b) {
        Class aa;
        Stack<SoftClass> stack = new Stack<SoftClass>();
        Set<String> visited = new HashSet<String>();
        stack.push(b);
        while (!stack.isEmpty()) {
            SoftClass n = stack.pop();
            if (a.getName().equals(b.getName())) {
                return true;
            }
            visited.add(n.getName());
            final String c = n.getSuperclass();
            if (c != null && visited.contains(c)) {
                stack.add(n.getClassLoader().findClass(c));
            }
            for (String cc : n.getInterfaces()) {
                if (c != null && visited.contains(c)) {
                    stack.add(n.getClassLoader().findClass(c));
                }
            }
        }
        return false;
    }

    /**
     * URL must denote either a jar file or a local folder
     */
    public static void visitURLClasses(URL url, SoftClassVisitor visitor, SoftClassLoader classLoader) throws IOException {
        File folder = PRSPrivateIOUtils.urlToFile(url);
        if (folder != null && folder.isDirectory()) {
            int prefixLength = folder.getPath().length();
            if(!folder.getPath().endsWith("/") && !folder.getPath().endsWith("\\")){
                prefixLength++;
            }
            Stack<File> stack = new Stack<File>();
            stack.push(folder);
            while (!stack.isEmpty()) {
                File f = stack.pop();
                File[] ff = f.listFiles();
                if (ff != null) {
                    for (File f0 : ff) {
                        if (f0.isFile()) {
                            String path = f0.getPath().substring(prefixLength);
                            if (path.endsWith(".class") && path.indexOf('$') < 0) {
                                String className = path.substring(0, path.length() - 6).replace('/', '.');
                                try {
                                    SoftClass clazz = classLoader.findClass(className);
                                    try {
                                        visitor.visit(clazz);
                                    } catch (CancelVisitException e) {
                                        return;
                                    }
                                } catch (SoftClassNotFoundException ex) {
                                    System.err.println("Unable to load (" + className + ") due to ClassNotFoundException : " + ex + "\n==>" + folder);
                                    ex.printStackTrace();
                                } catch (Throwable ex) {
                                    System.err.println("Unable to load (" + className + ") due to " + ex.getClass().getSimpleName() + " : " + ex + "\n==>" + folder);
                                    ex.printStackTrace();
                                }
                            }
                        } else if (f0.isDirectory()) {
                            stack.push(f0);
                        }
                    }
                }
            }

        } else {
            JarInputStream jar = new JarInputStream(url.openStream());
            ZipEntry nextEntry;
            while ((nextEntry = jar.getNextJarEntry()) != null) {
                String path = nextEntry.getName();
                if (path.endsWith(".class") && path.indexOf('$') < 0) {
                    String className = path.substring(0, path.length() - 6).replace('/', '.');
                    try {
                        SoftClass clazz = classLoader.findClass(className);
                        try {
                            visitor.visit(clazz);
                        } catch (CancelVisitException e) {
                            return;
                        }
                    } catch (SoftClassNotFoundException ex) {
                        System.err.println("Unable to load (" + className + ") due to ClassNotFoundException : " + ex + "\n==>" + url);
                        ex.printStackTrace();
                    } catch (Throwable ex) {
                        System.err.println("Unable to load (" + className + ") due to " + ex.getClass().getSimpleName() + " : " + ex + "\n==>" + url);
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}
