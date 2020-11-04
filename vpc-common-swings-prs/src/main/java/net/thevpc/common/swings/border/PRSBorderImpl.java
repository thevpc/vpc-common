/**
 * ====================================================================
 *                        vpc-prs library
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
package net.thevpc.common.swings.border;

import net.thevpc.common.prs.iconset.IconSet;
import net.thevpc.common.prs.messageset.MessageSet;

import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 27 août 2007 13:13:36
 */
public class PRSBorderImpl implements PRSBorder{
    private String id;
    private TitledBorder titledBorder;

    public PRSBorderImpl(String id, TitledBorder titledBorder) {
        this.id = id;
        this.titledBorder = titledBorder;
    }

    public void update(MessageSet messageSet) {
        titledBorder.setTitle(messageSet.get(id, true));
    }


    public void update(IconSet iconSet) {
        // what to do?
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        titledBorder.paintBorder(c, g, x, y, width, height);
    }

    public Insets getBorderInsets(Component c) {
        return titledBorder.getBorderInsets(c);
    }

    public boolean isBorderOpaque() {
        return titledBorder.isBorderOpaque();
    }
}
