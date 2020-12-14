/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.common.swings.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

/**
 *
 * @author thevpc
 */
public class LibInitializerManager {
    
    private static ServiceLoader<LibInitializer> codecSetLoader;
    public static void initializeLibraries(){
        if(codecSetLoader==null){
            codecSetLoader=ServiceLoader.load(LibInitializer.class);
            List<LibInitializer> remaining=new ArrayList<LibInitializer>();
            Set<String> done=new HashSet<String>();
            for (LibInitializer libInitializer : codecSetLoader) {
                final String[] dependencies = libInitializer.getDependencies();
                System.out.println("detected initializer "+libInitializer.getClass().getName()+(dependencies==null?"":(" Depending on "+Arrays.asList(dependencies))));
                remaining.add(libInitializer);
            }
            
            while(remaining.size()>0){
                boolean some=false;
                for(Iterator<LibInitializer> i=remaining.iterator();i.hasNext();){
                    LibInitializer next = i.next();
                    String[] dependencies = next.getDependencies();
                    boolean depOk=true;
                    if(dependencies!=null){
                        for (String d : dependencies) {
                            if(!done.contains(d)){
                                depOk=false;
                                break;
                            }
                        }
                    }
                    if(depOk){
                        done.add(next.getClass().getName());
                        System.out.println("initializing "+next.getClass().getName());
                        next.initializeLibrary();
                        i.remove();
                        some=true;
                    }
                }
                if(!some){
                    throw new IllegalArgumentException("Dependencies not verified :"+remaining);
                }
            }
            
        }
    }
}
