/**
 * ====================================================================
 *                        vpc-swingext library
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
package net.vpc.common.swings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 9 nov. 2006 11:55:40
 */
public class RecentFilesPropertiesModel implements RecentFilesModel {
    private File file;
    private String key;
    private boolean xml = true;
    private int maxRecentFiles = 10;

    public RecentFilesPropertiesModel(File file) {
        this(file, "RecentFiles");
    }

    public RecentFilesPropertiesModel(File file, String key) {
        this.file = file;
        this.key = key;
        xml = file.getName().toLowerCase().endsWith(".xml");
    }


    public void removeFile(File file) {
        try {
            File p = file.getCanonicalFile();
            File[] rf = getFiles();
            ArrayList<File> rfv = new ArrayList<File>(rf.length + 1);
            rfv.addAll(Arrays.asList(rf));
            rfv.remove(p);
            while (rfv.size() > maxRecentFiles) {
                rfv.remove(rfv.size() - 1);
            }
            rf = rfv.toArray(new File[rfv.size()]);
            setFiles(rf);
        } catch (IOException e) {
            throw new IllegalArgumentException("Bad file " + file.getPath());
        }
    }

    public void addFile(File file) {
        try {
            File p = file.getCanonicalFile();
            File[] rf = getFiles();
            ArrayList<File> rfv = new ArrayList<File>(rf.length + 1);
            rfv.addAll(Arrays.asList(rf));
            rfv.remove(p);
            rfv.add(0, p);
            while (rfv.size() > maxRecentFiles) {
                rfv.remove(rfv.size() - 1);
            }
            rf = rfv.toArray(new File[rfv.size()]);
            setFiles(rf);
        } catch (IOException e) {
            throw new IllegalArgumentException("Bad file " + file.getPath());
        }
    }

    public void setFiles(File[] files) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            Properties properties = new Properties();
            if (file.exists()) {
                try {
                    fis = new FileInputStream(file);
                    if (xml) {
                        properties.loadFromXML(fis);
                    } else {
                        properties.load(fis);
                    }
                } finally {
                    if (fis != null) {
                        fis.close();
                    }
                }
            }
            StringBuilder sb = new StringBuilder();
            for (File file1 : files) {
                if (sb.length() > 0) {
                    sb.append(":");
                }
                sb.append(file1.getCanonicalPath());
            }
            properties.put(key, sb.toString());
            if(file.getParentFile()!=null){
                file.getParentFile().mkdirs();
            }
            try {
                fos = new FileOutputStream(file);
                if (xml) {
                    properties.storeToXML(fos, "");
                } else {
                    properties.store(fos, "");
                }
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public File[] getFiles() {
        FileInputStream fis = null;
        try {
            Properties properties = new Properties();
            if (file.exists()) {
                try {
                    fis = new FileInputStream(file);
                    if (xml) {
                        properties.loadFromXML(fis);
                    } else {
                        properties.load(fis);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    //any exception dont worry
                } finally {
                    if (fis != null) {
                        fis.close();
                    }
                }
            }
            String all = properties.getProperty(key);
            if (all == null) {
                all = "";
            }
            StringTokenizer stringTokenizer = new StringTokenizer(all, ":");
            ArrayList<File> all2 = new ArrayList<File>();
            while (stringTokenizer.hasMoreTokens()) {
                all2.add(new File(stringTokenizer.nextToken()));
            }
            return all2.toArray(new File[all2.size()]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public int getMaxRecentFiles() {
        return maxRecentFiles;
    }

    public void setMaxRecentFiles(int maxRecentFiles) {
        this.maxRecentFiles = maxRecentFiles;
    }
}
