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

package net.vpc.common.swings.plaf;

import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import javax.swing.plaf.metal.OceanTheme;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author vpc
 */
public class MetalPlafHandler implements PlafHandler {
    public static final String METAL = "javax.swing.plaf.metal.MetalLookAndFeel";
    private Vector<File> extraFolders = new Vector<File>();

    public MetalPlafHandler() {
    }

    public void addFolder(File folder) {
        extraFolders.add(folder);
    }

    public int accept(String plaf) {
        return (plaf != null && (plaf.equals(METAL) || plaf.startsWith(METAL + ":"))) ? 10 : -1;
    }

    public void apply(PlafItem plafItem) throws ClassNotFoundException,
            InstantiationException,
            IllegalAccessException,
            UnsupportedLookAndFeelException {
        Object oo = plafItem.getTheme();
        if (oo instanceof Class) {
            try {
                oo = ((Class) oo).newInstance();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        } else if (oo instanceof File) {
            try {
                oo = new DefaultMetalThemeProperties((File) oo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (oo == null) {
            oo = new OceanTheme();
        }
        MetalLookAndFeel.setCurrentTheme((MetalTheme) oo);
        UIManager.setLookAndFeel(plafItem.getPlaf());
    }

    public PlafItem getPlafItem(String item) {
        if (METAL.equals(item)) {
            return new PlafItem(METAL + ":class:" + OceanTheme.class.getName(), METAL, OceanTheme.class, "Ocean");
        }
        item = item.substring(METAL.length() + 1);
        if (item.startsWith("class:")) {
            try {
                return new PlafItem(item, METAL, Class.forName(item.substring(6)), Class.forName(item.substring(6)).getSimpleName());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MetalPlafHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (item.startsWith("file:")) {
            return new PlafItem(item, METAL, new File(item.substring(5)), new File(item.substring(5)).getName());
        }
        throw new UnsupportedOperationException("Not supported");
    }


    public void install() {
        //
    }

    public PlafItem[] loadItems() {
        ArrayList<PlafItem> all = new ArrayList<PlafItem>();
        all.add(new PlafItem(METAL + ":class:" + DefaultMetalTheme.class.getName(), METAL, DefaultMetalTheme.class, "Default"));
        all.add(new PlafItem(METAL + ":class:" + OceanTheme.class.getName(), METAL, OceanTheme.class, "Ocean"));
        File def = new File(System.getProperty("user.home") + "/.java-apps/plaf-config/metal");
        ArrayList<File> allFiles = new ArrayList<File>();
        allFiles.add(def);
        allFiles.addAll(extraFolders);
        Comparator<File> comp = new Comparator<File>() {
            public int compare(File o1, File o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        };
        for (File someFile : allFiles) {
            File[] themeFiles = someFile.listFiles();
            if (themeFiles != null) {
                Arrays.sort(themeFiles, comp);
                for (File file : themeFiles) {
                    if (file.getName().toLowerCase().endsWith(".theme")) {
                        try {
                            all.add(new PlafItem(METAL + ":file:" + file.getCanonicalPath(), METAL, file, file.getName().substring(0, file.getName().length() - 6)));
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                }
            }
        }


        return all.toArray(new PlafItem[all.size()]);
    }
}
