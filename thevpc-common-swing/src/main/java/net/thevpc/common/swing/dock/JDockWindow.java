package net.thevpc.common.swing.dock;

import net.thevpc.common.swing.icon.EmptyIcon;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class JDockWindow extends JDockWindowBase {
    private JComponent content;
    private JComponent contentReplace;
//    private JComponent header;
//    private JComponent footer;

    public JDockWindow() {
        super(new BorderLayout());
        setBorder(null);
    }

    public JDockWindow(String id, String title, Icon icon, JComponent component, boolean closable) {
        this();
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.closable = closable;
        setComponent(component);
    }

//    public JComponent getHeader() {
//        return header;
//    }
//
//    public JDockWindow setHeader(JComponent header) {
//        this.header = header;
//        return this;
//    }
//
//    public JComponent getFooter() {
//        return footer;
//    }
//
//    public JDockWindow setFooter(JComponent footer) {
//        this.footer = footer;
//        return this;
//    }

    @Override
    public JDockWindowBase setTitle(String title) {
        super.setTitle(title);
        JInternalFrame ji = (JInternalFrame) this.contentReplace;
        if(ji!=null){
            ji.setTitle(title);
        }
        return this;
    }

    @Override
    public JDockWindowBase setIcon(Icon icon) {
        super.setIcon(icon);
        JInternalFrame ji = (JInternalFrame) this.contentReplace;
        if(ji!=null){
            ji.setFrameIcon(icon);
        }
        return this;
    }

    @Override
    public JDockWindowBase setComponent(JComponent component) {
        if (this.contentReplace != null) {
            this.remove(this.contentReplace);
        }
        this.content = component;
        if (content != null) {
            if (contentReplace == null) {
                JInternalFrame ji = new JInternalFrame(){
                    @Override
                    public void setBorder(Border border) {
                        super.setBorder(null);
                    }

                    @Override
                    public void setFrameIcon(Icon icon) {
                        if(icon==null){
                            super.setFrameIcon(new EmptyIcon(16,16));
                        }else{
                            super.setFrameIcon(icon);
                        }
                    }
                };
                ji.setBorder(null);
                ji.setFrameIcon(null);
                ji.setTitle(title);
                contentReplace = ji;
                ji.setVisible(true);
                ji.setFrameIcon(null);
                ji.getContentPane().add(content);
                add(contentReplace, BorderLayout.CENTER);
            } else {
                ((JInternalFrame) contentReplace).getContentPane().removeAll();
                ((JInternalFrame) contentReplace).getContentPane().add(content);
            }
        } else {
            this.remove(contentReplace);
            contentReplace = null;
        }
        return this;
    }


    @Override
    public JDockWindowBase setClosable(boolean closable) {
        super.setClosable(closable);
        JInternalFrame ji = (JInternalFrame) this.contentReplace;
        if(ji!=null){
            ji.setClosable(closable);
        }
        return this;
    }
    @Override
    public void updateUI() {
        super.updateUI();
        if(contentReplace!=null){
            contentReplace.updateUI();
//            ((JComponent)((JInternalFrame)contentReplace).getContentPane()).updateUI();
        }
        if(content!=null){
//            if(content instanceof JButton){
//                System.out.println("updateUI "+((JButton) content).getText());
//            }
            content.updateUI();
//            content.invalidate();
//            content.revalidate();
//            content.repaint();
        }
    }

}
