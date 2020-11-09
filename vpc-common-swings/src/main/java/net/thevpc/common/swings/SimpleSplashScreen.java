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
import java.awt.event.*;
import java.util.Arrays;
import java.util.TimerTask;
import java.util.Vector;

public class SimpleSplashScreen extends JWindow{
    private enum Status {
        START, HORIZONTAL_INIT, HORIZONTAL, VERTICAL_INIT, VERTICAL, END
    }

    public enum Type{
        ERROR, INFO, SUCCESS, WARNING, HTML
    }

    public static class Message{
        private Type type;
        private String text;

        public Message(Type type, String text) {
            if(text==null){
                text="";
            }
            this.type = type;
            this.text = text;
        }
    }

    private Comp component;
    private Dimension dimension;
    private ImageIcon imageIcon;
    private Timer animationTimer;
    private final Vector<Message> messages = new Vector<Message>();
    private long timeout = -1;
    private boolean hideOnClick = false;
    private boolean verticalAnimation = false;
    private boolean verticalAnimationLoop = false;
    private boolean horizontalAnimation = true;

    private int currentX = 0;
    private int currentY = 0;
    private int animVerticalDelay = 50;
    private int animHorizontralDelay = 20;
    private int animXstep = 20;
    private int animYstep = 1;
    //    private int animHeight = 0;
    private int textXmin = 170;
    private int textXmax = 470;
    private int textYmin = 130;
    private int textYmax = 230;

    private int textY = 150;
    //    private int messageCarsCount = 50;
    private int textHeight;
    private JLabel renderer;
    private Status status = Status.HORIZONTAL;
    private Color foregroundColor = Color.BLUE.darker();
    private Color backgroundColor = Color.WHITE;
    private Color infoColor = Color.WHITE;
    private Color warnColor = Color.YELLOW;
    private Color errorColor = Color.RED;
    private Color successColor = Color.ORANGE;
    private float progress = 0f;
    private String progressText = null;


    private class Comp extends JComponent {
        boolean somethingShown = false;

