/**
 * ====================================================================
 *                        vpc-commons library
 *
 * Description: <start><end>
 *
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.thevpc.common.prs.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 3 janv. 2006 21:40:14
 */
public class Reflector {
    /**
     * reflector look after all fields in the class and its super classes
     */
    public static final int FIELDS=1;
    /**
     * reflector look after all fields in the class and its super classes
     */
    public static final int METHODS=2;

    public static final int OVERRIDDEN_METHODS=4;
    public static final int PARENT_CLASSES=8;
    public static final int PARENT_INTERFACES=16;
    public static final int DEFAULT=FIELDS|METHODS|PARENT_CLASSES|PARENT_INTERFACES;
    public static final int ALL=FIELDS|METHODS|OVERRIDDEN_METHODS|PARENT_CLASSES|PARENT_INTERFACES;
    private Reflector() {
    }
    public static class ClassInfo{
        private List<Field> fields;
        private List<Method> methods;
        private List<Class> parentClasses;
        private List<Class> parentInterfaces;

        public ClassInfo() {
        }

        public List<Field> getFields() {
            return fields;
        }

        public void setFields(List<Field> fields) {
            this.fields = fields;
        }

        public List<Method> getMethods() {
            return methods;
        }

        public void setMethods(List<Method> methods) {
            this.methods = methods;
        }

        public List<Class> getParentClasses() {
            return parentClasses;
        }

        public void setParentClasses(List<Class> parentClasses) {
            this.parentClasses = parentClasses;
        }

        public List<Class> getParentInterfaces() {
            return parentInterfaces;
        }

        public void setParentInterfaces(List<Class> parentInterfaces) {
            this.parentInterfaces = parentInterfaces;
        }
    }

    public static ClassInfo getClassInfo(Class clz, FieldFilter fieldsFilter, MethodFilter methodsFilter, int flags) {
        boolean findFields=(flags & FIELDS)!=0;
        boolean findMethods=((flags & METHODS)!=0 || (flags & OVERRIDDEN_METHODS)!=0);
        boolean findParentClasses=(flags & PARENT_CLASSES)!=0;
        boolean findInterfaces=(flags & PARENT_INTERFACES)!=0;
        boolean deeperMethod=(flags & OVERRIDDEN_METHODS)!=0;
        if(((flags & METHODS)!=0 && (flags & OVERRIDDEN_METHODS)!=0)){
            throw new IllegalArgumentException("Invalid flags : METHODS and OVERRIDDEN_METHODS are exclusives");
        }
        if((flags&(ALL))!=flags){
          throw new IllegalArgumentException("Invalid flags");
        }
        return getClassInfo(clz, fieldsFilter, methodsFilter, findFields, findMethods, findParentClasses, findInterfaces, deeperMethod);
    }

    public static ClassInfo getClassInfo(Class clz, FieldFilter fieldsFilter, MethodFilter methodsFilter, boolean findFields, boolean findMethods, boolean findParentClasses, boolean findInterfaces, boolean deeperMethod) {
        ClassInfo ret=new ClassInfo();
        ret.setFields(findFields ? new ArrayList<Field>() : null);
        ret.setMethods(findMethods ? new ArrayList<Method>() : null);
        ret.setParentClasses(findParentClasses? new ArrayList<Class>():null);
        ret.setParentInterfaces(findInterfaces? new ArrayList<Class>():null);
        searchFields(clz, ret.getFields(), ret.getMethods(), ret.getParentClasses(), ret.getParentInterfaces(), fieldsFilter, methodsFilter, new HashSet<Class>(), true, new HashMap<String, Method>(), deeperMethod);
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
    public static void searchFields(Class clz, List<Field> fieldsList, List<Method> methodsList, List<Class> parentClasses, List<Class> interfaces, FieldFilter fieldsFilter, MethodFilter methodsFilter, boolean deeperMethod) {
        searchFields(clz, fieldsList, methodsList, parentClasses, interfaces, fieldsFilter, methodsFilter, new HashSet<Class>(), true, new HashMap<String, Method>(), deeperMethod);
    }

    private static void searchFields(Class clz, List<Field> fieldsList, List<Method> methodsList, List<Class> classes, List<Class> interfaces, FieldFilter fieldsFilter, MethodFilter methodsFilter, Set<Class> visitedClasses, boolean addFirst, Map<String, Method> visitedMethods, boolean deeperMethod) {
        if (fieldsFilter == null) {
            fieldsFilter = FieldFilter.NONE;
        }
        if (methodsFilter == null) {
            methodsFilter = MethodFilter.NONE;
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
            for (Field field : clz.getDeclaredFields()) {
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
            for (Method method : clz.getDeclaredMethods()) {
                if (methodsFilter.accept(method)) {
                    boolean addMethod = true;
                    if (deeperMethod && !Modifier.isPrivate(method.getModifiers())) {
                        Method mm = visitedMethods.get(getMethodSignature(method));
                        if (mm != null) {
                            if (method.getClass().isAssignableFrom(mm.getDeclaringClass())) {
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

        for (Class interfaze : clz.getInterfaces()) {
            searchFields(interfaze, fieldsList, methodsList, classes, interfaces, fieldsFilter, methodsFilter, visitedClasses, addFirst, visitedMethods, deeperMethod);
        }
        Class sclazz = clz.getSuperclass();
        if (sclazz != null) {
            searchFields(sclazz, fieldsList, methodsList, classes, interfaces, fieldsFilter, methodsFilter, visitedClasses, addFirst, visitedMethods, deeperMethod);
        }
    }

    private static String getMethodSignature(Method method) {
        StringBuilder b = new StringBuilder(method.getName());
        b.append("(");
        boolean first = true;

        for (Class<?> p : method.getParameterTypes()) {
            if (first) {
                first = false;
            } else {
                b.append(",");
            }
            b.append(p.getName());
        }
        b.append(")");
        return b.toString();
    }

}
