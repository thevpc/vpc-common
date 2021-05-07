/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
 *
 * <br>
 *
 * Copyright [2020] [thevpc] Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <br> ====================================================================
 */
package net.thevpc.common.swing.panel;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import net.thevpc.common.swing.image.ImageUtils;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com) %creationtime 13 juil. 2006
 * 22:14:21
 */
public class JImageBackgroundPanel extends JPanel {

    public enum DisplayMode {
        IMAGE_SIZE,
        IMAGE_CENTER,
        IMAGE_STRETCH,
        IMAGE_WEST,
        IMAGE_EAST,
        IMAGE_NORTH,
        IMAGE_SOUTH,
        IMAGE_TILE
    }

    private ImageIcon image;
    private DisplayMode displayMode = DisplayMode.IMAGE_STRETCH;
    private Dimension imageSize;
    private Dimension lastStretchDimension;
    private Image lastStretchImage;

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

    public void setBackgroundImageURL(URL imageURL) {
        setBackgroundImage(new ImageIcon(imageURL));
    }

    public void setBackgroundImage(ImageIcon image) {
        setOpaque(false);
        invalidateCache();
        this.image = image;
        imageSize = (image == null) ? null : new Dimension(image.getIconWidth(), image.getIconHeight());
    }

    public DisplayMode getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(DisplayMode displayMode) {
        invalidateCache();
        this.displayMode = displayMode == null ? DisplayMode.IMAGE_STRETCH : displayMode;
        repaint();
    }

    private void invalidateCache() {
        this.lastStretchDimension = null;
        this.lastStretchImage = null;
    }

    @Override
    public Dimension getPreferredSize() {
        if (image != null && displayMode == DisplayMode.IMAGE_SIZE) {
            if (isPreferredSizeSet()) {
                return super.getPreferredSize();
            } else {
                return new Dimension(image.getIconWidth(), image.getIconHeight());
            }
        } else {
            return super.getPreferredSize();
        }
    }

    @Override
    public int getWidth() {
        return super.getWidth();
    }

    @Override
    public int getHeight() {
        return super.getHeight();
    }

    @Override
    public void paint(Graphics g) {
        if (image != null) {
            switch (displayMode) {
                case IMAGE_CENTER: {
                    Dimension s = getSize();
                    int x = (int) (s.getWidth() / 2 - imageSize.width / 2);
                    int y = (int) (s.getHeight() / 2 - imageSize.height / 2);
                    image.paintIcon(this, g, x, y);
                    break;
                }
                case IMAGE_WEST: {
                    Dimension s = getSize();
                    int x = 0;
                    int y = (int) (s.getHeight() / 2 - imageSize.height / 2);
                    image.paintIcon(this, g, x, y);
                    break;
                }
                case IMAGE_NORTH: {
                    Dimension s = getSize();
                    int x = (int) (s.getWidth() / 2 - imageSize.width / 2);
                    int y = 0;
                    image.paintIcon(this, g, x, y);
                    break;
                }
                case IMAGE_STRETCH: {
                    Dimension s = getSize();
                    if (lastStretchDimension == null || !s.equals(lastStretchDimension)) {
                        lastStretchDimension = s;
                        lastStretchImage = ImageUtils.getFixedSizeImage(image.getImage(), (int) s.getWidth(), (int) s.getHeight());
                    }
                    g.drawImage(lastStretchImage, 0, 0, this);
                    break;
                }
                default: {
                    g.drawImage(image.getImage(), 0, 0, this);
                }
            }
        }
        super.paint(g);
    }

    public Dimension getImageSize() {
        return imageSize;
    }

}
