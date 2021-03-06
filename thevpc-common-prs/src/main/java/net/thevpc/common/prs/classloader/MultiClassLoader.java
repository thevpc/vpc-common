/**
 * ====================================================================
 *                        vpc-prs library
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
package net.thevpc.common.prs.classloader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 28 août 2007 12:35:01
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
