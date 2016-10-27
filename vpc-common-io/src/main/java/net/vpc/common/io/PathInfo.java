/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.io;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import static net.vpc.common.io.IOUtils.toFileLenient;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public class PathInfo {

    /**
     * Parent folder name
     */
    private String dirName;
    /**
     * file name (no path)
     */
    private String baseName;
    /**
     * full path
     */
    private String pathName;
    /**
     * extension
     */
    private String extensionPart;
    /**
     * name without extension
     */
    private String namePart;

    private PathInfo() {
    }


    private PathInfo(String dirName, String baseName, String pathName, String extensionPart, String namePart) {
        this.dirName = dirName;
        this.baseName = baseName;
        this.pathName = pathName;
        this.extensionPart = extensionPart;
        this.namePart = namePart;
    }

    public String getDirName() {
        return dirName;
    }

//    public void setDirName(String dirName) {
//        this.dirName = dirName;
//    }

    public String getBaseName() {
        return baseName;
    }

//    public void setBaseName(String baseName) {
//        this.baseName = baseName;
//    }

    public String getPathName() {
        return pathName;
    }

//    public void setPathName(String pathName) {
//        this.pathName = pathName;
//    }

    public String getExtensionPart() {
        return extensionPart;
    }

//    public void setExtensionPart(String extensionPart) {
//        this.extensionPart = extensionPart;
//    }

    public String getNamePart() {
        return namePart;
    }

//    public void setNamePart(String namePart) {
//        this.namePart = namePart;
//    }

    @Override
    public String toString() {
        return formatPath(dirName,namePart,extensionPart);
//        return "PathInfo{" + "dirName=" + dirName + ", baseName=" + baseName + ", pathName=" + pathName + ", extensionPart=" + extensionPart + ", namePart=" + namePart + '}';
    }

    public static String formatPath(String dirName,String name,String extension){
        StringBuilder sb=new StringBuilder();
        if(dirName!=null && dirName.length()>0){
            sb.append(dirName);
            if(!dirName.endsWith("/")){
                sb.append("/");
            }
        }
        if(name!=null && name.length()>0){
            sb.append(name);
        }
        if(extension!=null && extension.length()>0){
            sb.append(".");
            sb.append(extension);
        }
        return sb.toString();
   }


    public static PathInfo create(Object source) {
        String dirName = null;
        String baseName = null;
        String pathName = null;
        String sourceName = null;
        if (source != null) {
            boolean url = false;
            boolean file = false;
            if (source instanceof String) {
                sourceName = (String) source;
            } else if (source instanceof File) {
                file = true;
                sourceName = ((File) source).getPath();
            } else if (source instanceof URL) {
                url = true;
                sourceName = ((URL) source).toString();
            }
            if (sourceName != null) {
                File the_file = toFileLenient(sourceName);
                if (the_file != null) {
                    pathName = the_file.getPath();
                    baseName = the_file.getName();
                    dirName = the_file.getParentFile() == null ? null : the_file.getParentFile().getPath();
                } else if (url || (!url && !file && IOUtils.isURL(sourceName))) {
                    if (sourceName.startsWith("file:")) {
                        try {
                            Path tt = Paths.get(new URL(sourceName).toURI());
                            File ff = tt.toFile();
                            pathName = ff.getPath();
                            baseName = ff.getName();
                            dirName = ff.getParentFile() == null ? null : ff.getParentFile().getPath();
                        } catch (Exception ex) {
                            Logger.getLogger(IOUtils.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        String uu;
                        try {
                            uu = new URL(sourceName).getFile();
                        } catch (MalformedURLException ex) {
                            return null;
                        }
                        int ii = uu.indexOf('?');
                        if (ii > 0) {
                            uu = uu.substring(0, ii);
                        }
                        File ff = new File(uu);
                        pathName = ff.getPath();
                        baseName = ff.getName();
                        dirName = ff.getParentFile() == null ? null : ff.getParentFile().getPath();

                    }
                } else /*if (file || (!url && !file && ! IOUtils.isURL(sourceName)))*/ {
                    File ff = new File(sourceName);
                    pathName = ff.getPath();
                    baseName = ff.getName();
                    dirName = ff.getParentFile() == null ? "" : ff.getParentFile().getPath();
                }

                String _baseName="";
                PathInfo pathInfo = new PathInfo();
                pathInfo.baseName=baseName;
                pathInfo.dirName=(dirName);
                pathInfo.pathName=(pathName);
                if (baseName != null) {
                    int dot = baseName.indexOf('.');
                    if (dot < 0) {
                        pathInfo.namePart=(baseName);
                        pathInfo.extensionPart=(null);
                    } else if (dot == 0) {
                        pathInfo.namePart=("");
                        pathInfo.extensionPart=(baseName);
                    } else if (dot == baseName.length() - 1) {
                        pathInfo.namePart=(baseName);
                        pathInfo.extensionPart=("");
                    } else {
                        pathInfo.namePart=(baseName.substring(0, dot));
                        pathInfo.extensionPart=(baseName.substring(dot + 1));
                    }
                }
                return pathInfo;
            }
        }
        return null;
    }

}
