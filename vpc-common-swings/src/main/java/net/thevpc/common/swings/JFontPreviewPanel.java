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
// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3)
// Source File Name:   JFontPreviewPanel.java

package net.thevpc.common.swings;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime  13 juil. 2006 22:14:21
 */
public class JFontPreviewPanel extends JPanel {

    public JFontPreviewPanel(Font f) {
        setFont(f);
        setBorder(new TitledBorder(new EtchedBorder(1), "Preview"));
    }

    public void setFont(Font f) {
        font = f;
        repaint();
    }

    public void update(Graphics g) {
        paintComponent(g);
        paintBorder(g);
    }

    public void paintComponent(Graphics g) {
        Image osi = createImage(getSize().width, getSize().height);
        Graphics osg = osi.getGraphics();
        osg.setFont(font);
        Rectangle2D bounds = font.getStringBounds(font.getFontName(), 0, font.getFontName().length(), new FontRenderContext(null, true, false));
        int width = (new Double(bounds.getWidth())).intValue();
        int height = (new Double(bounds.getHeight())).intValue();
        osg.drawString(font.getFontName(), 5, (getSize().height - height) / 2 + height);
        g.drawImage(osi, 0, 0, this);
    }

    public Dimension getPreferredSize() {
        return new Dimension(getSize().width, 75);
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    private Font font;
}
