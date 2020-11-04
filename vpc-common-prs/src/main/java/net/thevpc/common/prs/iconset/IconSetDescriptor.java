/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
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
package net.thevpc.common.prs.iconset;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import net.thevpc.common.prs.log.LoggerProvider;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 23 juin 2006 18:12:57
 */
public class IconSetDescriptor {

    private String id;
    private String name;
    private String group;
    private String className;
    private Object owner;
    private ArrayList<String> initialParameters;
    private ClassLoader classLoader;

    public IconSetDescriptor(String path, Object owner, ClassLoader loader) {
        String[] s = path.split("\\.");
        this.id = s[s.length - 1];
        this.className = DefaultIconSet.class.getName();
        this.initialParameters = new ArrayList<String>(Arrays.asList(path));
        this.classLoader = loader;
        this.owner = owner;

    }

    public IconSetDescriptor(String id, String name, String group, String className, String[] initialParameters, Object owner, ClassLoader loader) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.className = className == null ? DefaultIconSet.class.getName() : className;
        this.initialParameters = new ArrayList<String>(Arrays.asList(initialParameters));
        this.classLoader = loader;
        this.owner = owner;
    }

    public Object getOwner() {
        return owner;
    }

    public String getGroup() {
        return group;
    }

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public String[] getInitialParameters() {
        return initialParameters.toArray(new String[initialParameters.size()]);
    }

    public IconSet createIconSet(Locale locale, LoggerProvider loggerProvider) throws IconSetNotFoundException {
        try {
            Class<?> aClass = Class.forName(getClassName(), true, getClassLoader());
            Constructor<?> constructor = null;
            try {
                constructor = aClass.getConstructor(IconSetDescriptor.class, Locale.class, LoggerProvider.class);
            } catch (Exception e) {
                throw new IllegalArgumentException("Expected Consturctor(IconSetDescriptor, Locale,LoggerProvider)", e);
                //ignore
            }
            return (IconSet) constructor.newInstance(this, locale, loggerProvider);
        } catch (InvalidIconSetException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            System.err.println(e.getCause().toString());
            throw new IconSetNotFoundException(id);
        } catch (Throwable e) {
            System.err.println(e.toString());
//            e.printStackTrace();
            throw new IconSetNotFoundException(id);
        }
    }

    public ClassLoader getClassLoader() {
        return classLoader == null ? getClass().getClassLoader() : classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "IconSetDesc(" + id + ";" + name + ";" + className + ";" + initialParameters + ";" + classLoader + ")";
    }
}
