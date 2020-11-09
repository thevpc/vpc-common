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
