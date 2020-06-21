package net.vpc.common.jeep.editor;

import net.vpc.common.textsource.JTextSource;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSyntaxUtils {
    public static final String SOURCE_NAME="JSyntax.SourceName";

    public static String getSourceName(JTextComponent component){
        return (String) component.getClientProperty(SOURCE_NAME);
    }

    public static void setSourceName(JTextComponent component,String sourceName){
        component.putClientProperty(SOURCE_NAME,sourceName);
    }

    public static void setText(JTextComponent component, JTextSource source){
        component.putClientProperty(SOURCE_NAME,source.name());
        component.setText(source.text());
    }

    /**
     * A helper function that will return the SyntaxDocument attached to the
     * given text component.  Return null if the document is not a
     * SyntaxDocument, or if the text component is null
     */
    public static JSyntaxDocument getSyntaxDocument(JTextComponent component) {
        if (component == null) {
            return null;
        }
        Document doc = component.getDocument();
        if (doc instanceof JSyntaxDocument) {
            return (JSyntaxDocument) doc;
        } else {
            return null;
        }
    }

    public static int getLineCount(JTextComponent pane) {
        JSyntaxDocument sdoc = getSyntaxDocument(pane);
        if (sdoc != null) {
            return sdoc.getLineCount();
        }
        int count = 0;
        try {
            int p = pane.getDocument().getLength() - 1;
            if (p > 0) {
                count = getLineNumber(pane, p);
            }
        } catch (Exception ex) {
            Logger.getLogger(JSyntaxUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    /**
     * Gets the Line Number at the give position of the editor component.
     * The first line number is ZERO
     *
     * @return line number
     * @throws BadLocationException
     */
    public static int getLineNumber(JTextComponent editor, int pos){
        if (getSyntaxDocument(editor) != null) {
            JSyntaxDocument sdoc = getSyntaxDocument(editor);
            return sdoc.getLineNumberAt(pos);
        } else {
            Document doc = editor.getDocument();
            return doc.getDefaultRootElement().getElementIndex(pos);
        }
    }

    /**
     * Gets the column number at given position of editor.  The first column is
     * ZERO
     *
     * @return the 0 based column number
     * @throws javax.swing.text.BadLocationException
     */
    public static int getColumnNumber(JTextComponent editor, int pos){
        // speedup if the pos is 0
        if(pos == 0) {
            return 0;
        }
        Rectangle r = null;
        try {
            r = editor.modelToView(pos);
        } catch (BadLocationException e) {
            return -1;
        }
        int start = editor.viewToModel(new Point(0, r.y));
        int column = pos - start;
        return column;
    }

    public static Rectangle getScreenBoundsForPoint(int x, int y) {
        GraphicsEnvironment env = GraphicsEnvironment.
                getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();
        for (GraphicsDevice device : devices) {
            GraphicsConfiguration config = device.getDefaultConfiguration();
            Rectangle gcBounds = config.getBounds();
            if (gcBounds.contains(x, y)) {
                return gcBounds;
            }
        }
        // If point is outside all monitors, default to default monitor (?)
        return env.getMaximumWindowBounds();
    }
}
