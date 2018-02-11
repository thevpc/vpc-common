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

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 8 févr. 2007 20:30:17
 */
public class JComboboxFontNames extends JComboBox {
    private boolean  nullable;
    public JComboboxFontNames(boolean nullable) {
        this.nullable=nullable;
        Font[] fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
//        Vector<Font> fonts = new Vector<Font>(1, 1);
        Vector<String> names = new Vector<String>(1, 1);
        if(nullable){
            names.add(null);
        }
        for (Font aFontList : fontList) {
            String fontName = aFontList.getFamily();
            if (!names.contains(fontName)) {
                names.addElement(fontName);
//                fonts.addElement(aFontList);
            }
        }
        setModel(new DefaultComboBoxModel(names));
    }

    public boolean isNullable() {
        return nullable;
    }

    public String getFontName(){
        return (String) getSelectedItem();
    }
}
