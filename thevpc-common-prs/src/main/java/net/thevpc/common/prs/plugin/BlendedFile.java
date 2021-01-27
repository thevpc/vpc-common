package net.thevpc.common.prs.plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BlendedFile {
    private static String FOLDER_INFO_NAME = ".folder-info";
    public File userFile;
    public File systemFile;

    private static class FolderInfo {
        private File userFolder;
        private Set<String> deleted = new HashSet<String>();

        private FolderInfo(File userFolder) {
            this.userFolder = userFolder;
            load();
        }

        public boolean isDeleted(String name) {
            return deleted.contains(name);
        }

        public void setDeleted(String name, boolean de) {
            if (de) {
                deleted.add(name);
            } else {
                deleted.remove(name);
            }
            store();
        }

        public void store() {
            if (userFolder.exists()) {
                try {
                    File file = new File(userFolder, FOLDER_INFO_NAME);
                    PrintStream out = null;
                    try {
                        out = new PrintStream(file);
                        out.println("[DELETED]");
                        for (String d : deleted) {
                            out.println(d);
                        }
                    } finally {
                        if (out != null) {
                            out.close();
                        }
                    }
                } catch (Exception e) {
                    //ignore
                }
            }
        }

        public void load() {
            deleted.clear();
            if (userFolder.exists()) {
                try {
                    File file = new File(userFolder, ".folder-info");
                    BufferedReader in = null;
                    try {
                        in = new BufferedReader(new FileReader(file));
                        String line = null;
                        boolean deletedFlag = false;
                        while ((line = in.readLine()) != null) {
                            if (line.trim().equals("[DELETED]")) {
                                deletedFlag = true;
                            } else if (deletedFlag && line.trim().length() > 0 && !line.startsWith("#")) {
                                deleted.add(line.trim());
                            }
                        }
                    } finally {
                        if (in != null) {
                            in.close();
                        }
                    }
                } catch (Exception e) {
                    //ignore
                }
            }
        }
    }

    public BlendedFile(File userFile, File systemFile) {
        this.userFile=userFile;
        this.systemFile = systemFile;
        if(userFile==null || systemFile==null){
            throw new IllegalArgumentException("User File and System File should not be null");
        }
    }

    public File getSystemFile() {
        return systemFile;
    }

    public BlendedFile[] listFiles() {
        ArrayList<BlendedFile> files = new ArrayList<BlendedFile>();
        File[] ufiles = userFile.listFiles();
        File[] sfiles = isValidSystemFile() ? getSystemFile().listFiles():null;
        Set<String> visited = new HashSet<String>();
        if (ufiles != null) {
            for (File file : ufiles) {
                if (!file.getName().equals(FOLDER_INFO_NAME)) {
                    if (file.isDirectory()) {
                        files.add(new BlendedFile(file, new File(getSystemFile(), file.getName())));
                        visited.add(file.getName());
                    } else {
                        files.add(new BlendedFile(file, new File(getSystemFile(), file.getName())));
                        visited.add(file.getName());
                    }
                }
            }
        }
        if (sfiles != null) {
            for (File file : sfiles) {
                if (!visited.contains(file.getName())) {
                    files.add(new BlendedFile(new File(getUserFile(), file.getName()),file));
                    visited.add(file.getName());
                }
            }
        }
        return files.toArray(new BlendedFile[files.size()]);
    }

    public BlendedFile[] listFiles(FileFilter f) {
        ArrayList<BlendedFile> files = new ArrayList<BlendedFile>();
        for (BlendedFile file : listFiles()) {
            if (f.accept(file.getUserFile())) {
                files.add(file);
            }
        }
        return files.toArray(new BlendedFile[files.size()]);
    }

    public boolean delete() {
        if (exists()) {
            if(getUserFile().exists()){
                if(getUserFile().delete()){
                    if(isValidSystemFile()){
                        FolderInfo i=new FolderInfo(getUserFile().getParentFile());
                        i.setDeleted(getName(),true);
                    }
                    return true;
                }
                return false;
            }else{
                if(isValidSystemFile()){
                    FolderInfo i=new FolderInfo(getUserFile().getParentFile());
                    i.setDeleted(getName(),true);
                    return true;
                }
            }
        }
        return false;
    }

    public String getName(){
        return getUserFile().getName();
    }

    public BlendedFile getParentAppFile(){
        File up = userFile.getParentFile();
        File sp = systemFile.getParentFile();
        if(up==null || sp==null){
            return null;
        }
        return new BlendedFile(
                up,
                sp
        );
    }

    public boolean exists(){
        return userFile.exists() || systemFile.exists();
    }

//    public File getFile(){
//        if(userFile!=null){
//            return userFile;
//        }
//        if(systemFile!=null){
//            return systemFile;
//        }
//        return userFile;
//    }

    public File getUserFile() {
        return userFile;
    }

    public boolean mkdirs() {
        return getUserFile().mkdirs();
    }

    public boolean mkdir() {
        return getUserFile().mkdir();
    }

    public File getFile(){
        if(userFile.exists()){
            return userFile;
        }
        if(systemFile.exists()){
            return systemFile;
        }
        return userFile;
    }
    private boolean isValidSystemFile(){
        return systemFile.exists() && !systemFile.equals(userFile);
    }
}
