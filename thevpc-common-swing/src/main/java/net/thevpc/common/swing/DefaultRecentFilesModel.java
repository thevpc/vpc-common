/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author thevpc
 */
public class DefaultRecentFilesModel implements RecentFilesModel {

    private ArrayList<File> files = new ArrayList<File>();
    private int max = 0;

    private void ensureSize() {
        if (max > 0) {
            while (files.size() > max) {
                files.remove(files.size() - 1);
            }
        }
    }

    @Override
    public File[] getFiles() {
        return files.toArray(new File[files.size()]);
    }

    @Override
    public void setFiles(File[] files) {
        this.files.clear();
        if (files != null) {
            this.files.addAll(Arrays.asList(files));
            ensureSize();
        }
    }

    @Override
    public void removeFile(File file) {
        this.files.remove(file);
    }

    @Override
    public void addFile(File file) {
        files.remove(file);
        files.add(0, file);
    }

    @Override
    public int getMaxRecentFiles() {
        return max;
    }

    @Override
    public void setMaxRecentFiles(int maxRecentFiles) {
        this.max=maxRecentFiles;
        ensureSize();
    }
}
