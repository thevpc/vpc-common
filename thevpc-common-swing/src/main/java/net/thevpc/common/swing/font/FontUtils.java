/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.font;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 * @author thevpc
 */
public class FontUtils {

    /**
     * Parse a font from the syntax.properties file. Takes a comma-separated
     * list of the plain,bold and italic parameters
     *
     * @param font The font string
     * @return A font style constant
     */
    public static int parseFontFlags(String font) {
        int flags = Font.PLAIN;
        if (font != null) {
            StringTokenizer tokenizer = new StringTokenizer(font, ",");
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken().toLowerCase();

                if (token.equals("bold")) {
                    flags |= Font.BOLD;
                } else if (token.equals("italic")) {
                    flags |= Font.ITALIC;
                } else if (token.equals("plain")) {
                    /* Ignore */
                }
                /*else
                {
                    throw new Exception("Unknown font attribute");
                }*/
            }
        }
        return flags;
    }

    public static String formatFont(Font font) {
        StringBuilder n = new StringBuilder(font.getName());
        n.append(",").append(font.getSize());
        int s = font.getStyle();
        switch (s) {
            case Font.PLAIN: {
                break;
            }
            case Font.BOLD: {
                n.append(",bold");
                break;
            }
            case Font.ITALIC: {
                n.append(",italic");
                break;
            }
            default: {
                if (font.isItalic() && font.isBold()) {
                    n.append(",bold,italic");
                }
            }
        }
        return n.toString();
    }

    public static Font parseFont(String font) {
        int style = Font.PLAIN;
        int size = 14;
        String name = null;
        boolean nameVisited = false;
        if (font != null) {
            StringTokenizer tokenizer = new StringTokenizer(font, ",");
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken().toLowerCase();
                token = token.trim();
                if (token.length() > 0) {
                    if (!nameVisited) {
                        name = token;
                        nameVisited = true;
                    } else {
                        if (token.equals("bold")) {
                            style |= Font.BOLD;
                        } else if (token.equals("italic")) {
                            style |= Font.ITALIC;
                        } else if (token.equals("plain")) {
                            style = Font.PLAIN;
                        } else if (token.matches("[0-9]+")) {
                            size = Integer.parseInt(token);
                        }
                    }
                }
            }
        }
        if (name == null) {
            return null;
        }
        return new Font(name, style, size);
    }

    public static Font deriveFont(Font _font, boolean bold, boolean italic, boolean underline, boolean strike) {
        Font f = _font.deriveFont((bold ? Font.BOLD : 0) + (italic ? Font.ITALIC : 0));
        Map attributes = null;
        if (underline) {
            if (attributes == null) {
                attributes = f.getAttributes();
            }
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        }
        if (strike) {
            if (attributes == null) {
                attributes = f.getAttributes();
            }
            attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        }
        if (attributes != null) {
            f = f.deriveFont(attributes);
        }
        return f;
    }
}
