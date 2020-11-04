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
package net.thevpc.common.swings;

import javax.swing.*;
import java.awt.*;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime  13 juil. 2006 22:14:21
 */
public class JImageBackgroundPanel extends JPanel {
    public static final int IMAGE_SIZE=0;
    public static final int IMAGE_CENTER=1;
    public static final int IMAGE_WEST=2;
    public static final int IMAGE_EAST=3;
    public static final int IMAGE_NORTH=4;
    public static final int IMAGE_SOUTH=5;
    public static final int IMAGE_TILE=6;

    private ImageIcon image;
    private int display_mode;
    private Dimension imageSize;

    public JImageBackgroundPanel() {
    }

    public JImageBackgroundPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public JImageBackgroundPanel(LayoutManager layout) {
        super(layout);
    }

    public JImageBackgroundPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public void setBackgroundImageURL(String imageURL,int display_mode){
        setBackgroundImage(new ImageIcon(JImageBackgroundPanel.class.getResource(imageURL)),display_mode);
    }

    public void setBackgroundImage(ImageIcon image,int display_mode){
        setOpaque(false);
        this.image=image;
        this.display_mode=display_mode;
        imageSize=(image==null)? null : new Dimension(image.getIconWidth(),image.getIconHeight());
    }

    public Dimension getPreferredSize() {
        if(image !=null && display_mode==IMAGE_SIZE){
            if(isPreferredSizeSet()){
                return super.getPreferredSize();
            }else{
                return new Dimension(image.getIconWidth(),image.getIconHeight());
            }
        }else{
            return super.getPreferredSize();
        }
    }

    public int getWidth() {
        return super.getWidth();
    }

    public int getHeight() {
        return super.getHeight();
    }

    public void paint(Graphics g) {
        if(image!=null){
            image.paintIcon(this,g,0,0);
        }
        super.paint(g);
    }

	public Dimension getImageSize() {
		return imageSize;
	}

}
