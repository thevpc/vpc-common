package net.thevpc.common.swing.frame;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

public class JInternalFrameHelper {
    private JInternalFrame frame;

    public JInternalFrameHelper(JInternalFrame frame) {
        this.frame = frame;
    }
    public JInternalFrame getFrame() {
        return frame;
    }


    public boolean setIcon(boolean value) {
        if (frame.isIconifiable()) {
            if (frame.isIcon() != value) {
                try {
                    frame.setIcon(true);
                } catch (PropertyVetoException e) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean setSelected(boolean value) {
//        if(frame.isIconifiable()){
        if (frame.isSelected() != value) {
            try {
                frame.setSelected(value);
            } catch (PropertyVetoException e) {
                return false;
            }
        }
        return true;
//        }
//        return false;
    }

    public boolean setClosed(boolean value) {
        if (frame!=null && frame.isClosable()) {
            if (frame.isClosed() != value) {
                try {
                    frame.setClosed(true);
                } catch (PropertyVetoException e) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void setVisible(boolean value) {
        frame.setVisible(value);
    }

    public void moveToFront() {
        frame.moveToFront();
    }

    public void moveToBack() {
        frame.moveToBack();
    }

    public void setPreferredSize(Dimension d){
        frame.setPreferredSize(d);
    }

    public void pack(){
        frame.pack();
    }

}
