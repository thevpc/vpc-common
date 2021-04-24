/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing;

import java.awt.Font;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;

/**
 *
 * @author vpc
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

    public static void addZoomTextOnMouseWheel(JComponent p) {
        p.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
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
            doc.setCharacterAttributes(start, end - start, attr, replace);
        }
        StyledEditorKit k = getStyledEditorKit(editor);
        MutableAttributeSet inputAttributes = k.getInputAttributes();
        if (replace) {
            inputAttributes.removeAttributes(inputAttributes);
        }
        inputAttributes.addAttributes(attr);
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
