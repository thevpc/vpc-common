/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.prs.softreflect.classloader;

import net.thevpc.common.prs.softreflect.SoftClass;
import net.thevpc.common.prs.softreflect.HostToSoftClass;

/**
 *
 * @author thevpc
 */
public class HostToSoftClassLoader extends AbstractSoftClassLoader {
    private ClassLoader sunLoader;
    private String name;
    public HostToSoftClassLoader(String name,ClassLoader sunLoader,SoftClassLoader parent) {
        super(parent);
        this.sunLoader = sunLoader;
        this.name = name;
    }

    @Override
    public SoftClass loadClass(String name) {
        try {
            Class clz=sunLoader.loadClass(name);
            return new HostToSoftClass(clz, this);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "HostToSoftClassLoader{name=" +name+" ; "+ sunLoader + "; parent ="+getParent()+'}';
    }
    
}
