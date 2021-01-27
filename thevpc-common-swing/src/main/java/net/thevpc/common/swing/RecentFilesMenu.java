/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
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
package net.thevpc.common.swing;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class RecentFilesMenu extends JMenu {
    private ActionListener fileSelectionListener;
    public static final String SELECTED_FILE = "SELECTED_FILE";
    private RecentFilesModel model;
    private Vector<FileSelectedListener> listeners;

    public RecentFilesMenu() {
        this("Fichiers recents", new DefaultRecentFilesModel());
    }

    public RecentFilesMenu(String title, RecentFilesModel model) {
        super(title);
        setRecentFilesModel(model);
        this.fileSelectionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JMenuItem jmi = (JMenuItem) e.getSource();
                File file = (File) jmi.getClientProperty("file");
                processFileSelected(file);
            }
        };
    }

    protected void processFileSelected(File file) {
        addFile(file);
        firePropertyChange(SELECTED_FILE, null, file);
        if (listeners != null) {
            FileEvent event = new FileEvent(this, file);
            for (FileSelectedListener listener : listeners) {
                listener.fileSelected(event);
            }
        }
    }

    public void addFileSelectedListener(FileSelectedListener listener) {
        if (listeners == null) {
            listeners = new Vector<FileSelectedListener>();
        }
        listeners.add(listener);
    }

    public void addFile(File file) {
        getRecentFilesModel().addFile(file);
    }

    public JPopupMenu getPopupMenu() {
        removeAll();
        File[] children = getRecentFilesModel().getFiles();
        if (children.length == 0) {
            JMenuItem menu = new JMenuItem("<EMPTY>");
            menu.setEnabled(false);
            add(menu);
        } else {
            for (int i = 0; i < children.length; i++) {
                JMenuItem item = createMenuItem(this, i, children[i]);
                if (item != null) {
                    item.addActionListener(fileSelectionListener);
                    add(item);
                }
            }
        }
        return super.getPopupMenu();
    }

    public JMenuItem createMenuItem(RecentFilesMenu parent, int index, File file) {
        String fileName;
        try {
            fileName = file.getCanonicalPath();
        } catch (IOException e) {
            fileName = file.getPath();
        }
        if (fileName.indexOf('.') > 0) {
            fileName = fileName.substring(0, fileName.indexOf('.'));
        }
        fileName = (index + 1) + ": " + fileName;
        JMenuItem mi = new JMenuItem(fileName);
        mi.putClientProperty("file", file);
        mi.putClientProperty("RecentFilesMenu", parent);
        return mi;
    }

    public int getMaxRecentFiles() {
        return getRecentFilesModel().getMaxRecentFiles();
    }

    public void setMaxRecentFiles(int maxRecentFiles) {
        getRecentFilesModel().setMaxRecentFiles(maxRecentFiles);
    }


    public RecentFilesModel getRecentFilesModel() {
        return model;
    }

    public void setRecentFilesModel(RecentFilesModel model) {
        this.model = model;
    }
}