        public Comp() {
            setBorder(BorderFactory.createEtchedBorder());
            setSize(dimension);
            setPreferredSize(dimension);
            setMinimumSize(dimension);
            setMaximumSize(dimension);
            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (hideOnClick) {
                        SimpleSplashScreen a=(SimpleSplashScreen)SwingUtilities.getAncestorOfClass(SimpleSplashScreen.class,Comp.this);
                        if(a!=null){
                            a.setVisible(false);
                        }
                    }
                }
            });
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imageIcon != null) {
                g.drawImage(imageIcon.getImage(), 0, 0, this);
            } else {
                g.setColor(backgroundColor == null ? Color.WHITE : backgroundColor);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            g.setColor(foregroundColor == null ? Color.BLACK : foregroundColor);
            FontMetrics fontMetrics = g.getFontMetrics();
            int h = fontMetrics.getHeight() + fontMetrics.getDescent() + fontMetrics.getAscent();
            textHeight = h;
            int xx = currentX;
            int yy = currentY;
            somethingShown = false;
//            g.drawRect(textXmin, textYmin, (textXmax - textXmin), (textYmax - textYmin) + textHeight);
            synchronized (messages) {
                for (Message message : messages) {
                    drawString(message, xx, yy, g);
                    yy += h;
                }
            }
            if (!somethingShown) {
//                verticalAnimationEnd();
            }
            Dimension s = getSize();
            g.setColor(Color.WHITE);
            g.fillRect(0, s.height - 5, s.width, 5);
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, s.height - 15, (int) (s.width ), 15);
            g.setColor(Color.DARK_GRAY.brighter());
            g.fillRect(0, s.height - 10, (int) (s.width ), 10);
            g.setColor(Color.DARK_GRAY.brighter().brighter());
            g.fillRect(0, s.height - 5, (int) (s.width ), 5);
            if (progress > 0) {
                g.setColor(Color.BLUE);
                g.fillRect(0, s.height - 5, (int) (s.width * progress), 5);
                g.setColor(Color.BLUE.brighter());
                g.fillRect(0, s.height - 3, (int) (s.width * progress), 3);
            }
            if(progressText!=null){
                g.setColor(Color.BLACK);
                drawString0(progressText, 10,s.height - 25, g);
            }
        }

        private void drawString(Message msg, int xx, int yy, Graphics g) {
            if (yy >= textYmin && yy <= textYmax) {
                if (msg == null) {
                    msg = new Message(Type.INFO,"");
                }
//                if (msg.length() > messageCarsCount) {
//                    int w = messageCarsCount / 2 - 2;
//                    msg = msg.substring(0, w) + "..." + msg.substring(msg.length() - w);
//                }
                JLabel label = getRenderer();
                switch (msg.type){
                    case INFO:{
                        Color c=infoColor==null?(foregroundColor==null?Color.WHITE:foregroundColor):infoColor;
                        String s="#" + Integer.toHexString(c.getRGB()).substring(2).toUpperCase();
                        label.setText("<html><p style=\"color: "+s+"\">"+msg.text+"</p></html>");
                        break;
                    }
                    case ERROR:{
                        Color c=errorColor==null?Color.RED:errorColor;
                        String s="#" + Integer.toHexString(c.getRGB()).substring(2).toUpperCase();
                        label.setText("<html><p style=\"color: "+s+"\">"+msg.text+"</p></html>");
                        break;
                    }
                    case WARNING:{
                        Color c=warnColor==null?Color.YELLOW:warnColor;
                        String s="#" + Integer.toHexString(c.getRGB()).substring(2).toUpperCase();
                        label.setText("<html><p style=\"color: "+s+"\">"+msg.text+"</p></html>");
                        break;
                    }
                    case SUCCESS:{
                        Color c=successColor==null?Color.ORANGE:successColor;
                        String s="#" + Integer.toHexString(c.getRGB()).substring(2).toUpperCase();
                        label.setText("<html><p style=\"color: "+s+"\">"+msg.text+"</p></html>");
                        break;
                    }
                    case HTML:{
                        label.setText("<html>"+msg.text+"</html>");
                        break;
                    }
                }
                label.setBounds(xx, yy, (textXmax - textXmin), textHeight);
                label.paint(g.create(xx, yy, (textXmax - textXmin), textHeight));
                somethingShown = true;
            }
        }
        
        private void drawString0(String msg, int xx, int yy, Graphics g) {
            if (msg == null) {
                msg = "";
            }
//                if (msg.length() > messageCarsCount) {
//                    int w = messageCarsCount / 2 - 2;
//                    msg = msg.substring(0, w) + "..." + msg.substring(msg.length() - w);
//                }
            JLabel label = getRenderer();

            label.setText(msg);
            label.setBounds(xx, yy, (textXmax - textXmin), textHeight);
            label.paint(g.create(xx, yy, (textXmax - textXmin), textHeight));
//            somethingShown = true;
        }

        public Dimension getImageDimension() {
            return dimension;
        }
    }

    public SimpleSplashScreen(ImageIcon image, Dimension preferredDimension) {
        dimension = preferredDimension;
        if (dimension == null) {
            if (imageIcon != null) {
                dimension = new Dimension(imageIcon == null ? 480 : imageIcon.getIconWidth(), imageIcon == null ? 350 : imageIcon.getIconHeight());
            } else {
                throw new IllegalArgumentException("Dimension is null!!");
            }
        }
        imageIcon = image;
        component = new Comp();
        setLayout(new BorderLayout());
        add(component, BorderLayout.CENTER);
        animationTimer = new Timer(animHorizontralDelay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doAnimation();
            }
        });
        renderer = new JLabel();
        addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {
                //do nothing
            }

            public void componentMoved(ComponentEvent e) {
                //do nothing
            }

            public void componentShown(ComponentEvent e) {
                pack();
                setLocationRelativeTo(null);
                setAlwaysOnTop(true);
                animateText();
                if (timeout > 0) {
                    java.util.Timer t = new java.util.Timer();
                    t.schedule(new TimerTask() {
                        public void run() {
                            hide();
                        }
                    }, timeout);
                }
                animationTimer.start();
            }

            public void componentHidden(ComponentEvent e) {
                //animationTimer.stop();
            }
        });
