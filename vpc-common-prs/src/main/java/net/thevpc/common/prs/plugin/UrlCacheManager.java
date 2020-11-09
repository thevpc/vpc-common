/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
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
