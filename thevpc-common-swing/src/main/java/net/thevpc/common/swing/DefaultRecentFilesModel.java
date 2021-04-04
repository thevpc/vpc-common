/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author thevpc
 */
public class DefaultRecentFilesModel implements RecentFilesModel {

    private ArrayList<String> files = new ArrayList<String>();
    private int max = 0;

    private void ensureSize() {
        if (max > 0) {
            while (files.size() > max) {
                files.remove(files.size() - 1);
            }
        }
    }

    @Override
    public List<String> getFiles() {
        return files;
    }

    @Override
    public void setFiles(List<String> files) {
        this.files.clear();
        if (files != null) {
            this.files.addAll(files);
            ensureSize();
        }
    }

    @Override
    public void removeFile(String file) {
        this.files.remove(file);
    }

    @Override
    public void addFile(String file) {
        files.remove(file);
        files.add(0, file);
    }

    @Override
    public int getMaxRecentFiles() {
        return max;
    }

    @Override
    public void setMaxRecentFiles(int maxRecentFiles) {
        this.max = maxRecentFiles;
        ensureSize();
    }
}
