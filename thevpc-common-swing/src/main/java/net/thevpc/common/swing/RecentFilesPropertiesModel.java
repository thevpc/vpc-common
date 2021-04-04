/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
 *
 * <br>
 *
 * Copyright [2020] [thevpc] Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <br> ====================================================================
 */
package net.thevpc.common.swing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com) %creationtime 9 nov. 2006
 * 11:55:40
 */
public class RecentFilesPropertiesModel implements RecentFilesModel {

    private String file;
    private String key;
    private boolean xml = true;
    private int maxRecentFiles = 10;

    public RecentFilesPropertiesModel(String file) {
        this(file, "RecentFiles");
    }

    public RecentFilesPropertiesModel(String file, String key) {
        this.file = file;
        this.key = key;
        xml = new File(file).getName().toLowerCase().endsWith(".xml");
    }

    public void removeFile(String file) {
        try {
            String p = new File(file).getCanonicalFile().getPath();
            List<String> rf = getFiles();
            ArrayList<String> rfv = new ArrayList<String>(rf.size() + 1);
            rfv.addAll(rf);
            rfv.remove(p);
            while (rfv.size() > maxRecentFiles) {
                rfv.remove(rfv.size() - 1);
            }
            rf = rfv;
            setFiles(rf);
        } catch (IOException e) {
            throw new IllegalArgumentException("Bad file " + file);
        }
    }

    public void addFile(String file) {
        try {
            String p = new File(file).getCanonicalFile().getPath();
            List<String> rf = getFiles();
            ArrayList<String> rfv = new ArrayList<String>(rf.size() + 1);
            rfv.addAll(rf);
            rfv.remove(p);
            rfv.add(0, p);
            while (rfv.size() > maxRecentFiles) {
                rfv.remove(rfv.size() - 1);
            }
            rf = rfv;
            setFiles(rf);
        } catch (IOException e) {
            throw new IllegalArgumentException("Bad file " + file);
        }
    }

    public void setFiles(List<String> files) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            Properties properties = new Properties();
            if (new File(file).exists()) {
                try {
                    fis = new FileInputStream(new File(file));
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
            for (String file1 : files) {
                if (sb.length() > 0) {
                    sb.append(":");
                }
                sb.append(new File(file1).getCanonicalPath());
            }
            properties.put(key, sb.toString());
            if (new File(file).getParentFile() != null) {
                new File(file).getParentFile().mkdirs();
            }
            try {
                fos = new FileOutputStream(new File(file));
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

    public List<String> getFiles() {
        FileInputStream fis = null;
        try {
            Properties properties = new Properties();
            if (new File(file).exists()) {
                try {
                    fis = new FileInputStream(new File(file));
                    if (xml) {
                        properties.loadFromXML(fis);
                    } else {
                        properties.load(fis);
                    }
                } catch (Exception e) {
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
            ArrayList<String> all2 = new ArrayList<String>();
            while (stringTokenizer.hasMoreTokens()) {
                all2.add(stringTokenizer.nextToken());
            }
            return all2;
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