//        renderer.setBorder(BorderFactory.createEtchedBorder());
    }


    @Override
    public void setComponentOrientation(ComponentOrientation o) {
        super.setComponentOrientation(o);
        getRenderer().setComponentOrientation(o);
    }

    public JLabel getRenderer() {
        return renderer;
    }

    public void setRenderer(JLabel renderer) {
        this.renderer = renderer;
    }

    private void doAnimation() {
        if (!horizontalAnimation && !verticalAnimation) {
            currentX = textXmin;
            currentY = textY;
        } else if (horizontalAnimation && !verticalAnimation) {
            switch (status) {
                case START:
                case HORIZONTAL_INIT: {
                    currentX = -(textXmax - textXmin);
                    currentY = textY;
                    status = Status.HORIZONTAL;
                    break;
                }
                case HORIZONTAL: {
                    if (currentX < textXmin) {
                        currentX += animXstep;
                        if (currentX > textXmin) {
                            currentX = textXmin;
                        }
                    } else {
                        currentX = textXmin;
                        status = Status.END;
                    }
                    break;
                }
            }
        } else if (horizontalAnimation && verticalAnimation) {
            switch (status) {
                case START:
                case HORIZONTAL_INIT: {
                    animationTimer.setDelay(animHorizontralDelay);
                    currentX = -(textXmax - textXmin);
                    currentY = textY;
                    status = Status.HORIZONTAL;
                    break;
                }
                case HORIZONTAL: {
                    animationTimer.setDelay(animHorizontralDelay);
                    if (currentX < textXmin) {
                        currentX += animXstep;
                        if (currentX > textXmin) {
                            currentX = textXmin;
                        }
                    } else {
                        currentX = textXmin;
                        status = Status.VERTICAL_INIT;
                    }
                    break;
                }
                case VERTICAL_INIT: {
//                    currentX = textX;
                    currentY = textY;
                    status = Status.VERTICAL;
                    animationTimer.setDelay(animVerticalDelay);
                    break;
                }
                case VERTICAL: {
                    animationTimer.setDelay(animVerticalDelay);
                    currentY -= animYstep;
                    break;
                }
            }
        }
//        verticalAnimationEnd();
        invalidate();
        repaint();
    }

    public void animateText() {
        status = Status.START;
        animationTimer.start();
    }


    public void clearMessages() {
        messages.clear();
        invalidate();
        repaint();
    }

    public void setMessageAt(int index, Message msg) {
        messages.set(index, msg);
        invalidate();
        repaint();
    }

    public Message geMessageAt(int index) {
        return messages.get(index);
    }

    public void setMessages(Message[] msg) {
        synchronized (messages) {
            messages.clear();
            messages.addAll(Arrays.asList(msg));
        }
    }

    public void addMessage(Message msg) {
        messages.add(msg);
        invalidate();
        repaint();
    }


    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public boolean isHideOnClick() {
        return hideOnClick;
    }

    public void setHideOnClick(boolean hideOnClick) {
        this.hideOnClick = hideOnClick;
    }

    public boolean isVerticalAnimation() {
        return verticalAnimation;
    }

    public void setVerticalAnimation(boolean verticalAnimation) {
        this.verticalAnimation = verticalAnimation;
    }

    public boolean isHorizontalAnimation() {
        return horizontalAnimation;
    }

    public void setHorizontalAnimation(boolean horizontalAnimation) {
        this.horizontalAnimation = horizontalAnimation;
    }

    public boolean isVerticalAnimationLoop() {
        return verticalAnimationLoop;
    }

    public void setVerticalAnimationLoop(boolean verticalAnimationLoop) {
        this.verticalAnimationLoop = verticalAnimationLoop;
    }

    public int getTextYmin() {
        return textYmin;
    }

    public void setTextBounds(int x, int y, int width, int height) {
        setTextXmin(x);
        setTextXmax(x + width);
        setTextYmin(y);
        setTextYmax(y + height);
    }

    public void setTextBounds(Rectangle r) {
        setTextXmin((int) r.getMinX());
        setTextXmax((int) r.getMaxX());
        setTextYmin((int) r.getMinY());
        setTextYmax((int) r.getMaxY());
    }


    public int getTextXmin() {
        return textXmin;
    }

    public void setTextXmin(int textXmin) {
        this.textXmin = textXmin;
    }

    public int getTextXmax() {
        return textXmax;
    }

    public void setTextXmax(int textXmax) {
        this.textXmax = textXmax;
    }

    public void setTextYmin(int textYmin) {
        this.textYmin = textYmin;
    }

    public int getTextYmax() {
        return textYmax;
    }

    public void setTextYmax(int textYmax) {
        this.textYmax = textYmax;
    }

    public int getTextY() {
        return textY;
    }

    public void setTextY(int textY) {
        this.textY = textY;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgressText(String progressText) {
        this.progressText = progressText;
        repaint();
    }

    public void setProgress(float progress) {
        this.progress = progress;
        repaint();
    }

    public void progressStart(String e) {
        this.progress = 0f;
        this.progressText = e;
    }

    public void progressUpdate(String e) {
        this.progress = 0f;
        this.progressText = e;
    }

    public void progressEnd(String e) {
        this.progress = 1f;
        this.progressText = e;
    }

    public Color getErrorColor() {
        return errorColor;
    }

    public void setErrorColor(Color errorColor) {
        this.errorColor = errorColor;
    }

    public Color getInfoColor() {
        return infoColor;
    }

    public void setInfoColor(Color infoColor) {
        this.infoColor = infoColor;
    }

    public Color getSuccessColor() {
        return successColor;
    }

    public void setSuccessColor(Color successColor) {
        this.successColor = successColor;
    }

    public Color getWarnColor() {
        return warnColor;
    }

    public void setWarnColor(Color warnColor) {
        this.warnColor = warnColor;
    }
    
    
}
