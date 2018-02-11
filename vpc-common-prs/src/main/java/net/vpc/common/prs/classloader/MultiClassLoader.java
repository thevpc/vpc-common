/**
 * ====================================================================
 *                        vpc-prs library
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
package net.vpc.common.prs.classloader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 28 août 2007 12:35:01
 */
public class MultiClassLoader extends ClassLoader {

    private ClassLoader[] delagateLoaders;

    public MultiClassLoader(ClassLoader baseLoader, ClassLoader... cloaders) {
        super(baseLoader);
        setDelagateLoaders(cloaders);
    }

    public ClassLoader[] getDelagateLoaders() {
        return delagateLoaders;
    }

    public final void setDelagateLoaders(ClassLoader... delagateLoaders) {
        this.delagateLoaders = delagateLoaders == null ? new ClassLoader[0] : delagateLoaders;
    }

    @Override
    protected Class findClass(String name) throws ClassNotFoundException {
        for (ClassLoader delagateLoader : delagateLoaders) {
            try {
                Class<?> c = delagateLoader.loadClass(name);
                System.out.println("Loaded "+c+" from "+delagateLoader);
                return c;
            } catch (java.lang.NoClassDefFoundError e) {
                //ignore
            } catch (ClassNotFoundException e) {
                //
            }
        }
        throw new ClassNotFoundException(name);
    }

    @Override
    protected URL findResource(String name) {
        URL resource;
        for (ClassLoader delagateLoader : delagateLoaders) {
            resource = delagateLoader.getResource(name);
            if (resource != null) {
                return resource;
            }
        }
        return null;
    }

    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        ArrayList<URL> a = new ArrayList<URL>();
        Enumeration<URL> resources = super.findResources(name);
        while (resources.hasMoreElements()) {
            a.add(resources.nextElement());
        }
        for (ClassLoader delagateLoader : delagateLoaders) {
            resources = delagateLoader.getResources(name);
            while (resources.hasMoreElements()) {
                a.add(resources.nextElement());
            }
        }
        return Collections.enumeration(a);
    }
//    protected String findLibrary(String libname) {
//        String resource;
//        resource = super.findLibrary(libname);
//        if(resource==null){
//            for(int i=0;i< delagateLoaders.length;i++){
//                resource = delagateLoaders[0].findLibrary(libname);
//                if(resource!=null){
//                    break;
//                }
//            }
//        }
//        return resource;
//    }
}
