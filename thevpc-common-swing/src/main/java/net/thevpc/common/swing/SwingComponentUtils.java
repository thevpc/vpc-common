/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing;

import java.awt.Font;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Enumeration;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.text.*;

/**
 *
 * @author thevpc
 */
public class SwingComponentUtils {

    private static class ZoomInfo {

        private double textZoom = 1;
        private Font initialTextFont = null;
    }

    private static ZoomInfo getZoomInfo(JComponent p) {
        ZoomInfo z = (ZoomInfo) p.getClientProperty("ZoomInfo");
        if (z == null) {
            z = new ZoomInfo();
            p.putClientProperty("ZoomInfo", z);
        }
        return z;
    }

    public static double getTextZoom(JComponent p) {
        return getZoomInfo(p).textZoom;
    }

    public static void setTextZoom(JComponent p, double z) {
        if (Double.isNaN(z)) {
            z = 1;
        }
        ZoomInfo zoom = getZoomInfo(p);
        if (zoom.initialTextFont == null) {
            zoom.initialTextFont = p.getFont();
        }
        int size0 = zoom.initialTextFont.getSize();
        float newSize = (float) (zoom.textZoom * size0);
        if (newSize < 5) {
            newSize = 5;
            z=newSize*1.0/size0;
        }else if (newSize > 1000) {
            newSize = 1000;
            z=newSize*1.0/size0;
        }
        p.setFont(zoom.initialTextFont.deriveFont(newSize));
        zoom.textZoom = z;
    }

    public static void evalZoomTextOnMouseWheel(MouseWheelEvent e,JComponent p) {
        if (e.isControlDown()) {
            double oldZoom = getTextZoom(p);
            double amount = Math.pow(1.1, 1);
            if (e.getWheelRotation() < 0) {
                //zoom in (amount)
                setTextZoom(p, oldZoom * amount);
            } else {
                //zoom out (amount)
                setTextZoom(p, oldZoom / amount);
            }
        } else {
            p.getParent().dispatchEvent(e);
        }
    }

    public static void addZoomTextOnMouseWheel(JComponent p) {
        p.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                evalZoomTextOnMouseWheel(e,p);
            }
        });
    }

    public static int getLineFromOffset(JTextComponent component, int offset) {
        return component.getDocument().getDefaultRootElement().getElementIndex(offset);
    }

    public static int getLineStartOffsetForLine(JTextComponent component, int line) {
        return component.getDocument().getDefaultRootElement().getElement(line).getStartOffset();
    }

    public static int getLineEndOffsetForLine(JTextComponent component, int line) {
        return component.getDocument().getDefaultRootElement().getElement(line).getEndOffset();
    }

    public static final void setCharacterAttributes(JEditorPane editor,
            AttributeSet attr, boolean replace) {
        int start = editor.getSelectionStart();
        int end = editor.getSelectionEnd();
        if (start != end) {
            StyledDocument doc = getStyledDocument(editor);
            doc.setCharacterAttributes(start, end - start, copyRemoveNulls(attr), replace);
        }
        StyledEditorKit k = getStyledEditorKit(editor);
        MutableAttributeSet inputAttributes = k.getInputAttributes();
        if (replace) {
            inputAttributes.removeAttributes(inputAttributes);
        }
        Enumeration<?> n = attr.getAttributeNames();
        while(n.hasMoreElements()) {
            Object kk=n.nextElement();
            Object vv = attr.getAttribute(kk);
            if(vv!=null) {
                inputAttributes.addAttribute(kk, vv);
            }else{
                inputAttributes.removeAttribute(kk);
            }
        }
    }
    private static MutableAttributeSet copyRemoveNulls(AttributeSet attr){
        MutableAttributeSet inputAttributes = new SimpleAttributeSet();
        Enumeration<?> n = attr.getAttributeNames();
        while(n.hasMoreElements()) {
            Object kk=n.nextElement();
            Object vv = attr.getAttribute(kk);
            if(vv!=null) {
                inputAttributes.addAttribute(kk, vv);
            }else{
                inputAttributes.removeAttribute(kk);
            }
        }
        return inputAttributes;
    }

    public static final StyledDocument getStyledDocument(JEditorPane e) {
        Document d = e.getDocument();
        if (d instanceof StyledDocument) {
            return (StyledDocument) d;
        }
        throw new IllegalArgumentException("extected StyledDocument");
    }

    public static final StyledEditorKit getStyledEditorKit(JEditorPane e) {
        EditorKit k = e.getEditorKit();
        if (k instanceof StyledEditorKit) {
            return (StyledEditorKit) k;
        }
        throw new IllegalArgumentException("expected StyledEditorKit");
    }
}
