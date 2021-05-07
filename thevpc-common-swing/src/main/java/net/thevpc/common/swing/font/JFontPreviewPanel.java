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
// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3)
// Source File Name:   JFontPreviewPanel.java

package net.thevpc.common.swing.font;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime  13 juil. 2006 22:14:21
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
