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

package net.thevpc.common.prs.plugin;

import net.thevpc.common.prs.util.PRSPrivateIOUtils;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 23 sept. 2007 17:00:45
 */
public class UrlCacheManager {
    private Map<URL, URL> urlMapper = new HashMap<URL, URL>();
    private File cacheFolder;
    private Algorithm algorithm = Algorithm.OWNER;

    public static enum Algorithm {
        FLAT,
        OWNER,
    }

    public UrlCacheManager(File cacheFolder) {
        this.cacheFolder = cacheFolder == null ? new File(System.getProperty("java.io.tmpdir") + "/prstempsdir") : cacheFolder;
    }
    public void eradicate(File f) {
        if(f.exists()){
            if(f.isFile()){
                f.delete();
            }else if(f.isDirectory()){
                for (File file : f.listFiles()) {
                    eradicate(file);
                }
                f.delete();
            }else{
                throw new IllegalArgumentException("could not eradicate : "+f);
            }
        }
    }
    public void clearCacheFolder() {
        File defaultTempFile = cacheFolder;
        if (defaultTempFile.exists() && defaultTempFile.isDirectory()) {
            for (File file : defaultTempFile.listFiles()) {
                eradicate(file);
            }
        }
    }

    public Map<URL, URL> getUrlMapper() {
        return urlMapper;
    }

    public File getCacheFolder() {
        return cacheFolder;
    }

    public URL translateURL(URL url, String owner) throws IOException {
        switch (algorithm) {
            case FLAT: {
                return translateURLFlat(url, owner);
            }
            case OWNER: {
                return translateURLOwner(url, owner);
            }
        }
        return translateURLFlat(url, owner);
    }

    protected URL translateURLFlat(URL url, String owner) throws IOException {
        if (urlMapper != null) {
            URL url1 = getUrlMapper().get(url);
            if (url1 != null) {
                return url1;
            }
        }
        File defaultTempFile = getCacheFolder();
        if (defaultTempFile != null && !defaultTempFile.exists()) {
            defaultTempFile.mkdirs();
        }
        if (defaultTempFile != null && !defaultTempFile.exists()) {
            defaultTempFile = null;
        }
        File ff = new File(getURLName(url));
        String prefix = PRSPrivateIOUtils.getFileNameWithoutExtension(ff);
        String suffix = PRSPrivateIOUtils.getFileExtension(ff);
        while (prefix.length() < 2) {
            prefix += "_";
        }
        prefix += ".";
        suffix = ((owner == null || owner.length() == 0) ? "" : ("." + owner)) + ".cache." + suffix;
//        while(suffix.length()<3){
//            suffix+="_";
//        }
        File temp = File.createTempFile(prefix, suffix, defaultTempFile);

        FileOutputStream os = new FileOutputStream(temp);
        InputStream is = url.openStream();
        byte[] buffer = new byte[1024];
        int c;
        while ((c = is.read(buffer)) > 0) {
            os.write(buffer, 0, c);
        }
        os.close();
        is.close();
        URL url2 = temp.toURL();

        getUrlMapper().put(url, url2);
        return url2;
    }

    protected URL translateURLOwner(URL url, String owner) throws IOException {
        if (urlMapper != null) {
            URL url1 = getUrlMapper().get(url);
            if (url1 != null) {
                return url1;
            }
        }
        File defaultTempFile = getCacheFolder();
        if (defaultTempFile != null && !defaultTempFile.exists()) {
            defaultTempFile.mkdirs();
        }
        if (defaultTempFile != null && !defaultTempFile.exists()) {
            defaultTempFile = null;
        }
        File temp ;
        File tempFolder = new File(defaultTempFile, owner);
        tempFolder.mkdirs();
        File ff = new File(tempFolder, getURLName(url));
        if (ff.exists()) {
            String prefix = PRSPrivateIOUtils.getFileNameWithoutExtension(ff);
            String suffix = PRSPrivateIOUtils.getFileExtension(ff);
            while (prefix.length() < 2) {
                prefix += "_";
            }
            prefix += ".";
            suffix = ".cache." + suffix;
//        while(suffix.length()<3){
//            suffix+="_";
//        }
            temp = File.createTempFile(prefix, suffix, tempFolder);
        } else {
            temp = ff;
        }
        FileOutputStream os = new FileOutputStream(temp);
        InputStream is = url.openStream();
        byte[] buffer = new byte[1024];
        int c;
        while ((c = is.read(buffer)) > 0) {
            os.write(buffer, 0, c);
        }
        os.close();
        is.close();
        URL url2 = temp.toURL();

        getUrlMapper().put(url, url2);
        return url2;
    }

    protected String getURLName(URL url) {
        String p = url.getPath();
        int x = p.lastIndexOf('/');
        if (x >= 0) {
            p = p.substring(x + 1);
        }
        x = p.lastIndexOf('!');
        if (x >= 0) {
            p = p.substring(x + 1);
        }
        return p;
    }
}
