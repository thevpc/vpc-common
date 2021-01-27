/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.prs.softreflect.classloader;

import java.util.Arrays;
import net.thevpc.common.prs.softreflect.SoftClass;

/**
 *
 * @author thevpc
 */
public class MultiSoftClassLoader extends AbstractSoftClassLoader {

    private SoftClassLoader[] others;
    private String name;
    public MultiSoftClassLoader(String name,SoftClassLoader[] others, SoftClassLoader parent) {
        super(parent);
        this.others = others;
        this.name = name;
    }

    @Override
    public SoftClass loadClass(String name) {
        for (SoftClassLoader a : others) {
            SoftClass clz=null;
            try{
                clz=a.findClass(name);
            }catch(Exception e){
                //
            }
            if(clz!=null){
                return clz;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "MultiSoftClassLoader{ name="+name+" : " + Arrays.asList(others) + "; parent ="+getParent()+'}';
    }
    
    
}
