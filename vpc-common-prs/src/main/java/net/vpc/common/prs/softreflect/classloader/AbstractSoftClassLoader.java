/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.prs.softreflect.classloader;

import java.util.HashMap;
import java.util.Map;
import net.vpc.common.prs.softreflect.SoftClass;
import net.vpc.common.prs.softreflect.SoftClassNotFoundException;

/**
 *
 * @author vpc
 */
public abstract class AbstractSoftClassLoader implements SoftClassLoader {

    private final SoftClassLoader parent;
    private Map<String, SoftClass> loaded;

    public AbstractSoftClassLoader(SoftClassLoader parent) {
        this.parent = parent;
    }

    @Override
    public final SoftClass findClass(String name) {
        if (loaded != null) {
            SoftClass c = loaded.get(name);
            if (c != null) {
                return c;
            }
        }
        SoftClass c = null;
        if (parent != null) {
            try {
                c = parent.findClass(name);
            } catch (SoftClassNotFoundException e) {
                //ignore
            }
        }
        if (c == null) {
            c = loadClass(name);
        }
        if (c == null) {
            throw new SoftClassNotFoundException("Class not found " + name);
        }
        c.setClassLoader(this);
        if (loaded == null) {
            loaded = new HashMap<String, SoftClass>();
        }
        loaded.put(name, c);
        return c;
    }

    public abstract SoftClass loadClass(String name);

    public SoftClassLoader getParent() {
        return parent;
    }
    
}
