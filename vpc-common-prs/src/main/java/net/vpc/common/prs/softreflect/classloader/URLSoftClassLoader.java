/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.prs.softreflect.classloader;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

import net.vpc.common.prs.softreflect.SoftClassBuilder;
import net.vpc.common.prs.softreflect.SoftReflector;
import net.vpc.common.prs.softreflect.SoftClass;

/**
 *
 * @author vpc
 */
public class URLSoftClassLoader extends AbstractSoftClassLoader {

    private URL[] urls;
    private URLClassLoader ucl;
    private SoftClassBuilder classBuilder;
    private String name;

    public URLSoftClassLoader(String name,URL[] urls, SoftClassBuilder classBuilder, SoftClassLoader parent) {
        super(parent);
        this.name = name;
        this.urls = urls;
        this.classBuilder = classBuilder==null? SoftReflector.getClassBuilder():classBuilder;
        ucl = new URLClassLoader(urls);
    }

    @Override
    public String toString() {
        StringBuilder urlsString=new StringBuilder();
        for (URL url : urls) {
            String s=null;
            if("file".equals(url.getProtocol())){
                s=new File(url.getFile()).getName();
            }else{
                s=url.toString();
            }
            if(urlsString.length()>0){
                urlsString.append(":");
            }
            urlsString.append(s);
        }
        return "RURLClassLoader{" + "name=" + name + ", urls=" + urlsString + "; parent ="+getParent()+'}';
    }
    
    

    @Override
    public SoftClass loadClass(String name) {
        String r = name.replace('.', '/').concat(".class");
        URL u = ucl.findResource(r);
        if (u == null) {
            return null;
        }
        InputStream data = null;
        try {
            SoftClass cc = classBuilder.buildClass(data = u.openStream());
            return cc;
        } catch (Exception e) {
            return null;
        } finally {
            if (data != null) {
                try {
                    data.close();
                } catch (Exception ee) {
                    //ignore
                }
            }
        }
    }

    public URL[] getUrls() {
        return urls;
    }
    
    
}
